package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import pro.sky.bot.enums.Pets;

public class AdopterConsultationKeyboard {

    public static final String RULES_OF_ACQUAINTANCE = "Правила знакомства с животным";
    public static final String LIST_OF_DOCUMENTS = "Необходимый перечень документов";
    public static final String TRANSPORT_RECOMMENDATIONS = "Рекомендации по транспортировке";
    public static final String HOME_IMPROVEMENT_RECOMMENDATIONS = "Рекомендации по обустройству дома";
    public static final String REASONS_OF_REFUSAL = "Причины отказа в заборе животного";
    public static final String ADD_CONTACT_BUTTON = "Записать контакт";
    public static final String EXIT_BUTTON = "В главное меню ";

    private AdopterConsultationKeyboard() {
    }

    public static Keyboard adopterConsultationKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(RULES_OF_ACQUAINTANCE)
        )
                .addRow(new KeyboardButton(LIST_OF_DOCUMENTS))
                .addRow(new KeyboardButton(TRANSPORT_RECOMMENDATIONS))
                .addRow(new KeyboardButton(HOME_IMPROVEMENT_RECOMMENDATIONS))
                .addRow(new KeyboardButton(REASONS_OF_REFUSAL))
                .addRow(new KeyboardButton(ADD_CONTACT_BUTTON).requestContact(true))
                .addRow(new KeyboardButton(EXIT_BUTTON));

        return keyboard;
    }
}
