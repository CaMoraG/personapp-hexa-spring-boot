package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfessionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

@Adapter
@Component
public class ProfessionInputAdapterRest {

    @Autowired @Qualifier("professionOutputAdapterMaria")
    private co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort professionOutMaria;

    @Autowired @Qualifier("professionOutputAdapterMongo")
    private co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort professionOutMongo;

    @Autowired
    private ProfessionMapperRest mapper;

    private ProfessionInputPort useCase;

    private String choosePort(String db) throws InvalidOptionException {
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(db)) {
            useCase = new ProfessionUseCase(professionOutMaria);
            return DatabaseOption.MARIA.toString();
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(db)) {
            useCase = new ProfessionUseCase(professionOutMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database: " + db);
    }

    public ProfessionResponse crear(ProfessionRequest request, String db) {
        try {
            String selectedDb = choosePort(db);
            Profession domain = mapper.toDomainFromRequest(request);
            Profession saved = useCase.create(domain);
            return mapper.toResponseFromDomain(saved, selectedDb);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public List<ProfessionResponse> listar(String db) {
        try {
            String selectedDb = choosePort(db);
            return useCase.findAll().stream()
                .map(p -> mapper.toResponseFromDomain(p, selectedDb))
                .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Boolean borrar(String db, Integer id) {
        try {
            choosePort(db);
            useCase.drop(id);
            return true;
        } catch (InvalidOptionException | NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public ProfessionResponse findOne(String db, Integer id) {
        try {
            String selectedDb = choosePort(db);
            Profession profession = useCase.findOne(id);
            return mapper.toResponseFromDomain(profession, selectedDb);
        } catch (InvalidOptionException | NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
