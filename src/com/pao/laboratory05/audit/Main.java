package com.pao.laboratory05.audit;

import java.util.Scanner;

import com.pao.laboratory05.angajati.Angajat;
import com.pao.laboratory05.angajati.Departament;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați (cu Audit) =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            if (optiune == 0) break;

            switch (optiune) {
                case 1 -> {
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament: ");
                    String dept = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double sal = scanner.nextDouble();
                    service.addAngajat(new Angajat(nume, new Departament(dept, "Sediu"), sal));
                }
                case 2 -> service.listBySalary();
                case 3 -> {
                    System.out.print("Departament: ");
                    service.findByDepartment(scanner.nextLine());
                }
                case 4 -> service.printAuditLog();
                default -> System.out.println("Opțiune invalidă!");
            }
        }
        scanner.close();
    }
}
