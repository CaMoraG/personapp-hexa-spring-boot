package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;


@Component("phoneOutputAdapterMaria")
public class PhoneOutputAdapterMaria  implements PhoneOutputPort{

    private final TelefonoRepositoryMaria repo;
    private final TelefonoMapperMaria mapper;

    @Autowired
    public PhoneOutputAdapterMaria(
        TelefonoRepositoryMaria repo,
        TelefonoMapperMaria mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Phone save(Phone phone) {
        TelefonoEntity entity = mapper.fromDomainToAdapter(phone);
        TelefonoEntity persisted = repo.save(entity);
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
