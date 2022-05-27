package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class InfoKeyboard {

    public static final String ABOUT_BUTTON = "О приюте";
    public static final String SCHEDULE_BUTTON = "Часы работы";
    public static final String RULES_BUTTON = "Правила нахождения на территории";
    public static final String ADD_CONTACT_BUTTON = "Записать контакт";
    public static final String CALL_VOLUNTEER_BUTTON = "Позвать волонтёра";

    private InfoKeyboard() {
    }

    public static Keyboard infoKeyBoard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(ABOUT_BUTTON),
                new KeyboardButton(SCHEDULE_BUTTON)
        )
                .addRow(new KeyboardButton(RULES_BUTTON))
                .addRow(new KeyboardButton(ADD_CONTACT_BUTTON),
                        new KeyboardButton(CALL_VOLUNTEER_BUTTON))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);


        return keyboard;
    }

}
