package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import pro.sky.bot.enums.Pets;

public class DogRecommendationsKeyboard {

    public static final String YOUNG_RECOMMENDATION = "Обустройство дома для щенка";
    public static final String ADULT_RECOMMENDATION = "Обустройство дома для собаки";
    public static final String DISABILITY_RECOMMENDATION = "Обустройство дома для собаки с ограниченными возможностями";
    public static final String EXIT = "Обратно к информации для собак";

    private DogRecommendationsKeyboard() {
    }

    public static Keyboard keyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(YOUNG_RECOMMENDATION)
        )
                .addRow(new KeyboardButton(ADULT_RECOMMENDATION))
                .addRow(new KeyboardButton(DISABILITY_RECOMMENDATION))
                .addRow(new KeyboardButton(EXIT));
        return keyboard;
    }
}
