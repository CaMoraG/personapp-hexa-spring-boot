package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@Component
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}

	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	/**
     * Crea una nueva persona pidiéndole datos al usuario por consola.
     */
    public void crearPersona(Scanner keyboard) {
        try {
            System.out.print("DNI (cc): ");
            Integer cc = keyboard.nextInt(); keyboard.nextLine();

            System.out.print("Nombre: ");
            String nombre = keyboard.nextLine();

            System.out.print("Apellido: ");
            String apellido = keyboard.nextLine();

            System.out.print("Género (MALE/FEMALE/OTHER): ");
            String genero = keyboard.nextLine();

            System.out.print("Edad: ");
            Integer edad = keyboard.nextInt(); keyboard.nextLine();
            Person nueva = new Person(
                cc,
                nombre,
                apellido,
                Gender.valueOf(genero.toUpperCase()),
                edad,
                null,   // phoneNumbers (no lo solicitamos aquí)
                null    // studies (no lo solicitamos aquí)
            );

            personInputPort.create(nueva);
            System.out.println("Persona creada correctamente (cc = " + cc + ")");
        } catch (Exception e) {
            log.warn("Error al crear persona: " + e.getMessage());
            keyboard.nextLine(); // descartamos si quedó algo mal leído
        }
    }

	 /**
     * Edita una persona existente pidiéndole datos al usuario por consola.
     */
    public void editarPersona(Scanner keyboard) {
        try {
            System.out.print("DNI (cc) de la persona a editar: ");
            Integer cc = keyboard.nextInt(); keyboard.nextLine();

            System.out.print("Nuevo Nombre: ");
            String nombre = keyboard.nextLine();

            System.out.print("Nuevo Apellido: ");
            String apellido = keyboard.nextLine();

            System.out.print("Nuevo Género (MALE/FEMALE/OTHER): ");
            String genero = keyboard.nextLine();

            System.out.print("Nueva Edad: ");
            Integer edad = keyboard.nextInt(); keyboard.nextLine();

            Person actualizado = new Person(
                cc,
                nombre,
                apellido,
                Gender.valueOf(genero.toUpperCase()),
                edad,
                null,
                null
            );

            personInputPort.edit(cc, actualizado);
            System.out.println("Persona editada correctamente (cc = " + cc + ")");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al editar persona: " + e.getMessage());
            keyboard.nextLine();
        }
    }

	/**
     * Borra una persona existente pidiéndole el DNI (cc) al usuario.
     */
    public void borrarPersona(Scanner keyboard) {
        try {
            System.out.print("DNI (cc) de la persona a borrar: ");
            Integer cc = keyboard.nextInt(); keyboard.nextLine();

            personInputPort.drop(cc);
            System.out.println("Persona borrada correctamente (cc = " + cc + ")");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al borrar persona: " + e.getMessage());
            keyboard.nextLine();
        }
    }

    /**
     * Busca una persona por su DNI (cc) y la muestra por pantalla.
     */
    public void buscarPersona(Scanner keyboard) {
        try {
            System.out.print("DNI (cc) de la persona a buscar: ");
            Integer cc = keyboard.nextInt(); keyboard.nextLine();

            Person encontrada = personInputPort.findOne(cc);
            PersonaModelCli model = personaMapperCli.fromDomainToAdapterCli(encontrada);
            System.out.println("Encontrada: " + model);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn("Error al buscar persona: " + e.getMessage());
            keyboard.nextLine();
        }
    }

	/**
	 * Recupera una Person del dominio por su cc.
	 * Lanza NoExistException si no se encuentra.
	 */
	public Person obtenerPersonPorCc(Integer cc) throws NoExistException {
		return personInputPort.findOne(cc);
	}



}
