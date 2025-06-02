package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelefonoMenu {

    private static final int OPCION_REGRESAR            = 0;
    private static final int OPCION_VER_TODO            = 1;
    private static final int OPCION_CREAR               = 2;
    private static final int OPCION_EDITAR              = 3;
    private static final int OPCION_BORRAR              = 4;
    private static final int OPCION_BUSCAR_POR_NUMERO   = 5;

    /**
     * Muestra **solo** el submenú CRUD de teléfonos.
     * (Se asume que ya recibimos el PhoneInputAdapter Cli con la base configurada.)
     */
    public void iniciarMenu(TelefonoInputAdapterCli phoneAdapter, Scanner keyboard) {
        boolean volver = false;
        do {
            System.out.println("----------------------");
            System.out.println(OPCION_VER_TODO          + " - Ver todos los teléfonos");
            System.out.println(OPCION_CREAR             + " - Crear teléfono");
            System.out.println(OPCION_EDITAR            + " - Editar teléfono");
            System.out.println(OPCION_BORRAR            + " - Borrar teléfono");
            System.out.println(OPCION_BUSCAR_POR_NUMERO + " - Buscar teléfono por número");
            System.out.println(OPCION_REGRESAR          + " - Regresar al módulo anterior");
            System.out.print("Ingrese una opción: ");

            int opcion;
            try {
                opcion = keyboard.nextInt();
                keyboard.nextLine(); // consumir el “enter”
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine();
                continue;
            }

            switch (opcion) {
                case OPCION_REGRESAR:
                    volver = true;
                    break;
                case OPCION_VER_TODO:
                    phoneAdapter.listarPhones();
                    break;
                case OPCION_CREAR:
                    phoneAdapter.crearPhone(keyboard);
                    break;
                case OPCION_EDITAR:
                    phoneAdapter.editarPhone(keyboard);
                    break;
                case OPCION_BORRAR:
                    phoneAdapter.borrarPhone(keyboard);
                    break;
                case OPCION_BUSCAR_POR_NUMERO:
                    phoneAdapter.buscarPhone(keyboard);
                    break;
                default:
                    log.warn("Opción inválida. Intenta nuevamente.");
            }
        } while (!volver);
    }
    
}
