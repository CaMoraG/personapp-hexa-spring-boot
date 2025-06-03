package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyModelCli {
    private String personName;         // 🔁 Cambiado
    private String professionName;     // 🔁 Cambiado
    private String university;
    private String graduationDate;     // 🔁 Formateado como texto
}
