package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.Scanner;

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

    protected SendMessage sendMessageFromTextFile(Long chatId, String fileName) {

        String path = textFilesDirectory.concat(fileName);
        StringBuilder sb = new StringBuilder();

        try {
            File text = new File(path);
            Scanner reader = new Scanner(text);
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return sendMessage(chatId, sb.toString());
    }
}
