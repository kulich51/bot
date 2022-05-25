package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.service.NewUserConsultationService;

@Service
public class NewUserConsultationServiceImpl implements NewUserConsultationService {

    private final String START_MESSAGE = "Мы приют домашних живтоных. Здесь вы можете взять животное из нашего приюта.\n\n" +
                        "Для взаимодействия вы можете использвать следующие команды:\n" +
                        "/start - перейти к стартовому сообщению\n" +
                        "/shelter_info - узнать информацию о приюте\n" +
                        "/take_pet - взять животное из приюта\n" +
                        "/volunteer - позвать волонтера для общения";


    private final String START_URL = "/start";
    private final String INFO_URL = "/shelter_info";


    @Override
    public BaseRequest parse(Long chatId, String userMessage) {
        switch (userMessage) {
            case (START_URL):
                return sendMessage(chatId, START_MESSAGE);
            case (INFO_URL):
                return sendMessage(chatId, "Какая информация интересует?", InfoKeyboard.infoKeyBoard());
            default:
                return sendMessage(chatId, "Непредвиденная ошибка");
        }
    }

    private SendMessage sendMessage(Long chatId, String message) {

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }

    private SendMessage sendMessage(Long chatId, String message, Keyboard keyboard) {

        return sendMessage(chatId, message)
                .replyMarkup(keyboard);
    }
}
