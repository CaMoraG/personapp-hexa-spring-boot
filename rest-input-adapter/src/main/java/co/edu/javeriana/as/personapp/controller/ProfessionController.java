package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.ProfessionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionController {

    private final ProfessionInputAdapterRest professionInputAdapterRest;
    private static final Logger log = LoggerFactory.getLogger(ProfessionController.class);

    public ProfessionController(ProfessionInputAdapterRest professionInputAdapterRest) {
        this.professionInputAdapterRest = professionInputAdapterRest;
    }

    @GetMapping
    public ResponseEntity<List<ProfessionResponse>> getAllProfessions(@RequestParam(defaultValue = "mongo") String db) {
        log.info("Getting all professions from {}", db);
        return ResponseEntity.ok(professionInputAdapterRest.listar(db));
    }

    @PostMapping
    public ResponseEntity<ProfessionResponse> createOrUpdateProfession(
            @RequestBody ProfessionRequest request,
            @RequestParam(defaultValue = "mongo") String db) {
        log.info("Saving profession {}", request);
        return ResponseEntity.ok(professionInputAdapterRest.crear(request, db));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProfession(
            @RequestParam(defaultValue = "mongo") String db,
            @PathVariable Integer id) {
        log.info("Deleting profession with id {} from {}", id, db);
        return ResponseEntity.ok(professionInputAdapterRest.borrar(db, id));
    }

}
