package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

import com.pao.laboratory09.exercise1.Tranzactie;

public class CoadaTranzactii {
    private final int CAPACITATE = 5;
    private final Queue<Tranzactie> coada = new LinkedList<>();

    public volatile boolean acceptaCereri = true;

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == CAPACITATE) {
            System.out.println("ATM " + atmId + " asteapta");
            wait();
        }
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }
}
