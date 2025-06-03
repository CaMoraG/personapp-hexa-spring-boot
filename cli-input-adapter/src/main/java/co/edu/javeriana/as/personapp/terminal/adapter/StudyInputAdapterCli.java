package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Component
public class StudyInputAdapterCli {

    @Autowired
    private StudyInputPort studyInputPort;

    @Autowired
    private StudyMapperCli studyMapperCli;

    public List<StudyModelCli> listAll() {
        return studyInputPort.findAll().stream()
                .map(studyMapperCli::fromDomainToAdapter)
                .collect(Collectors.toList());
    }

    public StudyModelCli findOne(Integer personId, Integer professionId) throws NoExistException {
        Study study = studyInputPort.findOne(personId, professionId);
        return studyMapperCli.fromDomainToAdapter(study);
    }

    public StudyModelCli create(Study study) throws NoExistException {
        return studyMapperCli.fromDomainToAdapter(studyInputPort.create(study));
    }

    public StudyModelCli edit(Integer personId, Integer professionId, Study updated) throws NoExistException {
        return studyMapperCli.fromDomainToAdapter(studyInputPort.edit(personId, professionId, updated));
    }

    public Boolean drop(Integer personId, Integer professionId) throws NoExistException {
        return studyInputPort.drop(personId, professionId);
    }


    public void setStudyInputPort(StudyInputPort inputPort) {
        this.studyInputPort = inputPort;
    }
}
