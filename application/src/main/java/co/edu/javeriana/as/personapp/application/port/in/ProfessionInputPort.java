package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@Port
public interface ProfessionInputPort {
     Profession create(Profession p);
    List<Profession> findAll();
    Profession findOne(Integer id) throws NoExistException;
    Profession edit(Integer id, Profession p) throws NoExistException;
    Boolean drop(Integer id) throws NoExistException;
}


