package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;

import com.pao.laboratory08.exercise1.models.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt",
            OUTPUT_FILE = "src/com/pao/laboratory08/exercise2/rezultate.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim(), oras = parts[2].trim(), strada = parts[3].trim();
                    int varsta = Integer.parseInt(parts[1].trim());

                    studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            System.out.println("Prag: ");
            scanner.close();
            return;
        }
        int prag = scanner.nextInt();
        scanner.close();

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            System.out.println("Prag: " + prag);
            System.out.println("Total studenți filtrați: " + filtrati.size());

            for (Student s : filtrati) {
                System.out.println(s);

                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
