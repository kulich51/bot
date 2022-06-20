package pro.sky.bot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.keyboard.PotentialHostConsultationKeyboard;
import pro.sky.bot.keyboard.StartMenuKeyboard;
import pro.sky.bot.model.DatabaseContact;
import pro.sky.bot.model.Pets;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.service.ConsultationService;
import pro.sky.bot.service.impl.GreetingServiceImpl;
import pro.sky.bot.service.impl.NewUserConsultationServiceImpl;
import pro.sky.bot.service.impl.PotentialHostConsultationServiceImpl;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NewUserConsultationServiceImpl newUserConsultationService;
    private final PotentialHostConsultationServiceImpl potentialHostConsultationService;
    private final GreetingServiceImpl greetingService;
    private final ContactRepository contactRepository;

    private Pets selectedPet;

    public TelegramBotUpdatesListener(
            TelegramBot telegramBot,
            NewUserConsultationServiceImpl newUserConsultationService,
            PotentialHostConsultationServiceImpl potentialHostConsultationService,
            GreetingServiceImpl greetingService, ContactRepository contactRepository) {
        this.telegramBot = telegramBot;
        this.newUserConsultationService = newUserConsultationService;
        this.potentialHostConsultationService = potentialHostConsultationService;
        this.greetingService = greetingService;
        this.contactRepository = contactRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Проверка поделился ли пользователем контактом
            if (addContact(update)) {
                return;
            }
            processUpdate(update);
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private boolean addContact(Update update) {

        Contact contact = update.message().contact();

        if (contact != null) {
            contactRepository.save(createDatabaseContact(contact));
            telegramBot.execute(sendTextMessage(update.message().chat().id(), "Ваш контакт добавлен"));
            return true;
        }
        return false;
    }

    private DatabaseContact createDatabaseContact(Contact contact) {
        return new DatabaseContact(
                contact.userId(),
                contact.phoneNumber(),
                contact.firstName(),
                contact.lastName()
        );
    }

    private void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String userMessage = update.message().text();
        switch (userMessage) {
            case ("/start"):
                telegramBot.execute(greetingService.greeting(chatId));
                break;
            case("/dogs"):
                selectedPet = Pets.DOG;
                telegramBot.execute(greetingService.getStartMenu(chatId));
                break;
            case("/cats"):
                selectedPet = Pets.CAT;
                telegramBot.execute(greetingService.getStartMenu(chatId));
                break;
            case (StartMenuKeyboard.ABOUT_BUTTON):
                telegramBot.execute(greetingService.getInfoKeyboard(chatId));
                break;
            case (InfoKeyboard.ABOUT_BUTTON):
                telegramBot.execute(newUserConsultationService.getAboutMessage(chatId, selectedPet));
                break;
            case (InfoKeyboard.SCHEDULE_BUTTON):
                telegramBot.execute(newUserConsultationService.getShelterScheduleMessage(chatId, selectedPet));
                telegramBot.execute(newUserConsultationService.getMapByCoordinates(chatId, selectedPet));
                break;
            case (InfoKeyboard.PERMIT_BUTTON):
                telegramBot.execute(newUserConsultationService.getSecurityPhoneMessage(chatId, selectedPet));
                break;
            case (InfoKeyboard.RULES_BUTTON):
                telegramBot.execute(newUserConsultationService.getRulesMessage(chatId));
                break;
            case (InfoKeyboard.ADD_CONTACT_BUTTON):
                break;
            default:
                telegramBot.execute(sendTextMessage(chatId, "Sorry. Try again"));
        }
    }

    private void parseUserMessage(Long chatId, String userMessage, ConsultationService service, Pets pet) {
        BaseRequest request = null;
        try {
            request = service.parse(chatId, userMessage, pet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        telegramBot.execute(request);
    }

    private SendMessage sendTextMessage(Long chatId, String message) {

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }
}
