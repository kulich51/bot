package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class MessageSender {

    /**
     * Send text message to user
     * @param chatId chat identifier in telegram
     * @param message text message for user
     * @return telegram SendMessage object
     */
    protected SendMessage sendMessage(Long chatId, String message) {

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }

    /**
     * Send text message to user and show keyboard
     * @param chatId chat identifier in telegram
     * @param message text message for user
     * @param keyboard keyboard object to show
     * @return telegram SendMessage object
     */
    protected SendMessage sendMessage(Long chatId, String message, Keyboard keyboard) {

        return sendMessage(chatId, message)
                .replyMarkup(keyboard);
    }

    protected SendMessage sendDefaultMessage(Long chatId) {

        String message = "Sorry, try again!";
        return sendMessage(chatId, message);
    }
}
