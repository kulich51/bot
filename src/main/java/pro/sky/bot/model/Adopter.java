package pro.sky.bot.model;


import javax.persistence.*;
import java.util.Date;

/**
 * сущность таблицы усыновителей
 * idUser - идентификатор пользователя
 * idPet   -идентификатор животного
 * isChecked  - на испытательном сроке
 * isAdopter  - усыновил
 * dateProbation - дата срока
 * probationDays - количество дней
 * extraDays     - доп дни
 * is_checked = true - весь испытательный срок
 * по окончанию is_checked = false
 * kind - вид животного
 */
@Entity
@Table(name = "adopter")
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "user_id")
    public Long userId;

//    @OneToOne (cascade = CascadeType.ALL)
//    @JoinColumn (name = "pet_id", referencedColumnName = "id")
//    public Pet pet;
    @Column(name = "pet_id")
    public Long petId;

    @Column(name = "is_probation_checked")
    public boolean isProbationChecked;
    @Column(name = "start_date_probation")
    public Date startDateProbation;
    @Column(name = "finish_date_probation")
    public Date finishDateProbation;

    public Adopter() {
    }

    public Adopter(Long id, Long userId, Long petId, boolean isProbationChecked, Date startDateProbation, Date finishDateProbation) {
        this.id = id;
        this.userId = userId;
        this.petId = petId;
        this.isProbationChecked = isProbationChecked;
        this.startDateProbation = startDateProbation;
        this.finishDateProbation = finishDateProbation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public boolean isProbationChecked() {
        return isProbationChecked;
    }

    public void setProbationChecked(boolean probationChecked) {
        isProbationChecked = probationChecked;
    }

    public Date getStartDateProbation() {
        return startDateProbation;
    }

    public void setStartDateProbation(Date startDateProbation) {
        this.startDateProbation = startDateProbation;
    }

    public Date getFinishDateProbation() {
        return finishDateProbation;
    }

    public void setFinishDateProbation(Date finishDateProbation) {
        this.finishDateProbation = finishDateProbation;
    }

    @Override
    public String toString() {
        return "Adopter{" +
                "id=" + id +
                ", userId=" + userId +
                ", petId=" + petId +
                ", isProbationChecked=" + isProbationChecked +
                ", startDateProbation=" + startDateProbation +
                ", finishDateProbation=" + finishDateProbation +
                '}';
    }
}
