package pro.sky.bot.model;


import pro.sky.bot.enums.Pets;

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

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "pet_id", referencedColumnName = "id")
    public Pet pet;

    @Column(name = "is_probation_checked")
    public boolean isProbation_checked;
    @Column(name = "date_Probation")
    public LocalDate dateProbation;
    @Column(name = "probation_days")
    public int probationDays;
    @Column(name = "extra_days")
    public int extraDays;

    public Adopter(Long id, Long userId, Pet pet, boolean isProbation_checked, LocalDate dateProbation, int probationDays, int extraDays) {
        this.id = id;
        this.userId = userId;
        this.pet = pet;
        this.isProbation_checked = isProbation_checked;
        this.dateProbation = dateProbation;
        this.probationDays = probationDays;
        this.extraDays = extraDays;
    }

    public Adopter() {
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

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
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

    @Override
    public String toString() {
        return "Adopter{" +
                "id=" + id +
                ", userId=" + userId +
                ", pet=" + pet +
                ", isProbation_checked=" + isProbation_checked +
                ", dateProbation=" + dateProbation +
                ", probationDays=" + probationDays +
                ", extraDays=" + extraDays +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adopter adopter = (Adopter) o;

        if (isProbation_checked != adopter.isProbation_checked) return false;
        if (probationDays != adopter.probationDays) return false;
        if (extraDays != adopter.extraDays) return false;
        if (id != null ? !id.equals(adopter.id) : adopter.id != null) return false;
        if (userId != null ? !userId.equals(adopter.userId) : adopter.userId != null) return false;
        if (pet != null ? !pet.equals(adopter.pet) : adopter.pet != null) return false;
        return dateProbation != null ? dateProbation.equals(adopter.dateProbation) : adopter.dateProbation == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (pet != null ? pet.hashCode() : 0);
        result = 31 * result + (isProbation_checked ? 1 : 0);
        result = 31 * result + (dateProbation != null ? dateProbation.hashCode() : 0);
        result = 31 * result + probationDays;
        result = 31 * result + extraDays;
        return result;
    }
}
