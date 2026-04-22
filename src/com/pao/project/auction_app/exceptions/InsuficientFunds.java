package com.pao.project.auction_app.exceptions;

public class InsuficientFunds extends RuntimeException {
    public InsuficientFunds(double bidAmount, double userBalance) {
        super(String.format("Bid of %.2f EUR is not sufficient. Your balance is %.2f EUR.", bidAmount, userBalance));
    }

}
