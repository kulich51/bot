package pro.sky.bot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.keyboard.ReportKeyboard;
import pro.sky.bot.keyboard.StartMenuKeyboard;
import pro.sky.bot.model.DatabaseContact;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.model.Report;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.repository.ReportsRepository;
import pro.sky.bot.service.impl.GreetingServiceImpl;
import pro.sky.bot.service.impl.NewUserConsultationServiceImpl;
import pro.sky.bot.service.impl.PotentialHostConsultationServiceImpl;
import pro.sky.bot.service.impl.ReportServiceImpl;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NewUserConsultationServiceImpl newUserConsultationService;
    private final ReportServiceImpl reportService;
    private final PotentialHostConsultationServiceImpl potentialHostConsultationService;
    private final GreetingServiceImpl greetingService;
    private final ContactRepository contactRepository;
    private final PetRepository petRepository;
    private final ReportsRepository reportsRepository;

    private Pets selectedPet;

    public TelegramBotUpdatesListener(
            TelegramBot telegramBot,
            NewUserConsultationServiceImpl newUserConsultationService,
            ReportServiceImpl reportService, PotentialHostConsultationServiceImpl potentialHostConsultationService,
            GreetingServiceImpl greetingService, ContactRepository contactRepository, PetRepository petRepository, ReportsRepository reportsRepository) {
        this.telegramBot = telegramBot;
        this.newUserConsultationService = newUserConsultationService;
        this.reportService = reportService;
        this.potentialHostConsultationService = potentialHostConsultationService;
        this.greetingService = greetingService;
        this.contactRepository = contactRepository;
        this.petRepository = petRepository;
        this.reportsRepository = reportsRepository;
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

            if (getAdopterReport(update)) {
                return;
            }

            processUpdate(update);
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private boolean getAdopterReport(Update update) {

        Long chatId = update.message().chat().id();
        if (update.message().photo() != null && update.message().caption() !=null) {

            GetFile request = new GetFile(update.message().photo()[2].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(request);
            File file = getFileResponse.file();

            Report report = new Report();
            report.setId(0L);
            report.setFileId(telegramBot.getFullFilePath(file));
            report.setTextReport(update.message().caption());
            report.setPet(petRepository.getByName("sad"));

            Instant instant = Instant.ofEpochSecond(update.message().date());
            java.util.Date date = Date.from(instant);

            report.setDate(date);
            report.setUserId(update.message().from().id());
            reportsRepository.save(report);

            return true;
        }
        return false;
    }

    private byte[] downloadImage(String url) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return result.getBody();
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
                telegramBot.execute(newUserConsultationService.getKeyboard(chatId));
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
            case (InfoKeyboard.CALL_VOLUNTEER_BUTTON):
                telegramBot.execute(newUserConsultationService.getVolunteerContact(chatId));
                break;
            case (StartMenuKeyboard.SEND_REPORT_BUTTON):
                telegramBot.execute(reportService.getKeyboard(chatId));
                break;
            case (ReportKeyboard.REPORT_FORM_BUTTON):
                telegramBot.execute(reportService.getReportForm(chatId));
                break;
            default:
                telegramBot.execute(sendTextMessage(chatId, "Sorry. Try again"));
        }
    }

    private SendMessage sendTextMessage(Long chatId, String message) {

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }
}
