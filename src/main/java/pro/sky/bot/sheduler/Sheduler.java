package pro.sky.bot.sheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.model.Contact;
import pro.sky.bot.model.Report;
import pro.sky.bot.repository.AdopterRepository;
import pro.sky.bot.repository.ContactRepository;
import pro.sky.bot.repository.ReportRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Sheduler {

    private final TelegramBot telegramBot;
    private final AdopterRepository adopterRepository;
    private final ReportRepository reportRepository;
    private final ContactRepository contactRepository;

    private final String ONE_DAY_DELAY_MESSAGE = "Добрый день!\nВчера вы не отправили отчет о питомце.\n" +
            "Напоминаем, что это нужно делать каждый день!";

    private final String CONGRATULATIONS = "Поздравляем. Ваш испытательный срок закончился!";
    private final String PROBATION_NOT_PASSED = "Испытательный срок закончился!\n" +
            "К сожалению, вы его не прошли.\n" +
            "Обратитеться в волонтерам за дальнейшими инструкциями\n";


    public Sheduler(TelegramBot telegramBot, AdopterRepository adopterRepository, ReportRepository reportRepository, ContactRepository contactRepository) {
        this.telegramBot = telegramBot;
        this.adopterRepository = adopterRepository;
        this.reportRepository = reportRepository;
        this.contactRepository = contactRepository;
    }

    /**
     * Remind send report, if adopter didn't send report yesterday
     */
    @Scheduled(cron = "${cron.expression}")
    public void remindAboutOneDayDelay() {

        Collection<Adopter> adopters = adopterRepository.getAdopterOnProbation();
        Set<Long> adopterId = getAdopterId(adopters);
        adopterId.removeAll(getAdopterIdFromReport());
        Collection<Contact> contacts = contactRepository.findAllByUserIdIn(adopterId);
        sendMessageToContacts(contacts, ONE_DAY_DELAY_MESSAGE);
    }


    /**
     * Send congratulations, if probation period was ended
     */
    @Scheduled(cron = "${cron.expression}")
    public void Congratulate() {

        Collection<Adopter> adopters = adopterRepository.getAdopterWithEndProbation(getYesterdayDate(), true);
        Set<Long> adopterId = getAdopterId(adopters);
        Collection<Contact> contacts = contactRepository.findAllByUserIdIn(adopterId);
        sendMessageToContacts(contacts, CONGRATULATIONS);
    }

    /**
     * Send information, if probation did not pass
     */
    @Scheduled(cron = "${cron.expression}")
    public void probationNotPassed() {

        Collection<Adopter> adopters = adopterRepository.getAdopterWithEndProbation(getYesterdayDate(), false);
        Set<Long> adopterId = getAdopterId(adopters);
        Collection<Contact> contacts = contactRepository.findAllByUserIdIn(adopterId);
        sendMessageToContacts(contacts, PROBATION_NOT_PASSED);
    }

    /**
     * Send standard message to all contacts in the collection
     * @param contacts collection of Contact objects
     * @param message string message to the user
     */
    private void sendMessageToContacts(Collection<Contact> contacts, String message) {
        for (Contact contact : contacts) {
            telegramBot.execute(new SendMessage(contact.getChatId(), message));
        }
    }

    /**
     * Get only adopter id from adopters
     * @param adopters collection of adopters from db
     * @return set of adopters id
     */
    private Set<Long> getAdopterId(Collection<Adopter> adopters) {

        return adopters
                .stream()
                .map(Adopter :: getUserId)
                .collect(Collectors.toSet());
    }

    /**
     * Get all yesterday reports and extract only adopterd id
     * @return set of adopters id
     */
    private Set<Long> getAdopterIdFromReport() {

        Collection<Report> reports = reportRepository.getReportsByDate(getYesterdayDate());
        return reports
                .stream()
                .map(Report :: getUserId)
                .collect(Collectors.toSet());
    }

    /**
     * Get yesterday relatively today date
     * @return yesterday date
     */
    private Date getYesterdayDate() {

        Instant instant = Instant.now();
        return Date.from(instant.minus(1, ChronoUnit.DAYS));
    }
}
