package pro.sky.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.bot.model.Pet;
import pro.sky.bot.service.PetService;

import java.util.Collection;

/**
 * Контроллер для работы с животными
 */
@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    ResponseEntity<Pet> add(@RequestBody Pet pet) {
        return ResponseEntity.ok(petService.add(pet));
    }

    @GetMapping("/all")
    ResponseEntity<Collection<Pet>> getAll() {
        return ResponseEntity.ok(petService.getAll());
    }

    @GetMapping("/{name}")
    ResponseEntity<Pet> getPet(@PathVariable String name) {
        return ResponseEntity.ok(petService.getPet(name));
    }

    @DeleteMapping("/{id}")
    ResponseEntity removePet(@PathVariable Long id) {
        petService.removePet(id);
        return ResponseEntity.ok().build();
    }
}
