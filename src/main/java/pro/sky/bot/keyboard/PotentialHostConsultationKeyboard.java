package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.security.Key;

public class PotentialHostConsultationKeyboard {

    public static final String RULES_OF_ACQUAINTANCE = "Правила знакомства с собакой";
    public static final String RECOMMENDATIONS = "Рекомендации";
    public static final String CYNOLOGIST = "Информация по кинологам";
    public static final String LIST_OF_DOCUMENTS = "Необходимый перечень документов";

    private PotentialHostConsultationKeyboard() {
    }

    public static Keyboard potentialHostConsultationKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(RULES_OF_ACQUAINTANCE)
        )
                .addRow(new KeyboardButton(RECOMMENDATIONS))
                .addRow(new KeyboardButton(CYNOLOGIST))
                .addRow(new KeyboardButton(LIST_OF_DOCUMENTS));
        return keyboard;
    }
}
