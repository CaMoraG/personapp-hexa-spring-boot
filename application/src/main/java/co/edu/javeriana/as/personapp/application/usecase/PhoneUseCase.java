package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private final PhoneOutputPort out;

    public PhoneUseCase(
        @Qualifier("phoneOutputAdapterMaria")
        PhoneOutputPort out
    ) {
        this.out = out;
    }

    @Override
    public Phone create(Phone p) {
        log.debug("Into create Phone");
        return out.save(p);
    }

    @Override
    public List<Phone> findAll() {
        log.info("Finding all Phones");
        return out.find();
    }

    @Override
    public Phone findOne(String number) throws NoExistException {
        Phone ph = out.findById(number);
        if (ph != null) {
            return ph;
        }
        throw new NoExistException("Phone not found: " + number);
    }

    @Override
    public Phone edit(String number, Phone p) throws NoExistException {
        Phone existing = out.findById(number);
        if (existing != null) {
            p.setNumber(number);
            return out.save(p);
        }
        throw new NoExistException("Cannot edit non‐existent Phone: " + number);
    }

    @Override
    public Boolean drop(String number) throws NoExistException {
        Phone existing = out.findById(number);
        if (existing != null) {
            return out.delete(number);
        }
        throw new NoExistException("Cannot delete non‐existent Phone: " + number);
    }
    
}
