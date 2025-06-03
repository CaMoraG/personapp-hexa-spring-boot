package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import org.springframework.stereotype.Component;

@Component
public class StudyMapperCli {

    public StudyModelCli fromDomainToAdapter(Study study) {
        String personName = study.getPerson().getFirstName() + " " + study.getPerson().getLastName();
        String professionName = study.getProfession().getName();
        String university = study.getUniversityName();
        String graduationDate = study.getGraduationDate() != null ? study.getGraduationDate().toString() : "";

        return new StudyModelCli(personName, professionName, university, graduationDate);
    }
}
