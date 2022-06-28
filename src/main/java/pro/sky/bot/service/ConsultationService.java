package pro.sky.bot.service;

import com.pengrad.telegrambot.request.SendMessage;

public interface ConsultationService {

    SendMessage getVolunteerContact(Long chatId);
}
