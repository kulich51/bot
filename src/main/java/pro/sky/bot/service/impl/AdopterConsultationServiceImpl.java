package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.keyboard.AdopterConsultationKeyboard;
import pro.sky.bot.keyboard.CatRecommendationsKeyboard;
import pro.sky.bot.keyboard.DogRecommendationsKeyboard;
import pro.sky.bot.model.Volunteer;
import pro.sky.bot.repository.VolunteerRepository;
import pro.sky.bot.service.ConsultationService;

import java.util.List;

@Service
public class AdopterConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final VolunteerRepository volunteerRepository;

    private final String GREETING = "Здесь вы можете разобраться с бюрократическими (оформление договора)" +
            " и бытовыми (как подготовиться к жизни с животным) вопросам";

    public AdopterConsultationServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Get start keyboard for adopter consultation
     *
     * @param chatId chat identifier
     * @return SenMessage object with keyboard
     */
    @Override
    public SendMessage getKeyboard(Long chatId) {

        Keyboard keyboard = AdopterConsultationKeyboard.adopterConsultationKeyboard();
        return sendMessage(chatId, GREETING, keyboard);
    }

    /**
     * Get volunteer contact to text him
     *
     * @param chatId chat identifier
     * @return SenMessage object with volunteer contact in message
     */
    @Override
    public SendMessage getVolunteerContact(Long chatId) {

        List<Volunteer> volunteers = volunteerRepository.findAll();
        Volunteer random = volunteers.get(getRandomNumber(0, volunteers.size() - 1));
        String message = "Обратитесь за помощью к " + random.getUsername();
        return sendMessage(chatId, message);
    }

    /**
     * Get rules of acquaintance depending on pet type
     *
     * @param chatId chat identifier
     * @param pet    pet type (DOG or CAT)
     * @return SenMessage object information about acquaintance
     */
    public SendMessage getRulesOfAcquaintance(Long chatId, Pets pet) {

        String fileName = "rules of acquaintance.txt";
        String filePath = getPath(pet) + fileName;
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get necessary list of documents (common for pets and dogs)
     *
     * @param chatId chat identifier
     * @return SenMessage with list of documents in text
     */
    public SendMessage getListOfDocuments(Long chatId) {

        String message = "Чтобы взять животное из приюта необходим паспорт.\n" +
                "В день взятия животного из приюта заключается договор о передаче животного";

        return sendMessage(chatId, message);
    }

    /**
     * Get recommendations about pet transportation
     *
     * @param chatId chat identifier
     * @param pet    pet kind (DOG or CAT)
     * @return SendMessage with recommendations about pet transportation in text
     */
    public SendMessage getTransportRecommendations(Long chatId, Pets pet) {

        String fileName = "transport_recommendations.txt";
        String filePath = getPath(pet) + fileName;
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get keyboard with recommendations for home improvement
     *
     * @param chatId chat identifier
     * @param pet    pet kind (DOG or CAT)
     * @return SenMessage with keyboard
     */
    public SendMessage getHomeImprovementRecommendations(Long chatId, Pets pet) {

        Keyboard keyboard = getRecommendationKeyboard(pet);
        String message = "Выберите рекомендации из списка ниже";
        return sendMessage(chatId, message, keyboard);
    }

    /**
     * Get text recommendations for young pet
     *
     * @param chatId chat identifier
     * @param pet    pet kind (DOG or CAT)
     * @return SendMessage with recommendations for young pet in text
     */
    public SendMessage getRecommendationYoungPet(Long chatId, Pets pet) {

        String fileName = "young_home_improvement.txt";
        String filePath = getPath(pet) + fileName;
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get text recommendations for adult pet
     *
     * @param chatId chat identifier
     * @param pet    pet kind (DOG or CAT)
     * @return SendMessage with recommendations for adult pet in text
     */
    public SendMessage getRecommendationAdultPet(Long chatId, Pets pet) {

        String fileName = "adult_home_improvement.txt";
        String filePath = getPath(pet) + fileName;
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get text recommendations for disability pet
     *
     * @param chatId chat identifier
     * @param pet    pet kind (DOG or CAT)
     * @return SendMessage with recommendations for disability pet in text
     */
    public SendMessage getRecommendationDisabilityPet(Long chatId, Pets pet) {

        String fileName = "disability_home_improvement.txt";
        String filePath = getPath(pet) + fileName;
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get text reasons of refusal
     *
     * @param chatId chat identifier
     * @return SendMessage with reasons of refusal in text
     */
    public SendMessage getReasonsOfRefusal(Long chatId) {

        String filePath = "common/reasons_of_refusal.txt";
        return sendMessageFromTextFile(chatId, filePath);
    }

    /**
     * Get random number from interval
     *
     * @param min min number
     * @param max max number
     * @return random number
     */
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Get pet shelter
     *
     * @param pet type of pet shelter from enum (cat or dog)
     * @return pet shelter
     */
    private String getPath(Pets pet) {
        if (pet.equals(Pets.DOG)) {
            return "dog/";
        }
        return "cat/";
    }

    /**
     * Get recommendation for adopters
     * @param pet object of shelter
     * @return cat or dog recommendations
     */
    private Keyboard getRecommendationKeyboard(Pets pet) {

        if (pet.equals(Pets.DOG)) {
            return DogRecommendationsKeyboard.keyboard();
        }
        return CatRecommendationsKeyboard.keyboard();
    }
}
