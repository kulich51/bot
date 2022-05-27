package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public interface NewUserConsultationService {

    SendMessage parse(Long chatId, String url);
}
