package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String db = setPersonOutputPortInjection(request.getDatabase());
			Person saved = personInputPort.create(
                     personaMapperRest.fromAdapterToDomain(request));
			if (DatabaseOption.MONGO.toString().equalsIgnoreCase(db)) {
				return personaMapperRest.fromDomainToAdapterRestMongo(saved);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMaria(saved);
			}
			//setPersonOutputPortInjection(request.getDatabase());
			//Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
			//return personaMapperRest.fromDomainToAdapterRestMaria(person);
		} catch (InvalidOptionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
			//log.warn(e.getMessage());
			//return new PersonaResponse("", "", "", "", "", "", "");
		}
		//return null;
	}

	// — UPDATE
    public PersonaResponse edit(String database, Integer id, PersonaRequest req) {
        try {
            String db = setPersonOutputPortInjection(database);
            Person toUpdate = personaMapperRest.fromAdapterToDomain(req);
            toUpdate.setIdentification(id);
            Person updated = personInputPort.edit(id, toUpdate);
            return mapByDb(db, updated);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
    }

	 // — DELETE
    public void drop(String database, Integer id) {
        try {
            setPersonOutputPortInjection(database);
            personInputPort.drop(id);
        } catch (InvalidOptionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch(NoExistException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
    }


	// Helper común para mapear según la BD
    private PersonaResponse mapByDb(String db, Person person) {
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(db)) {
            return personaMapperRest.fromDomainToAdapterRestMaria(person);
        }
        return personaMapperRest.fromDomainToAdapterRestMongo(person);
    }

	public PersonaResponse findOne(String database, Integer id) {
		try {
			String db = setPersonOutputPortInjection(database);
			Person p = personInputPort.findOne(id);
			return mapByDb(db, p);
		} catch (InvalidOptionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoExistException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}


}
