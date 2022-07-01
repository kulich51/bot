package pro.sky.bot.service.impl;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.exception.ReportNotFoundException;
import pro.sky.bot.model.Photo;
import pro.sky.bot.model.Report;
import pro.sky.bot.projection.ReportProjection;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.repository.ReportRepository;
import pro.sky.bot.service.ReportService;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PetRepository petRepository;

    public ReportServiceImpl(ReportRepository reportsRepository, PetRepository petRepository) {
        this.reportRepository = reportsRepository;
        this.petRepository = petRepository;
    }

    @Override
    public Collection<Report> getReportsByPet(String petName) {

        ReportProjection reports = getPetReports(petName);
        return reports.getReports();
    }

    @Override
    public Report getReportByPetNameAndId(String petName, Long reportId, Boolean accept) {

        ReportProjection reports = getPetReports(petName);
        Report report = getReportById(reportId, reports.getReports());

        if (accept == true) {

            report.setAccepted(true);
            reportRepository.save(report);
        }
        return report;
    }

    private ReportProjection getPetReports(String petName) {

        ReportProjection reports = petRepository.findAllByName(petName);
        if (reports == null) {
            throw new ReportNotFoundException();
        }
        return reports;
    }

    private Report getReportById(Long reportId, Collection<Report> reports) {

        Report adopterReport = reports
                .stream()
                .filter(report -> report.getId() == reportId)
                .findFirst()
                .orElseThrow(ReportNotFoundException::new);

        return adopterReport;
    }

    @Override
    public Photo getReportPhoto(String petName, Long reportId) {

        Report report = getReportById(reportId, getPetReports(petName).getReports());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(report.getFilePath(), HttpMethod.GET, httpEntity, byte[].class);

        Photo photo = new Photo(result.getBody(), parseMediaType(report.getFilePath()));
        return photo;
    }

    private String parseMediaType(String url) {

        int lastIndex = url.lastIndexOf(".");
        return url.substring(lastIndex + 1);
    }

    private Date getTodayDate() {

        Instant instant = Instant.now();
        return Date.from(instant);
    }
}
