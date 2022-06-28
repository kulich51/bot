package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet getByName(String name);
}
