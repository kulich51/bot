package pro.sky.bot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.bot.exception.AdopterNotFoundException;
import pro.sky.bot.exception.PetNotFoundException;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.repository.AdopterRepository;
import pro.sky.bot.service.AdopterService;

import java.util.Collection;

@Service
public class AdopterServiceImpl implements AdopterService {

    private final AdopterRepository adopterRepository;

    public AdopterServiceImpl(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @Override
    public Adopter add(Adopter adopter) {
        return adopterRepository.save(adopter);
    }

    @Override
    public Collection<Adopter> getAll() {
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

    @Override
    public void removeAdopter(Long id) {
        adopterRepository.deleteById(id);
    }
}
