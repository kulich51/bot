package pro.sky.bot.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import pro.sky.bot.exception.AdopterNotFoundException;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.repository.AdopterRepository;
import pro.sky.bot.service.AdopterService;

import java.util.Collection;
import java.util.Date;

@Service
public class AdopterServiceImpl implements AdopterService {

    private final AdopterRepository adopterRepository;

    public AdopterServiceImpl(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @Override
    public Adopter add(Adopter adopter) {

        Date start = adopter.getStartDateProbation();

        adopter.setFinishDateProbation(getFinishDate(start, 30));
        return adopterRepository.save(adopter);
    }

    @Override
    public Collection<Adopter> getAdopters(Boolean onProbation) {

        if (onProbation) {
            return adopterRepository.getAdopterOnProbation();
        }
        return adopterRepository.findAll();
    }

    @Override
    public Collection<Adopter> getAdopter(Long id) {

        Collection<Adopter> adopter = adopterRepository.getAdopterByUserId(id);
        if (adopter == null) {
            throw new AdopterNotFoundException();
        }
        return adopter;
    }

    Date getFinishDate(Date startDate, int days) {

        return DateUtils.addDays(startDate, days);
    }

    @Override
    public void removeAdopter(Long id) {
        adopterRepository.deleteById(id);
    }
}
