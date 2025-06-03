package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@UseCase
public class StudyUseCase implements StudyInputPort {
    private final StudyOutputPort out;

    public StudyUseCase(
        @Qualifier("studyOutputAdapterMaria")
        StudyOutputPort out
    ) {
        this.out = out;
    }

    @Override
    public Study create(Study s) {
        log.debug("Into create Study");
        return out.save(s);
    }

    @Override
    public List<Study> findAll() {
        log.info("Finding all Studies");
        return out.find();
    }

    @Override
    public Study findOne(Integer personId, Integer professionId) throws NoExistException {
        Study st = out.findById(personId, professionId);
        if (st != null) {
            return st;
        }
        throw new NoExistException(
            "Study not found for person " + personId +
            " and profession " + professionId
        );
    }

    @Override
    public Study edit(Integer personId, Integer professionId, Study s) throws NoExistException {
        Study existing = out.findById(personId, professionId);
        if (existing != null) {
            // asegurar que la clave compuesta quede igual
            s.setPerson(existing.getPerson());
            s.setProfession(existing.getProfession());
            return out.save(s);
        }
        throw new NoExistException(
            "Cannot edit non‐existent Study for person " + personId +
            " and profession " + professionId
        );
    }

    @Override
    public Boolean drop(Integer personId, Integer professionId) throws NoExistException {
        Study existing = out.findById(personId, professionId);
        if (existing != null) {
            return out.delete(personId, professionId);
        }
        throw new NoExistException(
            "Cannot delete non‐existent Study for person " + personId +
            " and profession " + professionId
        );
    }
    
}
