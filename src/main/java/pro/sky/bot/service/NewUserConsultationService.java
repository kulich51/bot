package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;

import java.io.IOException;

public interface NewUserConsultationService {

    BaseRequest parse(Long chatId, String url) throws IOException;
}
