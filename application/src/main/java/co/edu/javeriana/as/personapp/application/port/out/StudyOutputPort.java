package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyOutputPort {
     /**
     * Crea o actualiza un Study.
     */
    Study save(Study study);

    /**
     * Devuelve todos los Study almacenados.
     */
    List<Study> find();

    /**
     * Busca un Study por su clave compuesta (Persona, Profesión).
     * @param personId     identificación de la Persona
     * @param professionId identificación de la Profesión
     */
    Study findById(Integer personId, Integer professionId);

    /**
     * Elimina un Study por su clave compuesta.
     * @return true si se eliminó, false si no existía.
     */
    Boolean delete(Integer personId, Integer professionId);

}
