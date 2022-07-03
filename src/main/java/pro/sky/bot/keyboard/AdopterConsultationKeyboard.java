package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class AdopterConsultationKeyboard {

    public static final String RULES_OF_ACQUAINTANCE = "Правила знакомства с животным";
    public static final String LIST_OF_DOCUMENTS = "Необходимый перечень документов";
    public static final String TRANSPORT_RECOMMENDATIONS = "Рекомендации по транспортировке";

    private AdopterConsultationKeyboard() {
    }

    public static Keyboard adopterConsultationKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(RULES_OF_ACQUAINTANCE)
        )
                .addRow(new KeyboardButton(LIST_OF_DOCUMENTS))
                .addRow(new KeyboardButton(TRANSPORT_RECOMMENDATIONS));
        return keyboard;
    }
}
