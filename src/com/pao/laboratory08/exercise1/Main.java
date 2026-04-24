package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

import com.pao.laboratory08.exercise1.models.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();
        String file = FILE_PATH;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim(), oras = parts[2].trim(), strada = parts[3].trim();
                    int varsta = Integer.parseInt(parts[1].trim());

                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);
                    studenti.add(student);
                }
            }
        } catch (IOException e) {
            System.out.println("A aparut o eroare la citirea fisierului: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            String[] comanda = input.split(" ", 2);
            String tipComanda = comanda[0];
            
            switch (tipComanda) {
                case "PRINT":
                    studenti.forEach(System.out::println);
                    break;

                case "SHALLOW":
                    if (comanda.length == 2) {
                        proceseazaClonare(studenti, comanda[1], false);
                    }
                    break;

                case "DEEP":
                    if (comanda.length == 2) {
                        proceseazaClonare(studenti, comanda[1], true);
                    }
                    break;
            }
        }
        scanner.close();
    }

    private static void proceseazaClonare(List<Student> studenti, String string, boolean b) {
        Student original = null;

        for (Student s : studenti) {
            if (s.getNume().equals(string)) {
                original = s;
                break;
            }
        }

        if (original != null) {
            try {
                Student clona = b ? original.deepClone() : original.shallowClone();

                clona.getAdresa().setOras("MODIFICAT");

                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

}
