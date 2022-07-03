package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class ReportKeyboard {

    public static final String REPORT_FORM_BUTTON = "Узнать форму отчёта";
    public static final String CALL_VOLUNTEER_BUTTON = "Позвать волонтёра";
    public static final String EXIT_BUTTON = "В главное меню";

    private ReportKeyboard() {
    }

    public static Keyboard reportKeyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(REPORT_FORM_BUTTON)
        )
                .addRow(new KeyboardButton(CALL_VOLUNTEER_BUTTON))
                .addRow(new KeyboardButton(EXIT_BUTTON))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true);
        return keyboard;
    }
}
