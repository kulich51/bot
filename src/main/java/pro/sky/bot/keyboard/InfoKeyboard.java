package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class InfoKeyboard {

    public static final String ABOUT_BUTTON = "О приюте";
    public static final String SCHEDULE_BUTTON = "Расписание работы приюта";
    public static final String PERMIT_BUTTON = "Оформить пропуск на машину";
    public static final String RULES_BUTTON = "Техника безопасности на территории приюта";
    public static final String ADD_CONTACT_BUTTON = "Записать контакт";
    public static final String CALL_VOLUNTEER_BUTTON = "Позвать волонтёра";


    private InfoKeyboard() {
    }

    public static Keyboard infoKeyBoard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(ABOUT_BUTTON)
        )
                .addRow(new KeyboardButton(SCHEDULE_BUTTON))
                .addRow(new KeyboardButton(PERMIT_BUTTON))
                .addRow(new KeyboardButton(RULES_BUTTON))
                .addRow(new KeyboardButton(ADD_CONTACT_BUTTON).requestContact(true))
                .addRow(new KeyboardButton(CALL_VOLUNTEER_BUTTON))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);

        return keyboard;
    }

}
