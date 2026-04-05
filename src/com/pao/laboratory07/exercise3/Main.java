package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        scanner.nextLine();

        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split(" ");
            String tip = parts[0];
            String nume = parts[1];

            switch (tip) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(nume, parts[3], Double.parseDouble(parts[2])));
                case "DISCOUNTED" -> comenzi.add(
                        new ComandaRedusa(nume, Double.parseDouble(parts[2]), Integer.parseInt(parts[3]), parts[4]));
                case "GIFT" -> comenzi.add(new ComandaGratuita(nume, parts[2]));
            }
        }

        comenzi.forEach(c -> System.out.println(c.descriere(true)));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            String[] cmdParts = line.split(" ");
            String command = cmdParts[0];

            try {
                switch (command) {
                    case "STATS" -> processStats(comenzi);
                    case "FILTER" -> {
                        double threshold = Double.parseDouble(cmdParts[1]);
                        processFilter(comenzi, threshold);
                    }
                    case "SORT" -> processSort(comenzi);
                    case "SPECIAL" -> processSpecial(comenzi);
                    case "QUIT" -> {
                        return;
                    }
                    default -> throw new InvalidCommandException("Comanda necunoscuta: " + command);
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }

    private static void processStats(List<Comanda> comenzi) {
        Map<String, Double> medii = comenzi.stream()
                .collect(Collectors.groupingBy(Comanda::getTip, Collectors.averagingDouble((Comanda::pretFinal))));

        String[] tipuri = { "STANDARD", "DISCOUNTED", "GIFT" };
        for (String tip : tipuri) {
            double medie = medii.getOrDefault(tip, 0.0);
            System.out.printf("%s: medie = %.2f lei\n", tip, medie);
        }
    }

    private static void processFilter(List<Comanda> comenzi, double threshold) {
        comenzi.stream()
                .filter(c -> c.pretFinal() >= threshold)
                .toList()
                .forEach(c -> System.out.println(c.descriere(false)));
    }

    private static void processSort(List<Comanda> comenzi) {
        comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient)
                        .thenComparingDouble(Comanda::pretFinal))
                .forEach(c-> System.out.println(c.descriere(false)));
    }

    private static void processSpecial(List<Comanda> comenzi) {
        comenzi.stream()
                .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                .forEach(c -> System.out.println(c.descriere(false)));
    }
}
