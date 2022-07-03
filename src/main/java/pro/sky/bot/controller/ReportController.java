package pro.sky.bot.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.model.Photo;
import pro.sky.bot.model.Report;
import pro.sky.bot.service.ReportService;

import java.util.Collection;

/**
 * <b>ReportController</b> - controller of reports.<br/>
 */
@RestController
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET http://localhost:8080/reports
     * get record by pet name
     */
    @GetMapping("{petName}")
    ResponseEntity<Collection<Report>> getReportsByPet(@PathVariable String petName) {

        return ResponseEntity.ok(reportService.getReportsByPet(petName));
    }

    /**
     * GET http://localhost:8080/reports
     * get record by pet name / report ID
     */
    @GetMapping("{petName}/{reportId}")
    ResponseEntity<Report> getReportByPetNameAndId(@PathVariable String petName,
                                                   @PathVariable Long reportId,
                                                   @RequestParam(defaultValue = "true") Boolean accept) {

        return ResponseEntity.ok(reportService.getReportByPetNameAndId(petName, reportId, accept));
    }

    /**
     * GET http://localhost:8080/reports
     * get record by pet name / report ID / photo
     */
    @GetMapping("{petName}/{reportId}/photo")
    ResponseEntity<byte[]> getReportPhoto(@PathVariable String petName,
                                          @PathVariable Long reportId) {

        Photo photo = reportService.getReportPhoto(petName, reportId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(photo.getData());
    }
}
