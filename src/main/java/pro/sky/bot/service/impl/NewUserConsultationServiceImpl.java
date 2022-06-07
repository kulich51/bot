package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.model.Shelter;
import pro.sky.bot.service.ConsultationService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

@Service
public class NewUserConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final String START_MESSAGE = "Мы приют домашних живтоных. Здесь вы можете взять животное из нашего приюта.\n\n" +
                        "Для взаимодействия вы можете использвать следующие команды:\n" +
                        "/start - перейти к стартовому сообщению\n" +
                        "/shelter_info - узнать информацию о приюте\n" +
                        "/take_pet - взять животное из приюта\n" +
                        "/volunteer - позвать волонтера для общения";


    private final String START_URL = "/start";
    private final String INFO_URL = "/shelter_info";

    private final Shelter shelter = new Shelter();


    @Override
    public BaseRequest parse(Long chatId, String userMessage) throws IOException {
        switch (userMessage) {
            case (START_URL):
                return sendMessage(chatId, START_MESSAGE);
            case (INFO_URL):
                return sendMessage(chatId, "Какая информация интересует?", InfoKeyboard.infoKeyBoard());
            case (InfoKeyboard.SCHEDULE_BUTTON):
                return new SendPhoto(chatId, getMapByCoordinates(shelter.getCoordinates()));
            case (InfoKeyboard.RULES_BUTTON):
                return sendMessageFromTextFile(chatId, "rules.txt");
            case (InfoKeyboard.ABOUT_BUTTON):
                return getRulesFromFile(chatId, shelter.getShelterInfoPath());
            default:
                return sendDefaultMessage(chatId);
        }
    }

    /**
     * Get static map from by Yandex static API
     * @param coordinates coordinates of a point (String format)
     * @return static map in byte array
     */
    private byte[] getMapByCoordinates(String coordinates) {

        String url =
                "https://static-maps.yandex.ru/1.x/?ll=" + coordinates +
                        "&size=450,450&z=15&l=map&pt=" + coordinates +  ",flag";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.IMAGE_GIF));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return response.getBody();
    }
}
