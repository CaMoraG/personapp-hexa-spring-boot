package co.edu.javeriana.as.personapp.terminal.mapper;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
@Component
public class TelefonoMapperCli {

    /**
     * Convierte del dominio (Phone) a la representación CLI (PhoneModelCli).
     */
    public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
        Integer ownerCc = null;
        if (phone.getOwner() != null) {
            ownerCc = phone.getOwner().getIdentification();
        }
        return new TelefonoModelCli(
            phone.getNumber(),
            phone.getCompany(),
            ownerCc
        );
    }

    /**
     * Convierte de la petición CLI (PhoneModelCli) al dominio (Phone).
     * @ param model contiene number, company y ownerCc
     * @ return Phone de dominio (solo con identificación de owner)
     */
    public Phone fromAdapterCliToDomain(TelefonoModelCli model) {
        Phone p = new Phone();
        p.setNumber(model.getNumber());
        p.setCompany(model.getCompany());
        // Creamos un Person “esqueleto” con solo el ID:
        co.edu.javeriana.as.personapp.domain.Person owner = new co.edu.javeriana.as.personapp.domain.Person();
        owner.setIdentification(model.getOwnerCc());
        p.setOwner(owner);
        return p;
    }
    
}
