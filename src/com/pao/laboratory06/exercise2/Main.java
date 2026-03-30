package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> null;
            };
            if (c != null) {
                c.citeste(in);
                colaboratori.add(c);
            }
        }

        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }
        // colaboratori.sort((c1, c2) -> Double.compare(c2.calculVenitNetAnual(), c1.calculVenitNetAnual()));


        Colaborator max = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculVenitNetAnual))
                .orElse(null);
        if (max != null) {
            System.out.print("\nColaborator cu venit net maxim: ");
            max.afiseaza();
        }

        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .forEach(Colaborator::afiseaza);

        System.out.println("\nSume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            double suma = 0;
            int count = 0;
            for (Colaborator c : colaboratori) {
                if (c.tipContract().equals(tip.name())) {
                    suma += c.calculVenitNetAnual();
                    count++;
                }
            }
            System.out.printf("%s: suma = %.2f lei, număr = %d%n", tip.name(), suma, count);
        }

    }
}