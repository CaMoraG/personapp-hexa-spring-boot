package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;


@Component("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {
    private final TelefonoRepositoryMongo repo;
    private final TelefonoMapperMongo mapper;

    @Autowired
    public PhoneOutputAdapterMongo(
        TelefonoRepositoryMongo repo,
        TelefonoMapperMongo mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Phone save(Phone phone) {
        TelefonoDocument doc = mapper.fromDomainToAdapter(phone);
        TelefonoDocument saved = repo.save(doc);
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
