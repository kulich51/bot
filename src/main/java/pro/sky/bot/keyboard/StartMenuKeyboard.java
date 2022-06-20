package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class StartMenuKeyboard {

    public static final String ABOUT_BUTTON = "Узнать информацию о приюте";
    public static final String TAKE_PET_BUTTON = "Как взять животное из приюта";
    public static final String SEND_REPORT_BUTTON = "Прислать отчет о питомце";
    public static final String CALL_VOLUNTEER_BUTTON = "Позвать волонтёра";

    private StartMenuKeyboard() {
    }

    public static Keyboard startMenuKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(ABOUT_BUTTON)
        )
                .addRow(new KeyboardButton(TAKE_PET_BUTTON))
                .addRow(new KeyboardButton(SEND_REPORT_BUTTON))
                .addRow(new KeyboardButton(CALL_VOLUNTEER_BUTTON))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);
        return keyboard;
    }
}