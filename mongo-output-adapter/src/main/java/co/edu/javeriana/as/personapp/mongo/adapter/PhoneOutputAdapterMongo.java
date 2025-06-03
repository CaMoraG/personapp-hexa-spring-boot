package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;


@Component("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    private final TelefonoRepositoryMongo repo;
    private final TelefonoMapperMongo mapper;
    private final PersonaRepositoryMongo personaRepo;

    @Autowired
    public PhoneOutputAdapterMongo(
        TelefonoRepositoryMongo repo,
        TelefonoMapperMongo mapper,
        PersonaRepositoryMongo personaRepo
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.personaRepo = personaRepo;
    }
    /* 
    @Override
    public Phone save(Phone phone) {
        TelefonoDocument doc = mapper.fromDomainToAdapter(phone);
        TelefonoDocument saved = repo.save(doc);
        return mapper.fromAdapterToDomain(saved);
    }
    */
    @Override
    public Phone save(Phone phone) {
        // 1) Mapeamos número + compañía (no asignamos aquí el owner):
        TelefonoDocument doc = mapper.fromDomainToAdapter(phone);

        // 2) Recuperamos el PersonaDocument que ya existe en MongoDB,
        //    por su ID (string). Si no está, lanzamos NoExistException.
        Integer ownerCc = phone.getOwner().getIdentification();
        PersonaDocument ownerDoc = personaRepo.findById(ownerCc)
            .orElseThrow(() -> new RuntimeException("No existe Persona con cc=" + ownerCc));

        // 3) Asignamos la referencia para que Mongo genere un DBRef sin intentar re-persistir la persona:
        doc.setPrimaryDuenio(ownerDoc);

        // 4) Guardamos el TelefonoDocument
        TelefonoDocument saved = repo.save(doc);

        // 5) Convertimos de vuelta a dominio si lo necesitamos:
        return mapper.fromAdapterToDomain(saved);
    }

    @Override
    public List<Phone> find() {
        return repo.findAll().stream()
            .map(mapper::fromAdapterToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        return repo.findById(number)
            .map(mapper::fromAdapterToDomain)
            .orElse(null);
    }

    @Override
    public Boolean delete(String number) {
        repo.deleteById(number);
        return true;
    }
    
}
