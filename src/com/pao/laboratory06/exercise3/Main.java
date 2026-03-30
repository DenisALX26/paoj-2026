package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        System.out.println("TVA curent: " + (ConstanteFinanciare.TVA.getValoare() * 100) + "%\n");

        Inginer[] ingineri = {
            new Inginer("Popescu", "Ion", "0712345678", 8000),
            new Inginer("Ionescu", "Ana", "0712345679", 9500),
            new Inginer("Popa", "Alin", "0712345670", 7000),
        };

        System.out.println("Sortare dupa nume:");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) {
            System.out.println(i);
        }

        System.out.println("\nSortare dupa salariu descrescator:");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) {
            System.out.println(i);
        }

        PlataOnline plataInginer = ingineri[0];
        plataInginer.autentificare("ion.popescu", "parola123");
        System.out.println("Sold initial: " + plataInginer.consultareSold());

        PersoanaJuridica companieFaraTel = new PersoanaJuridica("FaraTelefon SRL", null);
        PersoanaJuridica companieCuTel = new PersoanaJuridica("CuTelefon SRL", "0712345678");

        PlataOnlineSMS plataSMS = companieCuTel;
        plataSMS.trimiteSMS("S-a facut o plata de 200 RON");

        companieFaraTel.trimiteSMS("Acest mesaj este imposibil");

        try {
            plataInginer.autentificare(null, "123");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        try {
            if (!(plataInginer instanceof PlataOnlineSMS)) {
                System.out.println("Assta nu este SMS");
                throw new UnsupportedOperationException("Acest user nu suporta SMS");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}
