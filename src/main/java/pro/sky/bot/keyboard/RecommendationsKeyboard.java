package pro.sky.bot.keyboard;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class RecommendationsKeyboard {

    public static final String TRANSPORTATION_BUTTON = "Рекомендации по транспортировке";
    public static final String PUPPY_HOME_IMPROVEMENT_BUTTON = "Рекомендации обустройству дома для щенка";
    public static final String DOG_HOME_IMPROVEMENT_BUTTON = "Рекомендации обустройству дома для собаки";

    private RecommendationsKeyboard() {
    }

    public static Keyboard keyboard() {

        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(TRANSPORTATION_BUTTON)
        )
                .addRow(new KeyboardButton(PUPPY_HOME_IMPROVEMENT_BUTTON))
                .addRow(new KeyboardButton(DOG_HOME_IMPROVEMENT_BUTTON));
        return keyboard;
    }
}
