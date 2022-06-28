package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.Report;

public interface ReportsRepository extends JpaRepository<Report, Long> {
}
