package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;


@Component("phoneOutputAdapterMaria")
public class PhoneOutputAdapterMaria  implements PhoneOutputPort{

    private final TelefonoRepositoryMaria repo;
    private final TelefonoMapperMaria mapper;
    private final PersonaRepositoryMaria personaRepo; 

    @Autowired
    public PhoneOutputAdapterMaria(
        TelefonoRepositoryMaria repo,
        TelefonoMapperMaria mapper,
        PersonaRepositoryMaria personaRepo 
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.personaRepo = personaRepo;

    }
    /* 
    @Override
    public Phone save(Phone phone) {
        TelefonoEntity entity = mapper.fromDomainToAdapter(phone);
        TelefonoEntity persisted = repo.save(entity);
        return mapper.fromAdapterToDomain(persisted);
    }
    */
    @Override
    public Phone save(Phone phone) {
        // 1) Mapea solo número + compañía (el mapper ya no pone “duenio”):
        TelefonoEntity entity = mapper.fromDomainToAdapter(phone);

        // 2) Intento recuperar la PersonaEntity que ya existe en la BD:
        Integer ownerCc = phone.getOwner().getIdentification();
        PersonaEntity ownerEnt;
        try {
            ownerEnt = personaRepo.findById(ownerCc)
                .orElseThrow(() -> 
                    new NoExistException("No existe Persona con cc = " + ownerCc)
                );
        } catch (NoExistException ex) {
            // Convertimos la excepción comprobada en una unchecked:
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }

        // 3) Asignamos la referencia ManyToOne:
        entity.setDuenio(ownerEnt);

        // 4) Guardamos el TelefonoEntity sin reintentar persistir la persona:
        TelefonoEntity persisted = repo.save(entity);

        // 5) Mapeamos de vuelta a dominio:
        return mapper.fromAdapterToDomain(persisted);
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
