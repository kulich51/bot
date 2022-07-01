package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.bot.model.Pet;
import pro.sky.bot.model.Report;
import pro.sky.bot.projection.ReportProjection;

import java.util.Collection;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet getByName(String name);
    ReportProjection findAllByName(String name);
}
