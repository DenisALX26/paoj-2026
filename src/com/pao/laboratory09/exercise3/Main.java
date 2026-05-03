package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        CoadaTranzactii coada = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        ProcessorThread processorThread = new ProcessorThread(coada);
        Thread pThread = new Thread(processorThread);

        atm1.start();
        atm2.start();
        atm3.start();
        pThread.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();

            processorThread.activ = false;
            synchronized (coada) {
                coada.acceptaCereri = false;
                coada.notifyAll();
            }

            pThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
