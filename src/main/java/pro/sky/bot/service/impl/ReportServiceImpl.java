package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.exception.ReportNotFoundException;
import pro.sky.bot.model.Contact;
import pro.sky.bot.model.Photo;
import pro.sky.bot.model.Report;
import pro.sky.bot.projection.ReportProjection;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.repository.ReportRepository;
import pro.sky.bot.service.ReportService;

import java.util.Collection;
import java.util.Collections;

@Service
public class ReportServiceImpl implements ReportService {

    private final TelegramBot telegramBot;
    private final ReportRepository reportRepository;
    private final PetRepository petRepository;
    private final ContactRepository contactRepository;

    public ReportServiceImpl(TelegramBot telegramBot, ReportRepository reportsRepository, PetRepository petRepository, ContactRepository contactRepository) {
        this.telegramBot = telegramBot;
        this.reportRepository = reportsRepository;
        this.petRepository = petRepository;
        this.contactRepository = contactRepository;
    }

    private final String REPORT_NOT_ACCEPTED = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет " +
            "не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. " +
            "В противном случае, волонтеры приюта будут обязаны самолично проверять условия содержания животного";

    @Override
    public Collection<Report> getReportsByPet(String petName) {

        ReportProjection reports = getPetReports(petName);
        return reports.getReports();
    }

    @Override
    public Report getReportByPetNameAndId(String petName, Long reportId, Boolean accept) {

        ReportProjection reports = getPetReports(petName);
        Report report = getReportById(reportId, reports.getReports());

        if (accept == false) {
            report.setAccepted(false);
            reportRepository.save(report);
            sendMessageToAdopter(report.getUserId());
        }
        return report;
    }

    private void sendMessageToAdopter(Long userId) {

        Contact contact = contactRepository.findByUserId(userId);
        telegramBot.execute(new SendMessage(contact.getChatId(), REPORT_NOT_ACCEPTED));
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
}
