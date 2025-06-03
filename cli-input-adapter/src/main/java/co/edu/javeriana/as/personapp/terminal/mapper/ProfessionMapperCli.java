package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import org.springframework.stereotype.Component;

@Component
public class ProfessionMapperCli {

    public ProfessionModelCli fromDomainToAdapter(Profession profession) {
        return new ProfessionModelCli(
                profession.getIdentification(),
                profession.getName(),
                profession.getDescription()
        );
    }

    public Profession fromAdapterToDomain(ProfessionModelCli model) {
        return new Profession(
                model.getIdentification(),
                model.getName(),
                model.getDescription(),
                null // o puedes pasar una lista vacía de estudios si quieres: new ArrayList<>()
        );
    }
}
