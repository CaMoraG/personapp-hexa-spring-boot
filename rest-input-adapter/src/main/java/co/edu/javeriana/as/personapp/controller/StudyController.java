package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/study")
@Slf4j
public class StudyController {

    private final StudyInputAdapterRest studyInputAdapterRest;
    private static final Logger log = LoggerFactory.getLogger(StudyController.class);

    public StudyController(StudyInputAdapterRest studyInputAdapterRest) {
        this.studyInputAdapterRest = studyInputAdapterRest;
    }

    @GetMapping
    public ResponseEntity<List<StudyResponse>> getAllStudies(@RequestParam(defaultValue = "mongo") String db) {
        log.info("Getting all studies from {}", db);
        return ResponseEntity.ok(studyInputAdapterRest.listar(db));
    }

    @PostMapping
    public ResponseEntity<StudyResponse> createStudy(@RequestBody StudyRequest request) {
        log.info("Creating study {}", request);
        return ResponseEntity.ok(studyInputAdapterRest.crear(request));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteStudy(
            @RequestParam String db,
            @RequestParam Integer personId,
            @RequestParam Integer professionId
    ) {
        log.info("Deleting study with personId={} and professionId={} from {}", personId, professionId, db);
        return ResponseEntity.ok(studyInputAdapterRest.borrar(db, personId, professionId));
    }
}
