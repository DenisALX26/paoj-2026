package com.pao.project.auction_app.utils;

import com.pao.project.auction_app.services.UserService;

public class DataSeeder {
    private static UserService userService = UserService.getInstance();

    public static void seedUsers() {
        System.out.println("Seeding users...");

        try {
            userService.signUp("SELLER", "AutoExpert_RO", "contact@autoexpert.ro", "pass123");
            userService.signUp("SELLER", "VintageCars_Official", "office@vintage.com", "retro99");
            userService.signUp("SELLER", "ElectricMoves", "hello@emoves.ro", "tesla2026");

            userService.signUp("BIDDER", "Ion_Licitatorul", "ion@yahoo.com", "ionut123");
            userService.signUp("BIDDER", "Maria_M", "maria.m@gmail.com", "maria_pass");
            userService.signUp("BIDDER", "Collector_99", "collector@auctions.com", "rare_items");

            System.out.println("Seed completat cu succes!");
        } catch (Exception e) {
            System.err.println("Error seeding users: " + e.getMessage());
        }
    }
}
