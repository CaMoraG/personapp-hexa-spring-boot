package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;

@Mapper
public class PhoneMapperRest {
    public PhoneResponse fromDomainToAdapter(Phone phone, String database) {
        // evita NPE si owner o su ID vienen null
        String ownerId = "";
        if (phone.getOwner() != null && phone.getOwner().getIdentification() != null) {
        ownerId = phone.getOwner().getIdentification().toString();
        }
        return new PhoneResponse(
        phone.getNumber(),
        phone.getCompany(),
        ownerId,
        database,
        "OK"
        );
  }

  public Phone fromAdapterToDomain(PhoneRequest req, Person owner) {
    return new Phone(
      req.getNumber(),
      req.getCompany(),
      owner
    );
  }
    
}
