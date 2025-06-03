// StudyMapperRest.java
package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

@Component
public class StudyMapperRest {

    public Study toDomainFromRequest(StudyRequest request, Person person, Profession profession) {
        return new Study(
            person,
            profession,
            LocalDate.parse(request.getGraduationDate()),
            request.getUniversityName()
        );
    }

    public StudyResponse toResponseFromDomain(Study study, String database) {
        return new StudyResponse(
            study.getPerson().getIdentification(),
            study.getProfession().getIdentification(),
            study.getGraduationDate().toString(),
            study.getUniversityName(),
            database
        );
    }
}
