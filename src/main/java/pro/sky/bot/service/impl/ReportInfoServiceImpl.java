package pro.sky.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.bot.keyboard.ReportKeyboard;
import pro.sky.bot.model.Volunteer;
import pro.sky.bot.repository.VolunteerRepository;
import pro.sky.bot.service.ConsultationService;

import java.util.List;

@Service
public class ReportInfoServiceImpl extends MessageSender implements ConsultationService {

    private final VolunteerRepository volunteerRepository;

    public ReportInfoServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public SendMessage getKeyboard(Long chatId) {
        return sendMessage(chatId, "Можете отправить отчёт о питомце в любой момент.\n" +
                "Дополнительная информация по кнопкам ниже", ReportKeyboard.reportKeyboard());
    }

    public SendMessage getReportForm(Long chatId) {

        String message = "В ежедневный отчет входит следующая информация:\n" +
                "- Кличка животного\n" +
                "- Фото животного\n" +
                "- Рацион животного\n" +
                "- Общее самочувствие и привыкание к новому месту\n" +
                "- Изменение в поведении: отказ от старых привычек, приобретение новых\n";
        return sendMessage(chatId, message);
    }

    @Override
    public SendMessage getVolunteerContact(Long chatId) {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        Volunteer random = volunteers.get(getRandomNumber(0, volunteers.size() - 1));
        String message = "Обратитесь за помощью к " + random.getUsername();
        return sendMessage(chatId, message);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
