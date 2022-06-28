package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.PotentialHostConsultationKeyboard;
import pro.sky.bot.keyboard.RecommendationsKeyboard;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.service.ConsultationService;

import java.io.IOException;

@Service
public class PotentialHostConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final String TAKE_PET_URL = "/take_pet";
    private final String START_MESSAGE = "До того, как взять питомца из приюта, нужно ознакомиться с информацией ниже";
    public static final String RULES_OF_ACQUAINTANCE_FILE = "rules of acquaintance.txt";
    public static final String LIST_OF_DOCUMENTS_FILE = "list of documents.txt";

    public SendMessage parse(Long chatId, String userMessage, Pets pet) throws IOException {

        switch (userMessage) {
            case(TAKE_PET_URL):
                return sendMessage(
                        chatId,
                        START_MESSAGE,
                        PotentialHostConsultationKeyboard.potentialHostConsultationKeyboard()
                );
            case (PotentialHostConsultationKeyboard.RULES_OF_ACQUAINTANCE):
                return sendMessageFromTextFile(chatId, RULES_OF_ACQUAINTANCE_FILE);
            case (PotentialHostConsultationKeyboard.LIST_OF_DOCUMENTS):
                return sendMessageFromTextFile(chatId, LIST_OF_DOCUMENTS_FILE);
            case (PotentialHostConsultationKeyboard.RECOMMENDATIONS):
                return sendMessage(chatId,
                        "Ознакомьтесь с рекомендациями ниже",
                        RecommendationsKeyboard.keyboard());
            default:
                sendDefaultMessage(chatId);
        }

        return null;
    }

    @Override
    public SendMessage getKeyboard(Long chatId) {
        return null;
    }

    @Override
    public SendMessage getVolunteerContact(Long chatId) {
        return null;
    }
}
