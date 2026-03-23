package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService() {}

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] copy = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, copy, 0, carti.length);
        copy[copy.length - 1] = carte;
        carti = copy;
        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy);
        printList(copy);
    }

    public void listSortedByAuthor(Comparator<Carte> comparator) {
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy, comparator);
        printList(copy);
    }

    private void printList(Carte[] carti) {
        for (int i = 0; i <carti.length; i++) {
            System.out.println((i + 1) + ". " + carti[i]);
        }
    }
}
