package pro.sky.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.bot.model.Pet;
import pro.sky.bot.service.PetService;

import java.util.Collection;

/**
 * <b>PetsController</b> - controller of pets.<br/>
 */
@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * POST http://localhost:8080
     * create a record
     */
    @PostMapping
    ResponseEntity<Pet> add(@RequestBody Pet pet) {
        return ResponseEntity.ok(petService.add(pet));
    }

    /**
     * GET http://localhost:8080/pets/all
     * general reference request for pets
     * show all pets
     */
    @GetMapping("/all")
    ResponseEntity<Collection<Pet>> getAll() {
        return ResponseEntity.ok(petService.getAll());
    }

    /**
     * GET http://localhost:8080/pets
     * get record by name
     */
    @GetMapping("/{name}")
    ResponseEntity<Pet> getPet(@PathVariable String name) {
        return ResponseEntity.ok(petService.getPet(name));
    }

    /**
     * DELETE  http://localhost:8080/pets
     * Delete by id
     */
    @DeleteMapping("/{id}")
    ResponseEntity removePet(@PathVariable Long id) {
        petService.removePet(id);
        return ResponseEntity.ok().build();
    }
}
