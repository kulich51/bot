package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MessageSender {

    @Value("${text.files.path}")
    private String textFilesDirectory;

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

    protected SendMessage sendMessageFromTextFile(Long chatId, String fileName) throws IOException {

        String path = textFilesDirectory.concat(fileName);
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sendMessage(chatId, sb.toString());
        } finally {
            br.close();
        }
    }
}
