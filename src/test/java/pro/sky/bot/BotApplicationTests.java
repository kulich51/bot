package pro.sky.bot;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.bot.model.Pets;
import pro.sky.bot.service.impl.NewUserConsultationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BotApplicationTests {

    @Autowired
    private NewUserConsultationServiceImpl consultationService;

    private final String TEXT_PARAMETER = "text";
    private final String PHOTO_PARAMETER = "photo";
    private final Long DEFAULT_CHAT_ID = 1L;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetVolunteerContact() {

        SendMessage message = consultationService.getVolunteerContact(DEFAULT_CHAT_ID);
        String reply = getTextFromSendMessage(message);
        System.out.println(reply);
        assertTrue(reply.length() > 0);
    }

    @Test
    void testGetVolunteerContactContainsUsername() {

        SendMessage message = consultationService.getVolunteerContact(DEFAULT_CHAT_ID);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.contains("@"));
    }

    private String getTextFromSendMessage(SendMessage message) {

        return (String) message
                .getParameters()
                .get(TEXT_PARAMETER);
    }

    @ParameterizedTest
    @EnumSource(Pets.class)
    void testGetAboutMessageLength(Pets pet) {

        SendMessage message = consultationService.getAboutMessage(DEFAULT_CHAT_ID, pet);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.length() > 0);
    }

    @Test
    void testGetAboutMessageCatShelter() {

        SendMessage message = consultationService.getAboutMessage(DEFAULT_CHAT_ID, Pets.CAT);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.contains("кошек"));
        assertFalse(reply.contains("собак"));
    }

    @Test
    void testGetAboutMessageDogShelter() {

        SendMessage message = consultationService.getAboutMessage(DEFAULT_CHAT_ID, Pets.DOG);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.contains("собак"));
        assertFalse(reply.contains("кошек"));
    }

    @ParameterizedTest
    @EnumSource(Pets.class)
    void testGetShelterScheduleMessageLength(Pets pet) {

        SendMessage message = consultationService.getShelterScheduleMessage(DEFAULT_CHAT_ID, pet);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.length() > 0);
    }

    @ParameterizedTest
    @EnumSource(Pets.class)
    void testGetMapByCoordinatesSize(Pets pet) {

        byte[] reply = (byte[]) consultationService
                .getMapByCoordinates(DEFAULT_CHAT_ID, pet)
                .getParameters()
                .get(PHOTO_PARAMETER);
        assertTrue(reply.length > 0);
    }

    @ParameterizedTest
    @EnumSource(Pets.class)
    void testGetSecurityPhoneMessageLength(Pets pet) {

        SendMessage message = consultationService.getSecurityPhoneMessage(DEFAULT_CHAT_ID, pet);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.length() > 0);
    }

    @Test
    void testGetRulesMessageLength() {

        SendMessage message = consultationService.getRulesMessage(DEFAULT_CHAT_ID);
        String reply = getTextFromSendMessage(message);
        assertTrue(reply.length() > 0);
    }
}
