package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.bot.model.Adopter;

import java.util.Collection;

@Repository("adopterRepository")
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Collection<Adopter> getAdopterByUserId(Long userId);
}
