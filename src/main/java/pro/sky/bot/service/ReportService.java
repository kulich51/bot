package pro.sky.bot.service;

import pro.sky.bot.model.Photo;
import pro.sky.bot.model.Report;

import java.util.Collection;

public interface ReportService {

    Collection<Report> getReportsByPet(String petName);
    Report getReportByPetNameAndId(String petName, Long reportId, Boolean accpeted);

    Photo getReportPhoto(String petName, Long reportId);
}
