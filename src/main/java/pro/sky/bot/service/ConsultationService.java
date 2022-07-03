package pro.sky.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.bot.enums.Pets;

public interface ConsultationService {

    SendMessage getVolunteerContact(Long chatId);
    SendMessage getKeyboard(Long chatId); // Pets используется только в сервисе AdopterConsultationServiceImpl
}
