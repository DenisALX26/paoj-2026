package com.pao.laboratory07.exercise3;

public final class ComandaStandard extends Comanda {
    private double pret;

    public ComandaStandard(String nume, String client, double pret) {
        super(nume, client);
        this.pret = pret;
    }

    @Override
    public String descriere(boolean cuStare) {
        String base = String.format("STANDARD: %s, pret: %.2f lei", nume, pretFinal());
        if (cuStare) {
            base += String.format(" [%s]", stare);
        }
        return base + String.format(" - client: %s", client);
    }

    @Override
    public String getTip() {
        return "STANDARD";
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    
}
