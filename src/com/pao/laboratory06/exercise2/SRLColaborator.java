package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator  extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public double calculVenitNetAnual() {
        double profitAnual = (venitBrutLunar - cheltuieliLunare) * 12;
        double taxa = (profitAnual > 120000.01) ? 0.80 : 0.84;
        return profitAnual * taxa;
    }

}
