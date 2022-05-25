package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;

public interface NewUserConsultationService {

    BaseRequest parse(Long chatId, String url);
}
