package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@Component
public class TelefonoInputAdapterCli {

    @Autowired @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperCli phoneMapperCli;

    @Autowired
    private PersonaInputAdapterCli personaCli;

    private PhoneInputPort useCase;

    /**
     * Inyecta el PhoneOutputPort correcto según la cadena “MARIA” o “MONGO”.
     */
    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (DatabaseOption.MARIA.toString().equalsIgnoreCase(dbOption)) {
            this.useCase = new PhoneUseCase(phoneOutputPortMaria);
            personaCli.setPersonOutputPortInjection("MARIA");
        } else if (DatabaseOption.MONGO.toString().equalsIgnoreCase(dbOption)) {
            this.useCase = new PhoneUseCase(phoneOutputPortMongo);
            personaCli.setPersonOutputPortInjection("MONGO");
        } else {
            throw new InvalidOptionException("Opción de base de datos inválida: " + dbOption);
        }
    }

    /**
     * Lista todos los teléfonos.
     */
    public void listarPhones() {
        log.info("Listado de todos los teléfonos");
        List<Phone> listaDominio = useCase.findAll();
        List<TelefonoModelCli> cliList = listaDominio.stream()
            .map(phoneMapperCli::fromDomainToAdapterCli)
            .collect(Collectors.toList());
        if (cliList.isEmpty()) {
            System.out.println("No hay teléfonos registrados.");
        } else {
            cliList.forEach(p -> System.out.println(p));
        }
    }

    /**
     * Pide datos por consola y crea un nuevo teléfono.
     */
    public void crearPhone(Scanner keyboard) {
        try {
            System.out.print("Número (String): ");
            String number = keyboard.nextLine().trim();

            System.out.print("Compañía: ");
            String company = keyboard.nextLine().trim();

            System.out.print("DNI (cc) del owner: ");
            Integer ownerCc = keyboard.nextInt(); 
            keyboard.nextLine(); // consumir el salto de línea

            // 1) Recuperamos el Person completo desde la BD:
            Person owner = personaCli.obtenerPersonPorCc(ownerCc);

            // 2) Construimos el Phone de dominio usando el owner recuperado:
            Phone nuevo = new Phone();
            nuevo.setNumber(number);
            nuevo.setCompany(company);
            nuevo.setOwner(owner);

            useCase.create(nuevo);
            System.out.println("Teléfono creado: " + number);
        } catch (NoExistException e){
            log.warn("El owner no existe: " + e.getMessage());
        } catch (Exception e) {
            log.warn("Error al crear teléfono: " + e.getMessage());
            keyboard.nextLine(); // descartar
        }
    }

    /**
     * Edita un teléfono (no se puede cambiar el número, solo la compañía u owner).
     */
    public void editarPhone(Scanner keyboard) {
        try {
            System.out.print("Número del teléfono a editar: ");
            String number = keyboard.nextLine().trim();

            System.out.print("Nueva Compañía: ");
            String company = keyboard.nextLine().trim();

            System.out.print("Nuevo DNI (cc) del owner: ");
            Integer ownerCc = keyboard.nextInt();
            keyboard.nextLine();

            // 1) Recuperamos el Person completo desde la BD:
            Person owner = personaCli.obtenerPersonPorCc(ownerCc);

             // 2) Construimos el Phone de dominio con el mismo número y valores nuevos:
            Phone toUpdate = new Phone();
            toUpdate.setNumber(number);
            toUpdate.setCompany(company);
            toUpdate.setOwner(owner);

            useCase.edit(number, toUpdate);
            System.out.println("Teléfono editado: " + number);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al editar teléfono: " + e.getMessage());
            keyboard.nextLine();
        }
    }

    /**
     * Borra un teléfono según su número.
     */
    public void borrarPhone(Scanner keyboard) {
        try {
            System.out.print("Número del teléfono a borrar: ");
            String number = keyboard.nextLine().trim();
            useCase.drop(number);
            System.out.println("Teléfono eliminado: " + number);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al borrar teléfono: " + e.getMessage());
        }
    }

    /**
     * Busca un teléfono por su número y lo muestra.
     */
    public void buscarPhone(Scanner keyboard) {
        try {
            System.out.print("Número del teléfono a buscar: ");
            String number = keyboard.nextLine().trim();
            Phone encontrado = useCase.findOne(number);
            TelefonoModelCli model = phoneMapperCli.fromDomainToAdapterCli(encontrado);
            System.out.println("Encontrado: " + model);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al buscar teléfono: " + e.getMessage());
            keyboard.nextLine();
        }
    }
    
}
