package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.mapper.ProfessionMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

@Adapter
@Component
public class StudyInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutMongo;

    @Autowired
    private StudyMapperRest mapper;

    @Autowired
    private PersonaInputAdapterRest personaAdapter;

    @Autowired
    private ProfessionInputAdapterRest professionAdapter;

    @Autowired
    private PersonaMapperRest personaMapper;

    @Autowired
    private ProfessionMapperRest professionMapper;

    private StudyInputPort useCase;

    private String choosePort(String db) throws InvalidOptionException {
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(db)) {
            useCase = new StudyUseCase(studyOutMaria);
            return DatabaseOption.MARIA.toString();
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(db)) {
            useCase = new StudyUseCase(studyOutMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database: " + db);
    }

    public StudyResponse crear(StudyRequest req) {
        try {
            String db = choosePort(req.getDatabase());

            PersonaResponse personaRes = personaAdapter.findOne(db, req.getIdPerson());
            ProfessionResponse professionRes = professionAdapter.findOne(db, req.getIdProfession());

            Person person = personaMapper.fromAdapterToDomain(personaRes);
            Profession profession = professionMapper.toDomainFromResponse(professionRes);

            Study toSave = mapper.toDomainFromRequest(req, person, profession);
            Study saved = useCase.create(toSave);

            return mapper.toResponseFromDomain(saved, db);

        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public List<StudyResponse> listar(String db) {
        try {
            String chosen = choosePort(db);
            return useCase.findAll().stream()
                    .map(study -> mapper.toResponseFromDomain(study, chosen))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public boolean borrar(String db, Integer personId, Integer professionId) {
        try {
            choosePort(db);
            return useCase.drop(personId, professionId);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
