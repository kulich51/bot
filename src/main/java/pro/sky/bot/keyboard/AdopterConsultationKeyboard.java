package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class AdopterConsultationKeyboard {

    public static final String RULES_OF_ACQUAINTANCE = "Правила знакомства с животным";
    public static final String RECOMMENDATIONS = "Рекомендации";
    public static final String CYNOLOGIST = "Информация по кинологам";
    public static final String LIST_OF_DOCUMENTS = "Необходимый перечень документов";

    private AdopterConsultationKeyboard() {
    }

    public static Keyboard adopterConsultationKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(RULES_OF_ACQUAINTANCE)
        )
                .addRow(new KeyboardButton(RECOMMENDATIONS))
                .addRow(new KeyboardButton(CYNOLOGIST))
                .addRow(new KeyboardButton(LIST_OF_DOCUMENTS));
        return keyboard;
    }
}
