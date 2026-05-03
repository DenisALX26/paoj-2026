package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa
        // contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de
        // serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream
        // (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream
        // (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        // - LIST → afișează toate tranzacțiile, câte una pe linie
        // - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        // sau "Niciun rezultat." dacă nu există
        // - NOTE id → afișează "NOTE[id]: <valoarea câmpului note>"
        // sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        // [id] data tip: suma RON | contSursa -> contDestinatie
        // Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next().toUpperCase());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.note = "procesat";
            tranzactii.add(tranzactie);
        }

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File("output/lab09_ex1.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(tranzactii);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Tranzactie> tranzactiiDeserializate = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(outputFile))) {
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            if (comanda.equals("LIST")) {
                for (Tranzactie t : tranzactiiDeserializate) {
                    System.out.println(t);
                }
            } else if (comanda.equals("FILTER")) {
                String prefix = scanner.next();
                boolean found = false;
                for (Tranzactie t : tranzactiiDeserializate) {
                    if (t.data.startsWith(prefix)) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (comanda.equals("NOTE")) {
                int id = scanner.nextInt();
                boolean found = false;
                for (Tranzactie t : tranzactiiDeserializate) {
                    if (t.id == id) {
                        System.out.println("NOTE[" + id + "]: " + t.note);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
        scanner.close();
    }
}
