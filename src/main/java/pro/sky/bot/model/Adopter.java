package pro.sky.bot.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * сущность таблицы усыновителей
 * idUser - идентификатор пользователя
 * idPet   -идентификатор животного
 * isChecked  - на испытательном сроке
 * isAdopter  - усыновил
 * dateProbation - дата срока
 * probationDays - количество дней
 * extraDays     - доп дни
 * <p>
 * is_checked = true - весь испытательный срок
 * по окончанию is_checked = false
 * kind - вид животного
 */
@Entity
@Table(name = "adopter")
public class Adopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "user_id")
    public Long userId;
    @Column(name = "pet_id")
    public Long petId;
    @Column(name = "is_probation_checked")
    public boolean isProbation_checked;
    @Column(name = "date_Probation")
    public LocalDate dateProbation;
    @Column(name = "probation_days")
    public int probationDays;
    @Column(name = "extra_days")
    public int extraDays;
    @JoinColumn(name = "kind")
    private Pets kind;

    public Adopter(Long userId, Long petId, boolean isProbation_checked, LocalDate dateProbation, int probationDays, int extraDays, Pets kind) {
        this.userId = userId;
        this.petId = petId;
        this.isProbation_checked = isProbation_checked;
        this.dateProbation = dateProbation;
        this.probationDays = probationDays;
        this.extraDays = extraDays;
        this.kind = kind;
    }

    public Adopter() {
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

    public boolean isProbation_checked() {
        return isProbation_checked;
    }

    public void setProbation_checked(boolean probation_checked) {
        isProbation_checked = probation_checked;
    }

    public LocalDate getDateProbation() {
        return dateProbation;
    }

    public void setDateProbation(LocalDate dateProbation) {
        this.dateProbation = dateProbation;
    }

    public int getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(int probationDays) {
        this.probationDays = probationDays;
    }

    public int getExtraDays() {
        return extraDays;
    }

    public void setExtraDays(int extraDays) {
        this.extraDays = extraDays;
    }

    public Pets getKind() {
        return kind;
    }

    public void setKind(Pets kind) {
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return isProbation_checked == adopter.isProbation_checked && probationDays == adopter.probationDays && extraDays == adopter.extraDays && Objects.equals(userId, adopter.userId) && Objects.equals(petId, adopter.petId) && Objects.equals(dateProbation, adopter.dateProbation) && kind == adopter.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, petId, isProbation_checked, dateProbation, probationDays, extraDays, kind);
    }

    @Override
    public String toString() {
        return "Adopter{" +
                "userId=" + userId +
                ", petId=" + petId +
                ", isProbation_checked=" + isProbation_checked +
                ", dateProbation=" + dateProbation +
                ", probationDays=" + probationDays +
                ", extraDays=" + extraDays +
                ", kind=" + kind +
                '}';
    }
}
