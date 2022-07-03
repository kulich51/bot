package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.model.*;
import pro.sky.bot.repository.VolunteerRepository;
import pro.sky.bot.service.ConsultationService;

import java.util.Collections;
import java.util.List;

@Service
public class NewUserConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final VolunteerRepository volunteerRepository;

    private static final String RULES_FILE_NAME = "rules.txt";

    public NewUserConsultationServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Get InfoKeyboard
     * @param chatId - chatId chat identifier
     * @return SendMessage object with keyboard
     */
    @Override
    public SendMessage getKeyboard(Long chatId) {
        return sendMessage(chatId, "Какая информация интересует?", InfoKeyboard.infoKeyBoard());
    }

    /**
     * Get volunteer contact to text him
     * @param chatId chatId chat identifier
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
     * Get pet shelter
     * @param pet type of pet shelter from enum (cat or dog)
     * @return pet shelter
     */
    private Shelter chooseShelter(Pets pet) {
        if (pet.equals(Pets.DOG)) {
            return new DogShelter();
        }
        return new CatShelter();
    }

    /**
     * Get information about shelter
     * @param chatId chatId chat identifier
     * @param pet type of pet shelter from enum (cat or dog)
     * @return SenMessage object with shelter info in message
     */
    public SendMessage getAboutMessage(Long chatId, Pets pet) {

        StringBuilder sb = new StringBuilder("Мы приют для ");
        if (pet.equals(Pets.DOG)) {
            sb.append("собак ");
        } else {
            sb.append("кошек ");
        }
        sb.append(chooseShelter(pet).getName());
        return sendMessage(chatId, sb.toString());
    }

    /**
     * Get shelter schedule
     * @param chatId chatId chat identifier
     * @param pet type of pet shelter from enum (cat or dog)
     * @return SenMessage object with shelter schedule in message
     */
    public SendMessage getShelterScheduleMessage(Long chatId, Pets pet) {

        Shelter shelter = chooseShelter(pet);
        StringBuilder sb = new StringBuilder("Мы находимся по адресу: ");
        sb.append(shelter.getAddress());
        sb.append("\nРежим работы приюта: ");
        sb.append(shelter.getSchedule());
        sb.append("\nСхема проезда: ");
        return sendMessage(chatId, sb.toString());
    }

    /**
     * Get static map image by coordinates
     * @param chatId chat identifier
     * @param pet type of pet shelter from enum (cat or dog)
     * @return SendPhoto image with byte array in message
     */
    public SendPhoto getMapByCoordinates(Long chatId, Pets pet) {

        String coordinates = chooseShelter(pet).getCoordinates();

        String url =
                "https://static-maps.yandex.ru/1.x/?ll=" + coordinates +
                        "&size=450,450&z=15&l=map&pt=" + coordinates +  ",flag";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.IMAGE_GIF));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return new SendPhoto(chatId, response.getBody());
    }

    /**
     * Get security phone
     * @param chatId chat identifier
     * @param pet type of pet shelter from enum (cat or dog)
     * @return SenMessage object with security in message
     */
    public SendMessage getSecurityPhoneMessage(Long chatId, Pets pet) {

        String message = "Для оформления пропуска на машину позвонить по телефону:\n"
                + chooseShelter(pet).getSecurityPhone();
        return sendMessage(chatId, message);
    }

    /**
     * Get rules of shelter which are stored in a file
     * @param chatId chat identifier
     * @return SenMessage object with rules in message
     */
    public SendMessage getRulesMessage(Long chatId) {

        return sendMessage(chatId, "Непредвиденная ошибка");
    }

    /**
     * Get random number from interval
     * @param min min number
     * @param max max number
     * @return random number
     */
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}