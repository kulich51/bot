package pro.sky.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.service.AdopterService;

import java.util.Collection;

@RestController
@RequestMapping("/adopter")
public class AdopterController {

    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @PostMapping
    ResponseEntity<Adopter> add(@RequestBody Adopter adopter) {
        return ResponseEntity.ok(adopterService.add(adopter));
    }

    @GetMapping("/all")
    ResponseEntity<Collection<Adopter>> getAll(@RequestParam(defaultValue = "true") Boolean onProbation ) {
        return ResponseEntity.ok(adopterService.getAdopters(onProbation));
    }

    @GetMapping("/{userId}")
    ResponseEntity<Collection<Adopter>> getAdopter(@PathVariable Long userId) {
        return ResponseEntity.ok(adopterService.getAdopter(userId));
    }

    @DeleteMapping("/{id}")
    ResponseEntity removeAdopter(@PathVariable Long id) {
        adopterService.removeAdopter(id);
        return ResponseEntity.ok().build();
    }
}
