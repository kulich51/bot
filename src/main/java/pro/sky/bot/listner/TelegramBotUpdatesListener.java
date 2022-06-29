package pro.sky.bot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
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
import pro.sky.bot.model.Pet;
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

            if (saveAdopterReport(update)) {
                return;
            }

            processUpdate(update);
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private Boolean saveAdopterReport(Update update) {

        Message message = update.message();
        Long chatId = message.chat().id();

        if (message.photo() != null) {

            boolean captionIsNull = checkReportCaption(message, chatId);

            // Если к отчету приложено только фото, то выходим из метода
            if (captionIsNull) {
                return true;
            }

            String petName = getPetName(update);
            Pet pet = petRepository.getByName(petName);
            boolean petIsNull = checkPet(pet, petName, chatId);

            // Если животное найдено в БД, то сохраняем присланный отчет в БД
            if (!petIsNull) {
                Long userId = message.from().id();
                File file = getPhotoFile(update);
                java.util.Date date = convertUnixDate(update);
                String textReport = getReport(update);

                Report report = new Report(
                        0L,
                        userId,
                        pet,
                        telegramBot.getFullFilePath(file),
                        date,
                        textReport,
                        false
                );
                reportsRepository.save(report);
            }
            return true;
        }
        return false;
    }

    private boolean checkReportCaption(Message message, Long chatId) {

        if (message.caption() == null) {
            telegramBot.execute(
                    sendTextMessage(
                    chatId,
                    "Пришлите отчёт заново. К фотографиии необходимо приложить отчёт согласно форме"
                    )
            );
            return true;
        }
        return false;
    }

    private boolean checkPet(Pet pet, String petName, Long chatId) {

        if (pet == null) {
            telegramBot.execute(
                    sendTextMessage(
                    chatId,
                    "Животное с кличкой '" + petName + "' не найдено. Повторите снова"
                    )
            );
            return true;
        }
        return false;
    }

    /**
     * Get File object from bot with report photo
     * @param update user's message
     * @return File object
     */
    private File getPhotoFile(Update update) {

        GetFile request = new GetFile(update.message().photo()[2].fileId());
        GetFileResponse getFileResponse = telegramBot.execute(request);
        return getFileResponse.file();
    }

    /**
     * Get date of a message from unix timestamp
     * @param update user's message
     * @return date of a message
     */
    private java.util.Date convertUnixDate(Update update) {

        Instant instant = Instant.ofEpochSecond(update.message().date());
        return Date.from(instant);
    }

    /**
     * Get pet name from users' message (first row from message)
     * @param update user's message
     * @return pet name
     */
    private String getPetName(Update update) {

        String caption = update
                .message()
                .caption();

        int petNameLastIndex = caption.indexOf("\n");
        return caption.substring(0, petNameLastIndex);
    }

    /**
     * Get report from users' message (first row from message)
     * @param update user's message
     * @return text report about pet
     */
    private String getReport(Update update) {

        String caption = update
                .message()
                .caption();

        int petNameLastIndex = caption.indexOf("\n");
        return caption.substring(petNameLastIndex + 1);
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
