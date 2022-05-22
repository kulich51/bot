package pro.sky.bot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text().equals("/start")) {
                String startMessage = "Мы приют домашних живтоных. Здесь вы можете взять животное из нашего приюта.\n\n" +
                        "Для взаимодействия вы можете использвать следующие команды:\n" +
                        "/start - перейти к стартовому сообщению\n" +
                        "/shelter_info - узнать информацию о приюте\n" +
                        "/take_pet - взять животное из приюта\n" +
                        "/volunteer - позвать волонтера для общения";

                telegramBot.execute(sendTextMessage(update.message(), startMessage));
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendMessage sendTextMessage(Message userMessage, String message) {

        Long chatId = userMessage.chat().id();

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }
}
