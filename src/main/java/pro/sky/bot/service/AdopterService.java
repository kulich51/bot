package pro.sky.bot.service;

import pro.sky.bot.model.Adopter;

import java.util.Collection;

public interface AdopterService {

    Adopter add(Adopter adopter);
    Collection<Adopter> getAdopters(Boolean onProbation);
    Collection<Adopter> getAdopter(Long userId);
    void removeAdopter(Long id);
}
