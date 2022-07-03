package pro.sky.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.bot.model.Adopter;
import pro.sky.bot.service.AdopterService;

import java.util.Collection;

/**
 * <b>AdopterController</b> - controller of adopters.<br/>
 */

@RestController
@RequestMapping("/adopter")
public class AdopterController {

    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    /**
     * POST http://localhost:8080
     * create a record
     */
    @PostMapping
    ResponseEntity<Adopter> add(@RequestBody Adopter adopter) {
        return ResponseEntity.ok(adopterService.add(adopter));
    }

    /**
     * GET http://localhost:8080/adopter
     * general reference query on adoptive parents
     * show all adopters
     */
    @GetMapping("/all")
    ResponseEntity<Collection<Adopter>> getAll(@RequestParam(defaultValue = "true") Boolean onProbation) {
        return ResponseEntity.ok(adopterService.getAdopters(onProbation));
    }

    /**
     * GET http://localhost:8080/adopter
     * get record by user ID
     */
    @GetMapping("/{userId}")
    ResponseEntity<Collection<Adopter>> getAdopter(@PathVariable Long userId) {
        return ResponseEntity.ok(adopterService.getAdopter(userId));
    }

    /**
     * PUT http://localhost:8080/adopter
     * probation period extension by 14 days ot 30 days
     */
    @PutMapping
    ResponseEntity<Adopter> changeProbation(@RequestBody Adopter adopter,
                                            @RequestParam(defaultValue = "false") boolean acceptProbation,
                                            @RequestParam(defaultValue = "0") int days) {
        return ResponseEntity.ok(adopterService.changeProbation(adopter, acceptProbation, days));
    }

    /**
     * DELETE  http://localhost:8080/adopter
     * delete by id
     */
    @DeleteMapping("/{id}")
    ResponseEntity removeAdopter(@PathVariable Long id) {
        adopterService.removeAdopter(id);
        return ResponseEntity.ok().build();
    }
}
