package pro.sky.bot.service;

import pro.sky.bot.model.Adopter;
import pro.sky.bot.model.Pet;

import java.util.Collection;

public interface AdopterService {

    Adopter add(Adopter adopter);
    Collection<Adopter> getAll();
    Collection<Adopter> getAdopter(Long userId);
    void removeAdopter(Long id);
}
