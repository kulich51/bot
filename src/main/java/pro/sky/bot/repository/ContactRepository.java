package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.Contact;

import java.util.Collection;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Collection<Contact> findAllByUserIdIn(Collection<Long> userId);
    Contact findByUserId(Long userId);
}
