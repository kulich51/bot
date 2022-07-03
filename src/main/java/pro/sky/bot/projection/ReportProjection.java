package pro.sky.bot.projection;

import pro.sky.bot.model.Report;

import java.util.Collection;

public interface ReportProjection {

    Collection<Report> getReports();
}
