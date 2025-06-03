package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Profession;

@Port
public interface ProfessionOutputPort {
    Profession save(Profession p);
    List<Profession> find();
    Profession findById(Integer id);
    Boolean delete(Integer id);

}
