package pro.sky.bot.repository;

import com.pengrad.telegrambot.model.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.Volunteer;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findAll();
}
