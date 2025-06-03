package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

     private final ProfessionOutputPort out;

    public ProfessionUseCase(
        @Qualifier("professionOutputAdapterMaria")
        ProfessionOutputPort out
    ) {
        this.out = out;
    }

    @Override
    public Profession create(Profession p) {
        log.debug("Into create Profession");
        return out.save(p);
    }

    @Override
    public List<Profession> findAll() {
        log.info("Finding all Professions");
        return out.find();
    }

    @Override
    public Profession findOne(Integer id) throws NoExistException {
        Profession p = out.findById(id);
        if (p != null) {
            return p;
        }
        throw new NoExistException("Profession not found: " + id);
    }

    @Override
    public Profession edit(Integer id, Profession p) throws NoExistException {
        Profession existing = out.findById(id);
        if (existing != null) {
            p.setIdentification(id);
            return out.save(p);
        }
        throw new NoExistException("Cannot edit non‐existent Profession: " + id);
    }

    @Override
    public Boolean drop(Integer id) throws NoExistException {
        Profession existing = out.findById(id);
        if (existing != null) {
            return out.delete(id);
        }
        throw new NoExistException("Cannot delete non‐existent Profession: " + id);
    }
}
