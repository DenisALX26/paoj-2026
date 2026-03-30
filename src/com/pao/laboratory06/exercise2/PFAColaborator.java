package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;
    private static final double SMB = 4050;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculVenitNetAnual() {
        double profitAnual = (venitBrutLunar - cheltuieliLunare) * 12;
        
        if (profitAnual >= 108000) 
            return 86400.00;
        if (profitAnual <= 42000)
            return 19200.00;

        return profitAnual * 0.72;
    }

    @Override
    public String tipContract() {
        return "PFA";
    }
}
