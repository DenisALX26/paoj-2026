package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus = false;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();

        if (in.hasNext("DA") || in.hasNext("NU")) {
            this.bonus = in.next().equals("DA");
        }
    }

    @Override
    public double calculVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        return bonus ? net * 1.1 : net;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}
