package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonaMenu {

	private static final int OPCION_REGRESAR = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR = 2;
    private static final int OPCION_EDITAR = 3;
    private static final int OPCION_BORRAR = 4;
    private static final int OPCION_BUSCAR_ID = 5;
	// mas opciones

	/**
     * 1) Mostrar el submenú de Persona  
     * 2) Invocar los métodos de PersonaInputAdapterCli según la opción  
     * 3) Regresar cuando el usuario pulse 0.
     */
    public void iniciarMenu(PersonaInputAdapterCli cliAdapter, Scanner keyboard) {
        boolean salir = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR:
                        salir = true;
                        break;
                    case OPCION_VER_TODO:
                        cliAdapter.historial();
                        break;
                    case OPCION_CREAR:
                        cliAdapter.crearPersona(keyboard);
                        break;
                    case OPCION_EDITAR:
                        cliAdapter.editarPersona(keyboard);
                        break;
                    case OPCION_BORRAR:
                        cliAdapter.borrarPersona(keyboard);
                        break;
                    case OPCION_BUSCAR_ID:
                        cliAdapter.buscarPersona(keyboard);
                        break;
                    default:
                        System.out.println("Opción inválida. Vuelve a intentarlo.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine(); // descartar lo que no sea int
            }
        } while (!salir);
    }


 	private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " - Ver todas las personas");
        System.out.println(OPCION_CREAR + " - Crear persona");
        System.out.println(OPCION_EDITAR + " - Editar persona");
        System.out.println(OPCION_BORRAR + " - Borrar persona");
        System.out.println(OPCION_BUSCAR_ID + " - Buscar persona por ID");
        System.out.println(OPCION_REGRESAR + " - Regresar al menú anterior");
        System.out.print("Ingrese una opción: ");
    }

	private int leerOpcion(Scanner keyboard) {
        return keyboard.nextInt();
    }


	

}
