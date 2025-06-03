package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuPrincipal {
	
	// Constantes para cada módulo
    private static final int SALIR = 0;
    private static final int MODULO_PERSONA = 1;
    private static final int MODULO_PROFESION = 2;
    private static final int MODULO_TELEFONO = 3;
    private static final int MODULO_ESTUDIO = 4;

    @Autowired
    private PersonaInputAdapterCli personaInputAdapterCli;

	@Autowired
	private TelefonoInputAdapterCli telefonoInputAdapterCli;

	@Autowired
	private PersonaMenu personaMenu;

	@Autowired
	private TelefonoMenu telefonoMenu;

    private final Scanner keyboard = new Scanner(System.in);

	/* 
    public MenuPrincipal() {
        this.personaMenu = new PersonaMenu();
        this.keyboard = new Scanner(System.in);
    }
	*/

    /**
     * Este método se invoca desde el CommandLineRunner;  
     * muestra el menú principal y, si eligen “1”, invoca a PersonaMenu.
     */
    public void inicio() {
        boolean terminar = false;
        do {
            System.out.println("----------------------");
            System.out.println(MODULO_PERSONA + " - Módulo de Personas");
            System.out.println(MODULO_PROFESION + " - Módulo de Profesiones");
            System.out.println(MODULO_TELEFONO + " - Módulo de Teléfonos");
            System.out.println(MODULO_ESTUDIO + " - Módulo de Estudios");
            System.out.println(SALIR + " - Salir");
            System.out.print("Ingrese una opción: ");
            try {
                int opcion = keyboard.nextInt();
                switch (opcion) {
                    case SALIR:
                        terminar = true;
                        break;
                    case MODULO_PERSONA:
                        // 1) El usuario elige primero la base de datos:
                        elegirMotorYEjecutarPersona();
                        break;
                    case MODULO_PROFESION:
                        log.warn("Módulo de Profesiones aún no implementado.");
                        break;
                    case MODULO_TELEFONO:
						elegirMotorYEjecutarTelefono();
                        break;
                    case MODULO_ESTUDIO:
                        log.warn("Módulo de Estudios aún no implementado.");
                        break;
                    default:
                        log.warn("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine();
            }
        } while (!terminar);

        System.out.println("👋 Saliendo del programa CLI...");
        keyboard.close();
    }

    /**
     * Pide al usuario que elija “MARIA” o “MONGO” y, acto seguido,
     * invoca al PersonaMenu con el adaptador configurado.
     */
    private void elegirMotorYEjecutarPersona() {
        boolean volver = false;
        do {
            System.out.println("----------------------");
            System.out.println("1 - Usar MariaDB");
            System.out.println("2 - Usar MongoDB");
            System.out.println("0 - Regresar");
            System.out.print("Ingrese una opción: ");
            int opcion = keyboard.nextInt();
            keyboard.nextLine();
            try {
                switch (opcion) {
                    case 0:
                        volver = true;
                        break;
                    case 1:
                        personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
                        personaMenu.iniciarMenu(personaInputAdapterCli, keyboard);
                        break;
                    case 2:
                        personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
                        personaMenu.iniciarMenu(personaInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("Opción inválida.");
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        } while (!volver);
    }

	 /**
     * Pide al usuario que elija “MARIA” o “MONGO” y, acto seguido,
     * invoca al TelefonoMenu con el adaptador configurado.
     */
    private void elegirMotorYEjecutarTelefono() {
        boolean volver = false;
        do {
            System.out.println("----------------------");
            System.out.println("1 - Usar MariaDB");
            System.out.println("2 - Usar MongoDB");
            System.out.println("0 - Regresar");
            System.out.print("Ingrese una opción: ");

            int opcion;
            try {
                opcion = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine();
                continue;
            }

            try {
                switch (opcion) {
                    case 0:
                        volver = true;
                        break;
                    case 1:
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                        telefonoMenu.iniciarMenu(telefonoInputAdapterCli, keyboard);
                        break;
                    case 2:
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                        telefonoMenu.iniciarMenu(telefonoInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("Opción inválida.");
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        } while (!volver);
    }

	private int leerOpcion() {
        try {
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.nextLine();
            return leerOpcion();
        }
    }

}
