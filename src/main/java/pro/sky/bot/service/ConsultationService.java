package pro.sky.bot.service;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.bot.model.Pets;

import java.io.IOException;

public interface ConsultationService {

    SendMessage getVolunteerContact(Long chatId);
}
