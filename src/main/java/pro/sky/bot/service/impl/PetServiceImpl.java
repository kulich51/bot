package pro.sky.bot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.bot.exception.PetNotFoundException;
import pro.sky.bot.model.Pet;
import pro.sky.bot.repository.PetRepository;
import pro.sky.bot.service.PetService;

import java.util.Collection;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet add(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Collection<Pet> getAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet getPet(String name) {
        Pet pet = petRepository.getByName(name);
        if (pet == null) {
            throw new PetNotFoundException();
        }
        return pet;
    }

    @Override
    public void removePet(Long id) {
        petRepository.deleteById(id);
    }
}
