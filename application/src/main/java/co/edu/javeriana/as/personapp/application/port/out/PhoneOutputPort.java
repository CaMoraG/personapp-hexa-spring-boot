package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Phone;

@Port
public interface PhoneOutputPort {
     /**
     * Crea o actualiza un Phone.
     */
    Phone save(Phone phone);

    /**
     * Devuelve todos los Phone almacenados.
     */
    List<Phone> find();

    /**
     * Busca un Phone por su número (clave única).
     * @param number número telefónico
     */
    Phone findById(String number);

    /**
     * Elimina un Phone por su número.
     * @return true si se eliminó, false si no existía.
     */
    Boolean delete(String number);

}
