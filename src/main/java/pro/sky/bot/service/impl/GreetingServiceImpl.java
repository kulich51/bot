package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.StartMenuKeyboard;
import pro.sky.bot.service.GreetingService;

@Service
public class GreetingServiceImpl extends MessageSender implements GreetingService {

    private static final String greeting = "Мы приют домашних живтоных. Здесь вы можете взять животное из нашего приюта.\n\n" +
            "Для взаимодействия вы можете использвать следующие команды:\n" +
            "/start - перейти к стартовому сообщению\n" +
            "/dogs - перейти в приют для собак\n" +
            "/cats - перейти в приют для кошек";

    private static final String startMenuMessage = "Что хотите сделать?";

    /**
     * Send greeting for users
     *
     * @param chatId chat id in bot
     * @return send greeting for users
     */

    @Override
    public SendMessage greeting(Long chatId) {
        return sendMessage(chatId, greeting);
    }

    /**
     * Send message about start menu
     *
     * @param chatId chat id in bot
     * @return send message about start menu
     */
    @Override
    public SendMessage getStartMenu(Long chatId) {
        return sendMessage(chatId, startMenuMessage, StartMenuKeyboard.startMenuKeyboard());
    }
}