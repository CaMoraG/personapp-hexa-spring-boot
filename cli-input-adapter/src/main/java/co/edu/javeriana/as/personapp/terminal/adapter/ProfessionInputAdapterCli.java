package co.edu.javeriana.as.personapp.terminal.adapter;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfessionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfessionInputAdapterCli {

    @Autowired
    private ProfessionInputPort professionInputPort;

    @Autowired
    private ProfessionMapperCli professionMapperCli;

    public List<ProfessionModelCli> listAll() {
        return professionInputPort.findAll().stream()
                .map(professionMapperCli::fromDomainToAdapter)
                .collect(Collectors.toList());
    }

    public ProfessionModelCli findOne(int id) throws NoExistException {
        Profession profession = professionInputPort.findOne(id);
        return professionMapperCli.fromDomainToAdapter(profession);
    }

    public ProfessionModelCli create(ProfessionModelCli model) {
        Profession domain = professionMapperCli.fromAdapterToDomain(model);
        return professionMapperCli.fromDomainToAdapter(professionInputPort.create(domain));
    }

    public ProfessionModelCli edit(int id, ProfessionModelCli updated) throws NoExistException {
        Profession domain = professionMapperCli.fromAdapterToDomain(updated);
        return professionMapperCli.fromDomainToAdapter(professionInputPort.edit(id, domain));
    }

    public Boolean drop(int id) throws NoExistException {
        return professionInputPort.drop(id);
    }

    public void setProfessionInputPort(ProfessionInputPort port) {
        this.professionInputPort = port;
    }

    public List<Profession> findAll() {
    return professionInputPort.findAll();
}

}
