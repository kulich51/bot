package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.PotentialHostConsultationKeyboard;
import pro.sky.bot.service.ConsultationService;

@Service
public class PotentialHostConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final String TAKE_PET_URL = "/take_pet";
    private final String START_MESSAGE = "До того, как взять питомца из приюта, нужно ознакомиться с информацией ниже";


    @Override
    public SendMessage parse(Long chatId, String userMessage) {

        switch (userMessage) {
            case(TAKE_PET_URL):
                return sendMessage(
                        chatId,
                        START_MESSAGE,
                        PotentialHostConsultationKeyboard.potentialHostConsultationKeyboard()
                );
            default:
                sendDefaultMessage(chatId);
        }

        return null;
    }


}
