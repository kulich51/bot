package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.DatabaseContact;

public interface ContactRepository extends JpaRepository<DatabaseContact, Long> {
}
