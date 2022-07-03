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
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.AdopterConsultationKeyboard;
import pro.sky.bot.keyboard.InfoKeyboard;
import pro.sky.bot.keyboard.ReportKeyboard;
import pro.sky.bot.keyboard.StartMenuKeyboard;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.enums.Pets;
import pro.sky.bot.model.Pet;
import pro.sky.bot.model.Report;
import pro.sky.bot.repository.AdopterRepository;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.repository.ReportRepository;
import pro.sky.bot.service.impl.GreetingServiceImpl;
import pro.sky.bot.service.impl.NewUserConsultationServiceImpl;
import pro.sky.bot.service.impl.AdopterConsultationServiceImpl;
import pro.sky.bot.service.impl.ReportInfoServiceImpl;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NewUserConsultationServiceImpl newUserConsultationService;
    private final ReportInfoServiceImpl reportService;
    private final AdopterConsultationServiceImpl adopterConsultationService;
    private final GreetingServiceImpl greetingService;
    private final ContactRepository contactRepository;
    private final PetRepository petRepository;
    private final ReportRepository reportsRepository;
    private final AdopterRepository adopterRepository;

    private Pets selectedPet;

    public TelegramBotUpdatesListener(
            TelegramBot telegramBot,
            NewUserConsultationServiceImpl newUserConsultationService,
            ReportInfoServiceImpl reportService, AdopterConsultationServiceImpl potentialHostConsultationService,
            GreetingServiceImpl greetingService, ContactRepository contactRepository, PetRepository petRepository, ReportRepository reportsRepository, AdopterRepository adopterRepository) {
        this.telegramBot = telegramBot;
        this.newUserConsultationService = newUserConsultationService;
        this.reportService = reportService;
        this.adopterConsultationService = potentialHostConsultationService;
        this.greetingService = greetingService;
        this.contactRepository = contactRepository;
        this.petRepository = petRepository;
        this.reportsRepository = reportsRepository;
        this.adopterRepository = adopterRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Update process ( new message from user)
     *
     * @param updates new message
     * @return CONFIRMED_UPDATES_ALL
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            // Проверка поделился ли пользователем контактом
            if (addContact(update)) {
                return;
            }

            // Проверка напрявляет ли пользователь отчёт
            if (saveAdopterReport(update)) {
                return;
            }

            processUpdate(update);
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Save adopter report (photo with text) in repository
     *
     * @param update user's message
     * @return true if user sent photo, otherwise false
     */
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

            // Если живтное не найдено, то выходим из метода
            if (petIsNull) {
                return true;
            }

            boolean userIsNotAdopter = checkAdopter(message, pet);

            // Если усыновитель не найден, то выходим из метода
            if (userIsNotAdopter) {
                return true;
            }

            Long userId = message.from().id();
            File file = getPhotoFile(update);
            java.util.Date date = convertUnixDate(update);
            String textReport = getReport(update);

            Report oldReport = reportsRepository.findByUserIdAndPetAndDate(userId, pet, date);

            Report report = new Report(
                    0L,
                    userId,
                    pet,
                    telegramBot.getFullFilePath(file),
                    date,
                    textReport,
                    true
            );

            // Если отчет по питомцу есть в БД, то обновляем его на новый
            if (oldReport != null) {
                updateReport(oldReport, report);
                telegramBot.execute(sendTextMessage(chatId, "Отчет откорректирован. Спасибо!"));
            } else {
                reportsRepository.save(report);
                telegramBot.execute(sendTextMessage(chatId, "Отчет сохранён. Спасибо!"));
            }
            return true;
        }
        return false;
    }

    /**
     * Update reports and save it to the database
     *
     * @param oldReport in database
     * @param report    new report
     */
    private void updateReport(Report oldReport, Report report) {

        oldReport.setAccepted(false);
        oldReport.setFilePath(report.getFilePath());
        oldReport.setTextReport(report.getTextReport());
        reportsRepository.save(oldReport);
    }


    /**
     * Check caption of report by null
     *
     * @param message user message via bot
     * @param chatId  chat id in bot
     * @return true if caption is null, otherwise false
     */
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

    /**
     * Check what pet object not null
     *
     * @param pet     pet object
     * @param petName pet name to find in repository
     * @param chatId  chat id in bot
     * @return true if pet is null (not found), otherwise false
     */
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
     * Adopter verification
     *
     * @param message user message via bot
     * @param pet     pet object
     * @return true if not an adopter, otherwise false
     */
    private boolean checkAdopter(Message message, Pet pet) {

        Long userId = message.from().id();
        Adopter adopter = adopterRepository.getAdopterByUserIdAndPetId(userId, pet.getId());

        if (adopter == null) {
            telegramBot.execute(
                    sendTextMessage(
                            message.chat().id(),
                            "Вы не являетесь усыновителем животного '" + pet.getName() + "'"
                    )
            );
            return true;
        }
        return false;
    }

    /**
     * Get File object from bot with report photo
     *
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
     *
     * @param update user's message
     * @return date of a message
     */
    private java.util.Date convertUnixDate(Update update) {

        Instant instant = Instant.ofEpochSecond(update.message().date());
        return Date.from(instant);
    }

    /**
     * Get pet name from users' message (first row from message)
     *
     * @param update user's message
     * @return pet name
     */
    private String getPetName(Update update) {

        String[] caption = update
                .message()
                .caption()
                .split("\n");

        return caption[0];
    }

    /**
     * Get report from users' message (first row from message)
     *
     * @param update user's message
     * @return text report about pet
     */
    private String getReport(Update update) {

        String[] caption = update
                .message()
                .caption()
                .split("\n");

        String[] report = Arrays.copyOfRange(caption, 1, caption.length);
        return String.join("\n", report);
    }

    /**
     * Add contact in to the database
     *
     * @param update user's message
     * @return true if pet is added, otherwise false
     */
    private boolean addContact(Update update) {

        Contact tgContact = update.message().contact();

        if (tgContact != null) {
            contactRepository.save(createDatabaseContact(tgContact, update.message().chat().id()));
            telegramBot.execute(sendTextMessage(update.message().chat().id(), "Ваш контакт добавлен"));
            return true;
        }
        return false;
    }

    /**
     * Create contacts for database
     *
     * @param contact contacts of a potential adopter ( pass via telegram )
     * @param chatId  chat id in bot
     * @return new entity for database
     */
    private pro.sky.bot.model.Contact createDatabaseContact(Contact contact, Long chatId) {
        return new pro.sky.bot.model.Contact(
                contact.userId(),
                contact.phoneNumber(),
                contact.firstName(),
                contact.lastName(),
                chatId
        );
    }

    /**
     * Update process ( new message from user)
     *
     * @param update new message
     */
    private void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String userMessage = update.message().text();

        switch (userMessage) {
            case ("/start"):
                telegramBot.execute(greetingService.greeting(chatId));
                break;
            case ("/dogs"):
                selectedPet = Pets.DOG;
                telegramBot.execute(greetingService.getStartMenu(chatId));
                break;
            case ("/cats"):
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
            case (InfoKeyboard.EXIT_BUTTON):
                telegramBot.execute(greetingService.getStartMenu(chatId));
                break;
            case (StartMenuKeyboard.SEND_REPORT_BUTTON):
                telegramBot.execute(reportService.getKeyboard(chatId));
                break;
            case (ReportKeyboard.REPORT_FORM_BUTTON):
                telegramBot.execute(reportService.getReportForm(chatId));
                break;
            case (StartMenuKeyboard.TAKE_PET_BUTTON):
                telegramBot.execute(adopterConsultationService.getKeyboard(chatId));
                break;
            case (AdopterConsultationKeyboard.RULES_OF_ACQUAINTANCE):
                telegramBot.execute(adopterConsultationService.getRulesOfAcquaintance(chatId, selectedPet));
                break;
            default:
                telegramBot.execute(sendTextMessage(chatId, "Sorry. Try again"));
        }
    }

    /**
     * Send message in bot
     *
     * @param message user message via bot
     * @param chatId  chat id in bot
     * @return new message
     */
    private SendMessage sendTextMessage(Long chatId, String message) {

        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }
}
