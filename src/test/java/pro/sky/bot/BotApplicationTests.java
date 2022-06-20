//package pro.sky.bot;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import pro.sky.bot.keyboard.InfoKeyboard;
//import pro.sky.bot.service.impl.NewUserConsultationServiceImpl;
//
//import java.io.IOException;
//import java.util.stream.Stream;
//
//@SpringBootTest
//class BotApplicationTests {
//
//    @Autowired
//    private NewUserConsultationServiceImpl consultationService;
//
//    private final String TEXT_PARAMETER = "text";
//    private final String PHOTO_PARAMETER = "photo";
//    private final String DEFAULT_ANSWER = "Непредвиденная ошибка";
//    private final Long DEFAULT_CHAT_ID = 1L;
//
//    @Test
//    void contextLoads() {
//    }
//
//    /**
//     * Test reply on '/start' message from user
//     */
//    @ParameterizedTest
//    @MethodSource("userMessageProvider")
//    void testTextReplyLength(String userMessage) throws IOException {
//        String textReply = (String) getReply(userMessage, TEXT_PARAMETER);
//        Assertions.assertTrue(textReply.length() > 0);
//    }
//
//    /**
//     * Test that reply doesn't equals default answer
//     */
//    @ParameterizedTest
//    @MethodSource("userMessageProvider")
//    void testTextReplyData(String userMessage) throws IOException {
//        String textReply = (String) getReply(userMessage, TEXT_PARAMETER);
//        Assertions.assertNotEquals(DEFAULT_ANSWER, textReply);
//    }
//
//    @Test
//    void testScheduleButton() throws IOException {
//        byte[] data =
//                (byte[]) getReply(InfoKeyboard.SCHEDULE_BUTTON, PHOTO_PARAMETER);
//
//        Assertions.assertTrue(data.length > 0);
//    }
//
//    private Object getReply(String userMessage, String parameter) throws IOException {
//        return consultationService
//                .parse(DEFAULT_CHAT_ID, userMessage)
//                .getParameters()
//                .get(parameter);
//    }
//
//    /**
//     * Provide user message passed throw the bot
//     */
//    static Stream<String> userMessageProvider() {
//        return Stream.of(
//                "/start",
//                InfoKeyboard.ABOUT_BUTTON,
//                InfoKeyboard.RULES_BUTTON,
//                InfoKeyboard.QUESTION_BUTTON
//        );
//    }
//}
