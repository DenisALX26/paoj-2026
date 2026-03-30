package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume, prenume;
    protected double venitBrutLunar;

    public abstract double calculVenitNetAnual();

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei%n", tipContract(), nume, prenume, calculVenitNetAnual());
    }

    public String getFullName() {
        return nume + " " + prenume;
    }
}
