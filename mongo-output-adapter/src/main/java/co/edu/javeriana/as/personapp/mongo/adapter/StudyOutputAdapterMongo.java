package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.StudyRepositoryMongo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudyOutputAdapterMongo implements StudyOutputPort {

    private final StudyRepositoryMongo repository;
    private final EstudiosMapperMongo mapper;

    @Override
    public Study save(Study study) {
        return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(study)));
    }

    @Override
    public List<Study> find() {
        return repository.findAll()
                .stream()
                .map(mapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer personId, Integer professionId) {
        String id = personId + "-" + professionId;
        return repository.findById(id)
                .map(mapper::fromAdapterToDomain)
                .orElse(null);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        String id = personId + "-" + professionId;
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
