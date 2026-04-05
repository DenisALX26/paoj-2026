package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected OrderState stare = OrderState.PLACED;
    
    public Comanda(String nume) {
        this.nume = nume;
    }

    public abstract double pretFinal();
    public abstract String descriere();
    
}
