package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfessionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Component
public class ProfessionMenu {

    @Autowired
    private ProfessionInputAdapterCli professionInputAdapterCli;

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        int opcion = 0;
        do {
            System.out.println("\n--- MENÚ DE PROFESIONES ---");
            System.out.println("1. Listar todas las profesiones");
            System.out.println("2. Crear nueva profesión");
            System.out.println("3. Buscar profesión por ID");
            System.out.println("4. Eliminar profesión");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // consumir salto de línea

            switch (opcion) {
                case 1 -> {
                    professionInputAdapterCli.findAll()
                        .forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Descripción: ");
                    String descripcion = scanner.nextLine();
                    ProfessionModelCli nueva = new ProfessionModelCli(null, nombre, descripcion);
                    System.out.println(professionInputAdapterCli.create(nueva));
                }
                case 3 -> {
                    System.out.print("ID de la profesión: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        System.out.println(professionInputAdapterCli.findOne(id));
                    } catch (NoExistException e) {
                        System.out.println("No se encontró la profesión con ID: " + id);
                    }
                }
                case 4 -> {
                    System.out.print("ID de la profesión a eliminar: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        boolean eliminado = professionInputAdapterCli.drop(id);
                        if (eliminado) {
                            System.out.println("Profesión eliminada correctamente.");
                        } else {
                            System.out.println("No se pudo eliminar la profesión.");
                        }
                    } catch (NoExistException e) {
                        System.out.println("No se encontró la profesión con ID: " + id);
                    }
                }
                case 0 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
}
