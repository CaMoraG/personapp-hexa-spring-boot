package co.edu.javeriana.as.personapp.mapper;

import org.springframework.stereotype.Component;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

import java.util.ArrayList;

@Component
public class ProfessionMapperRest {

    public Profession toDomainFromRequest(ProfessionRequest request) {
        return new Profession(
            request.getIdentification(),
            request.getName(),
            request.getDescription(),
            new ArrayList<>() // Lista vacía de estudios si no estás manejándolos aún
        );
    }

    public Profession toDomainFromResponse(ProfessionResponse response) {
        return new Profession(
            response.getIdentification(),
            response.getName(),
            response.getDescription(),
            new ArrayList<>()
        );
    }

    public ProfessionResponse toResponseFromDomain(Profession domain, String database) {
        return new ProfessionResponse(
            domain.getIdentification(),
            domain.getName(),
            domain.getDescription(),
            database
        );
    }
}
