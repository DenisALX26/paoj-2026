package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[newAngajati.length - 1] = a;
        angajati = newAngajati;
        System.out.println("Angajat adaugat: " + a.getNume());
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        System.out.println("Angajati sortati dupa salariu descrecator:");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartment(String nume) {
        System.out.println("Angajati din departamentul " + nume + ":");
        boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(nume)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Nu s-au gasit angajati in departamentul " + nume);
        }
    }
}
