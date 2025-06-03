package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.StudyRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMaria studyRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        log.info("Saving study in MariaDB");
        var savedEntity = studyRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study));
        return estudiosMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        log.info("Deleting study in MariaDB");
        EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personId);
        if (studyRepositoryMaria.existsById(pk)) {
            studyRepositoryMaria.deleteById(pk);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Study> find() {
        log.info("Finding all studies in MariaDB");
        return studyRepositoryMaria.findAll().stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer personId, Integer professionId) {
        log.info("Finding study by ID (personId={}, professionId={})", personId, professionId);
        EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personId);
        return studyRepositoryMaria.findById(pk)
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
