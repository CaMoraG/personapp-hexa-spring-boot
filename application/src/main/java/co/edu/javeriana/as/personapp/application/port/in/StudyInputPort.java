package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {
    Study create(Study s);
    List<Study> findAll();
    Study findOne(Integer personId, Integer profId) throws NoExistException;
    Study edit(Integer personId, Integer profId, Study s) throws NoExistException;
    Boolean drop(Integer personId, Integer profId) throws NoExistException;

}
