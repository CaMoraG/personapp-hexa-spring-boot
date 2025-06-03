package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class PhoneResponse extends PhoneRequest {
    private String status;   // e.g. "OK"

    // Constructor que engloba todos los campos del padre + el status
    public PhoneResponse(
        String number,
        String company,
        String owner,
        String database,
        String status
    ) {
        super(number, company, owner, database);
        this.status = status;
    }
    
}
