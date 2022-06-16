package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;
import pro.sky.bot.model.Pets;

import java.io.IOException;

public interface ConsultationService {

    BaseRequest parse(Long chatId, String url, Pets pet) throws IOException;

}
