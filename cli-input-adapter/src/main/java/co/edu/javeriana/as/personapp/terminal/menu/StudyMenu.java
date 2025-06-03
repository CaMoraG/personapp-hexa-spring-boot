package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.StudyInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StudyMenu {

    @Autowired
    private StudyInputAdapterCli studyInputAdapterCli;

    public void iniciarMenu(Scanner keyboard) {
        boolean volver = false;
        do {
            System.out.println("====================");
            System.out.println("MÓDULO ESTUDIOS");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar uno por ID compuesto");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion;
            try {
                opcion = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                log.warn("Entrada inválida. Solo números.");
                keyboard.nextLine();
                continue;
            }

            switch (opcion) {
                case 0:
                    volver = true;
                    break;
                case 1:
                    listarTodos();
                    break;
                case 2:
                    buscarPorId(keyboard);
                    break;
                default:
                    log.warn("Opción no válida.");
            }
        } while (!volver);
    }

    private void listarTodos() {
        List<StudyModelCli> studies = studyInputAdapterCli.listAll();
        if (studies.isEmpty()) {
            System.out.println("No hay estudios registrados.");
        } else {
            System.out.println("Estudios encontrados:");
            studies.forEach(System.out::println);
        }
    }

    private void buscarPorId(Scanner keyboard) {
        try {
            System.out.print("Ingrese ID de la persona: ");
            int personId = keyboard.nextInt();
            keyboard.nextLine();
            System.out.print("Ingrese ID de la profesión: ");
            int professionId = keyboard.nextInt();
            keyboard.nextLine();

            StudyModelCli study = studyInputAdapterCli.findOne(personId, professionId);
            System.out.println("Estudio encontrado: " + study);
        } catch (Exception e) {
            log.warn("Error al buscar estudio: " + e.getMessage());
        }
    }
} 
