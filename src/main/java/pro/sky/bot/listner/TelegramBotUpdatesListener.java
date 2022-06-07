package pro.sky.bot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.repository.VolunteerRepository;
import pro.sky.bot.service.NewUserConsultationService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NewUserConsultationService newUserConsultationService;
    private final VolunteerRepository volunteerRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NewUserConsultationService newUserConsultationService, VolunteerRepository volunteerRepository) {
        this.telegramBot = telegramBot;
        this.newUserConsultationService = newUserConsultationService;
        this.volunteerRepository = volunteerRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Проверка поделился ли пользователем контактом
            if (addContact(update)) {
                return;
            }

            String userMessage = update.message().text();
//            String userMessage = "update.message().text()";
            Long chatId = update.message().chat().id();
            switch (userMessage) {
                case ("/start"):
                case ("/shelter_info"):
                case (InfoKeyboard.ABOUT_BUTTON):
                case (InfoKeyboard.SCHEDULE_BUTTON):
                case (InfoKeyboard.RULES_BUTTON):
                case (InfoKeyboard.ADD_CONTACT_BUTTON):
                case (InfoKeyboard.QUESTION_BUTTON):
                    BaseRequest request = null;
                    try {
                        request = newUserConsultationService.parse(chatId, userMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    telegramBot.execute(request);
                    break;
                default:
                    telegramBot.execute(sendTextMessage(update.message(), "Sorry. Try again"));
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private boolean addContact(Update update) {
        Contact contact = update.message().contact();
        if (contact != null) {
            telegramBot.execute(sendTextMessage(update.message(), "Ваш контакт добавлен"));
            return true;
        }
        return false;
    }


    private SendMessage sendTextMessage(Message userMessage, String message) {

        Long chatId = userMessage.chat().id();

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }
}
