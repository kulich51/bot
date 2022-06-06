package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;

import java.io.IOException;

public interface ConsultationService {

    BaseRequest parse(Long chatId, String url) throws IOException;
}
