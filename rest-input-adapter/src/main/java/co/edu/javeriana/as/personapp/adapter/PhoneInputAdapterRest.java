package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.mapper.PhoneMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@Component
public class PhoneInputAdapterRest {

    @Autowired @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutMaria;

    @Autowired @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutMongo;

    @Autowired
    private PhoneMapperRest mapper;

    @Autowired
    private PersonaInputAdapterRest personaAdapter;

    @Autowired
    private PersonaMapperRest personaMapperRest;

    private PhoneInputPort useCase;

    private String choosePort(String db) throws InvalidOptionException {
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(db)) {
            useCase = new PhoneUseCase(phoneOutMaria);
            return DatabaseOption.MARIA.toString();
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(db)) {
            useCase = new PhoneUseCase(phoneOutMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database: " + db);
    }

    public PhoneResponse crear(PhoneRequest req) {
        try {
            String db = choosePort(req.getDatabase());

            // 1) Recuperar la Persona como PersonaResponse
            PersonaResponse pr = personaAdapter.findOne(
                db,
                Integer.valueOf(req.getOwner())
            );
            // 2) Convertir a dominio Person
            Person owner = personaMapperRest.fromAdapterToDomain(pr);

            // 3) Mapeo request → domain y guardo
            Phone toSave = mapper.fromAdapterToDomain(req, owner);
            Phone saved = useCase.create(toSave);

            // 4) convierto domain → response
            return mapper.fromDomainToAdapter(saved, db);

        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public List<PhoneResponse> listar(String db) {
        try {
            String chosen = choosePort(db);
            return useCase.findAll().stream()
                .map(p -> mapper.fromDomainToAdapter(p, chosen))
                .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public PhoneResponse obtener(String db, String number) {
        try {
            String chosen = choosePort(db);
            Phone p = useCase.findOne(number);
            return mapper.fromDomainToAdapter(p, chosen);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public PhoneResponse actualizar(String db, String number, PhoneRequest req) {
        try {
            String chosen = choosePort(db);

            // recuperar y mapear el owner de nuevo
            PersonaResponse pr = personaAdapter.findOne(
                chosen,
                Integer.valueOf(req.getOwner())
            );
            Person owner = personaMapperRest.fromAdapterToDomain(pr);

            Phone toUpdate = mapper.fromAdapterToDomain(req, owner);
            toUpdate.setNumber(number);

            Phone updated = useCase.edit(number, toUpdate);
            return mapper.fromDomainToAdapter(updated, chosen);

        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public void borrar(String db, String number) {
        try {
            choosePort(db);
            useCase.drop(number);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
