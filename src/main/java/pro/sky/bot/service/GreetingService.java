package pro.sky.bot.service;

import com.pengrad.telegrambot.request.SendMessage;

public interface GreetingService  {

    SendMessage greeting(Long chatId);
    SendMessage getStartMenu(Long chatId);
}