package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfessionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.StudyInputAdapterCli;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuPrincipal {

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
    private ProfessionInputAdapterCli professionInputAdapterCli;

    @Autowired
    private StudyInputAdapterCli studyInputAdapterCli;

    @Autowired
    private PersonaMenu personaMenu;

    @Autowired
    private TelefonoMenu telefonoMenu;

    @Autowired
    private ProfessionMenu professionMenu;

    @Autowired
    private StudyMenu studyMenu;

    private final Scanner keyboard = new Scanner(System.in);

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
                        elegirMotorYEjecutarPersona();
                        break;
                    case MODULO_PROFESION:
                        elegirMotorYEjecutarProfesion();
                        break;
                    case MODULO_TELEFONO:
                        elegirMotorYEjecutarTelefono();
                        break;
                    case MODULO_ESTUDIO:
                        elegirMotorYEjecutarEstudio();
                        break;
                    default:
                        log.warn("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine();
            }
        } while (!terminar);

        System.out.println("\uD83D\uDC4B Saliendo del programa CLI...");
        keyboard.close();
    }

    private void elegirMotorYEjecutarPersona() {
        elegirMotor("Persona", personaInputAdapterCli, personaMenu);
    }

    private void elegirMotorYEjecutarTelefono() {
        elegirMotor("Teléfono", telefonoInputAdapterCli, telefonoMenu);
    }

    private void elegirMotorYEjecutarProfesion() {
        elegirMotor("Profesiones", professionInputAdapterCli, professionMenu);
    }

    private void elegirMotorYEjecutarEstudio() {
        elegirMotor("Estudios", studyInputAdapterCli, studyMenu);
    }

    private void elegirMotor(String modulo, Object adapter, Object menu) {
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
                        adapter.getClass().getMethod("set" + modulo + "OutputPortInjection", String.class)
                               .invoke(adapter, "MARIA");
                        menu.getClass().getMethod("iniciarMenu", adapter.getClass(), Scanner.class)
                            .invoke(menu, adapter, keyboard);
                        break;
                    case 2:
                        adapter.getClass().getMethod("set" + modulo + "OutputPortInjection", String.class)
                               .invoke(adapter, "MONGO");
                        menu.getClass().getMethod("iniciarMenu", adapter.getClass(), Scanner.class)
                            .invoke(menu, adapter, keyboard);
                        break;
                    default:
                        log.warn("Opción inválida.");
                }
            } catch (Exception e) {
                log.error("Error en ejecución del menú de " + modulo + ": " + e.getMessage());
            }
        } while (!volver);
    }
}
