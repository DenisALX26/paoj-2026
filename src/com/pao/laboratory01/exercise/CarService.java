package com.pao.laboratory01.exercise;

/**
 * Exercițiu — Singleton Service pentru Car
 *
 * 
 *
 * Ce este un Singleton?
 * - O clasă care are O SINGURĂ instanță în toată aplicația.
 * - Constructorul este PRIVAT — nimeni nu poate face "new CarService()".
 * - Se accesează prin CarService.getInstance().
 *
 * De ce Singleton aici?
 * - Avem un singur "depozit" de mașini în tot programul.
 * - Oricine apelează getInstance() primește același depozit.
 *
 * Implementare: Bill Pugh Singleton (cu Holder intern).
 * - Instanța se creează o singură dată, lazy (doar la primul apel).
 * - Thread-safe fără sincronizare explicită.
 */
public class CarService {
    private Car[] cars;

    // Constructor PRIVAT — nu se poate apela din exterior
    private CarService() {
        this.cars = new Car[0];
    }

    // Holder intern — JVM garantează că se inițializează o singură dată
    private static class Holder {
        private static final CarService INSTANCE = new CarService();
    }

    // Punct unic de acces la instanță
    public static CarService getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Afișează toate mașinile din sistem.
     * Dacă nu există mașini, afișează un mesaj corespunzător.
     */
    public void listAllCars() {
        if (cars.length == 0) {
            System.out.println("Nu există mașini în sistem.");
            return;
        }
        for (int i = 0; i < cars.length; i++) {
            System.out.println((i + 1) + ". " + cars[i]);
        }
    }

    /**
     * Adaugă o mașină nouă în array.
     * Folosește pattern-ul de redimensionare dinamică (ca în ArrayDemo).
     */
    public void addCar(Car car) {
        Car[] tmp = new Car[cars.length + 1];
        System.arraycopy(cars, 0, tmp, 0, cars.length);
        tmp[tmp.length - 1] = car;
        cars = tmp;
        System.out.println("Mașina \"" + car.getName() + "\" a fost adăugată!");
    }

    public void addReview(String carName, String review) {
        Car car = null;
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].getName().equals(carName)) {
                car = cars[i];
                break;
            }
        }

        if (car == null) {
            System.out.println("Mașina nu a fost găsită.");
            return;
        }
        String[] reviews = car.getReviews();
        String[] newReviews = new String[reviews.length + 1];
        System.arraycopy(reviews, 0, newReviews, 0, reviews.length);
        newReviews[newReviews.length - 1] = review;
        car.setReviews(newReviews);
        System.out.println("Review adăugat cu succes!");
    }
}
