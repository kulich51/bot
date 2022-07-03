package pro.sky.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.bot.model.Adopter;

import java.util.Collection;
import java.util.Date;

@Repository("adopterRepository")
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Collection<Adopter> getAdopterByUserId(Long userId);
    Adopter getAdopterByUserIdAndPetId(Long userId, Long petId);

    @Query("select a from Adopter a where a.finishDateProbation > current_date")
    Collection<Adopter> getAdopterOnProbation();

    @Query(nativeQuery = true,
            value = "select * " +
                    "from adopter a " +
                    "where a.finish_date_probation = ?1 and a.is_probation_checked = ?2")
    Collection<Adopter> getAdopterWithEndProbation(Date date, Boolean checked);


}
