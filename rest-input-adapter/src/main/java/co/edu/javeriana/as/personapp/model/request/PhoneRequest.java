package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
  private String number;
  private String company;
  private String owner;     // cc de la persona dueña
  private String database;  // "MONGO" o "MARIA"
    
}
