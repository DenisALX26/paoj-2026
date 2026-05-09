package com.pao.laboratory10.exercise3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        List<Tranzactie> tranzactii = Arrays.asList(
                new Tranzactie(1, 1500.00, "2024-01-10", TipTranzactie.CREDIT, "RO01BTRL"),
                new Tranzactie(2, 250.50, "2024-01-15", TipTranzactie.DEBIT, "RO02INGB"),
                new Tranzactie(3, 100.00, "2024-01-20", TipTranzactie.DEBIT, "RO01BTRL"),
                new Tranzactie(4, 3400.00, "2024-02-05", TipTranzactie.CREDIT, "RO03BCR"),
                new Tranzactie(5, 50.00, "2024-02-14", TipTranzactie.DEBIT, "RO04BRD"),
                new Tranzactie(6, 120.00, "2024-02-20", TipTranzactie.DEBIT, "RO02INGB"),
                new Tranzactie(7, 800.00, "2024-02-28", TipTranzactie.CREDIT, "RO01BTRL"),
                new Tranzactie(8, 450.00, "2024-03-05", TipTranzactie.DEBIT, "RO05CEC"),
                new Tranzactie(9, 2100.00, "2024-03-12", TipTranzactie.CREDIT, "RO03BCR"),
                new Tranzactie(10, 75.00, "2024-03-25", TipTranzactie.DEBIT, "RO01BTRL"));

        System.out.println("Toate tranzacțiile de tip CREDIT");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\nSuma totală procesată");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total : %.2f RON\n", totalProcesat);

        System.out.println("\nSuma totală per lună");
        Map<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)));
        sumaPerLuna.forEach((luna, suma) -> System.out.printf(Locale.US, "%s: %.2f RON\n", luna, suma));

        System.out.println("\nTop 3 tranzacții (descrescător după sumă) ---");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\nConturi sursă unice");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi unice: " + conturiUnice);

        System.out.println("\nSuma medie a tranzacțiilor");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON\n", medie);

        System.out.println("\nExtrase de cont lunare");
        Map<String, List<Tranzactie>> extrase = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()));

        extrase.forEach((luna, lista) -> {
            double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n",
                    luna, lista.size(), totalLuna);
        });
    }
}
