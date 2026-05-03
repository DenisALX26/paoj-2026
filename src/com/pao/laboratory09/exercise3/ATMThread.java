package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread {
    private final int id;
    private final CoadaTranzactii coada;

    public ATMThread(int id, CoadaTranzactii coada) {
        this.id = id;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 4; i++) {
            int idTranzactie = id * 1000 + i;
            double suma = 100.50 * i * id;
            String name = "Client-" + id;
            Tranzactie t = new Tranzactie(idTranzactie, suma, "2024-06-01", name, name, null);

            System.out.println("ATM " + id + " adauga tranzactie: " + t);

            try {
                coada.adauga(t, id);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
