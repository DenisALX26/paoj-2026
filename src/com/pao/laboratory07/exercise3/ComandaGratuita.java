package com.pao.laboratory07.exercise3;

public final class ComandaGratuita extends Comanda{
    public ComandaGratuita(String nume, String client) {
        super(nume, client);
    }

    @Override
    public String descriere(boolean cuStare) {
        String base = String.format("GIFT: %s, gratuit", nume);
        if (cuStare) {
            base += String.format(" [%s]", stare);
        }
        return base + String.format(" - client: %s", client);
    }

    @Override
    public String getTip() {
        return "GIFT";
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }
}
