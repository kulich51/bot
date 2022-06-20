package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.keyboard.StartMenuKeyboard;
import pro.sky.bot.model.CatShelter;
import pro.sky.bot.model.DogShelter;
import pro.sky.bot.model.Pets;
import pro.sky.bot.model.Shelter;
import pro.sky.bot.service.ConsultationService;

import java.io.IOException;
import java.util.Collections;

@Service
public class NewUserConsultationServiceImpl extends MessageSender implements ConsultationService {

    private static final String RULES_FILE_NAME = "rules.txt";

    @Override
    public BaseRequest parse(Long chatId, String userMessage, Pets pet) throws IOException {
        Shelter shelter = chooseShelter(pet);
        switch (userMessage) {
            case (StartMenuKeyboard.ABOUT_BUTTON):
                return sendMessage(chatId, "Какая информация интересует?", InfoKeyboard.infoKeyBoard());
            case (InfoKeyboard.RULES_BUTTON):
                return sendMessageFromTextFile(chatId, "rules.txt");
            default:
                return sendDefaultMessage(chatId);
        }
    }

    private Shelter chooseShelter(Pets pet) {
        if (pet.equals(Pets.DOG)) {
            return new DogShelter();
        }
        return new CatShelter();
    }

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
     *
     * @param chatId
     * @param pet
     * @return
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

    public SendMessage getSecurityPhoneMessage(Long chatId, Pets pet) {

        String message = "Для оформления пропуска на машину позвонить по телефону:\n"
                + chooseShelter(pet).getSecurityPhone();
        return sendMessage(chatId, message);
    }

    public SendMessage getRulesMessage(Long chatId) {

        try {
            return sendMessageFromTextFile(chatId, RULES_FILE_NAME);
        } catch (IOException e) {
            return sendMessage(chatId, "Непредвиденная ошибка");
        }
    }
}