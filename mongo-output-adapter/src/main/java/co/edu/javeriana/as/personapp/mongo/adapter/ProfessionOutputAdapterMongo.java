package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfessionRepositoryMongo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    private final ProfessionRepositoryMongo repository;
    private final ProfesionMapperMongo mapper;

    @Override
    public Profession save(Profession p) {
        return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(p)));
    }

    @Override
    public List<Profession> find() {
        return repository.findAll()
                .stream()
                .map(mapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer id) {
        return repository.findById(id)
                .map(mapper::fromAdapterToDomain)
                .orElse(null);
    }

    @Override
    public Boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
