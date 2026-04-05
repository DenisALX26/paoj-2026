package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda{
    private double pret;
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, client);
        this.pret = pret;
        this.discountProcent = discountProcent;
    }

    public int getDiscountProcent() {
        return discountProcent;
    }

    @Override
    public String descriere(boolean cuStare) {
        String base = String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%)", nume, pretFinal(), discountProcent);
        if (cuStare) {
            base += String.format(" [%s]", stare);
        }
        return base + String.format(" - client: %s", client);
    }

    @Override
    public String getTip() {
        return "DISCOUNTED";
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    
}
