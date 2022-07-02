package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.bot.model.Pet;
import pro.sky.bot.model.Report;

import java.util.Collection;
import java.util.Date;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findByUserIdAndPetAndDate(Long userId, Pet pet, Date date);
    Collection<Report> getReportsByDate(Date date);
}
