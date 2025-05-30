package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

@Port
public interface PhoneInputPort {
    Phone create(Phone p);
    List<Phone> findAll();
    Phone findOne(String number) throws NoExistException;
    Phone edit(String number, Phone p) throws NoExistException;
    Boolean drop(String number) throws NoExistException;

}
