package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.keyboard.AdopterConsultationKeyboard;
import pro.sky.bot.model.Volunteer;
import pro.sky.bot.repository.VolunteerRepository;
import pro.sky.bot.service.ConsultationService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Service
public class AdopterConsultationServiceImpl extends MessageSender implements ConsultationService {

    private final VolunteerRepository volunteerRepository;

    private final String GREETING = "Здесь вы можете разобраться с бюрократическими (оформление договора)" +
            " и бытовыми (как подготовиться к жизни с животным) вопросам";

    public AdopterConsultationServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Get start keyboard for adopter consultation
     * @param chatId chat identifier
     * @return SenMessage object with keyboard
     */
    @Override
    public SendMessage getKeyboard(Long chatId) {

        Keyboard keyboard = AdopterConsultationKeyboard.adopterConsultationKeyboard();
        return sendMessage(chatId, GREETING, keyboard);
    }

    /**
     * Get volunteer contact to text him
     * @param chatId chat identifier
     * @return SenMessage object with volunteer contact in message
     */
    @Override
    public SendMessage getVolunteerContact(Long chatId) {

        List<Volunteer> volunteers = volunteerRepository.findAll();
        Volunteer random = volunteers.get(getRandomNumber(0, volunteers.size() - 1));
        String message = "Обратитесь за помощью к " + random.getUsername();
        return sendMessage(chatId, message);
    }

    public SendMessage getRulesOfAcquaintance(Long chatId, Pets pet) {

        String fileName = "rules of acquaintance.txt";
        String message = readFile(getPath(pet).concat(fileName));

        return sendMessage(chatId, message);

    }

    /**
     * Get random number from interval
     * @param min min number
     * @param max max number
     * @return random number
     */
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Get pet shelter
     * @param pet type of pet shelter from enum (cat or dog)
     * @return pet shelter
     */
    private String getPath(Pets pet) {
        if (pet.equals(Pets.DOG)) {
            return "src/main/resources/text/dog/";
        }
        return "src/main/resources/text/cat/";
    }

    private String readFile(String filePath) {

        StringBuilder sb = new StringBuilder();

        try {
            File text = new File(filePath);
            Scanner reader = new Scanner(text);
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return sb.toString();
    }
}
