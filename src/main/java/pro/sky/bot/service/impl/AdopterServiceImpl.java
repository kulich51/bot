package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import pro.sky.bot.exception.AdopterNotFoundException;
import pro.sky.bot.exception.ContactNotFoundException;
import pro.sky.bot.exception.InvalidProbationDaysCountException;
import pro.sky.bot.exception.ProbationPeriodNotEndException;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.model.Contact;
import pro.sky.bot.model.Pet;
import pro.sky.bot.repository.AdopterRepository;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.service.AdopterService;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Service
public class AdopterServiceImpl implements AdopterService {

    private final TelegramBot telegramBot;
    private final AdopterRepository adopterRepository;
    private final ContactRepository contactRepository;
    private final PetRepository petRepository;

    public AdopterServiceImpl(TelegramBot telegramBot, AdopterRepository adopterRepository, ContactRepository contactRepository, PetRepository petRepository) {
        this.telegramBot = telegramBot;
        this.adopterRepository = adopterRepository;
        this.contactRepository = contactRepository;
        this.petRepository = petRepository;
    }

    @Override
    public Adopter add(Adopter adopter) {

        checkContactExist(adopter.getUserId());
        checkPetExist(adopter.getPetId());

        Date start = adopter.getStartDateProbation();
        adopter.setFinishDateProbation(getFinishDate(start, 30));
        adopter.setProbationChecked(false);
        return adopterRepository.save(adopter);
    }

    /**
     * Get contact from db by userId and check it for null
     * @param userId user id
     */
    private void checkContactExist(Long userId) {

        Contact contact = contactRepository.findByUserId(userId);
        if (contact == null) {
            throw new ContactNotFoundException();
        }
    }

    /**
     * Get pet from db by pet_id and check it for null
     * @param petId pet id
     */
    private void checkPetExist(Long petId) {

        Pet pet = petRepository.getById(petId);
        if (pet == null) {
            throw new ContactNotFoundException();
        }
    }

    @Override
    public Collection<Adopter> getAdopters(Boolean onProbation) {

        if (onProbation) {
            return adopterRepository.getAdopterOnProbation();
        }
        return adopterRepository.findAll();
    }

    @Override
    public Collection<Adopter> getAdopter(Long id) {

        Collection<Adopter> adopter = adopterRepository.getAdopterByUserId(id);
        checkObjectNotNull(adopter);
        return adopter;
    }

    @Override
    public Adopter changeProbation(Adopter adopter, boolean acceptProbation, int days) {

        Adopter oldAdopter = getAdopterByUserIdAndPetId(adopter.getUserId(), adopter.getPetId());
        changeProbationStatus(oldAdopter, acceptProbation);
        changeProbationDate(oldAdopter, days);
        return adopterRepository.save(oldAdopter);
    }

    private void changeProbationStatus(Adopter adopter, boolean acceptProbation) {
        if (acceptProbation) {
            Instant instant = Instant.now();
            Date today = Date.from(instant);
            Date finishDate = adopter.getFinishDateProbation();

            if (today.after(finishDate) || today.equals(finishDate)) {
                adopter.setProbationChecked(true);
                return;
            }
            throw new ProbationPeriodNotEndException();
        }
    }

    private void changeProbationDate(Adopter adopter, int days) {

        Adopter oldAdopter = getAdopterByUserIdAndPetId(adopter.getUserId(), adopter.getPetId());
        if (days == 0) {
            return;
        } else if (days == 14 || days == 30) {
            Date newDate = DateUtils.addDays(oldAdopter.getFinishDateProbation(), days);
            oldAdopter.setFinishDateProbation(newDate);
            sendMessage(adopter.getUserId(), days);
            return;
        }
        throw new InvalidProbationDaysCountException();
    }

    @Override
    public void removeAdopter(Long id) {
        adopterRepository.deleteById(id);
    }

    private void sendMessage(Long userId, int days) {

        Contact contact = contactRepository.findByUserId(userId);
        String message = "Уважаемый усыновитель!\nСообщаем, что вам продлен испытательный срок на " + days + " дней";
        telegramBot.execute(new SendMessage(contact.getChatId(), message));
    }

    private Adopter getAdopterByUserIdAndPetId(Long userId, Long petId) {

        Adopter adopter = adopterRepository.getAdopterByUserIdAndPetId(userId, petId);
        checkObjectNotNull(adopter);
        return adopter;
    }

    private void checkObjectNotNull(Object adopter) {
        if (adopter == null) {
            throw new AdopterNotFoundException();
        }
    }

    Date getFinishDate(Date startDate, int days) {

        return DateUtils.addDays(startDate, days);
    }
}
