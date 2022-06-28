package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.InfoKeyboard;
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

    @Override
    public SendMessage greeting(Long chatId) {
        return sendMessage(chatId, greeting);
    }

    @Override
    public SendMessage getStartMenu(Long chatId) {
        return sendMessage(chatId, startMenuMessage, StartMenuKeyboard.startMenuKeyboard());
    }
}