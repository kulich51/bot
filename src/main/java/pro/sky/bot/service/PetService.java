package pro.sky.bot.service;

import pro.sky.bot.model.Pet;

import java.util.Collection;

public interface PetService {

    Pet add(Pet pet);
    Collection<Pet> getAll();
    Pet getPet(String name);
    void removePet(Long id);
}
