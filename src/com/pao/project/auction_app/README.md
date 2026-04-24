# Sistem de Licitatii Auto - Proiect PAO (Etapa I)

Acest proiect reprezinta implementarea unui sistem de licitatii pentru vehicule

---

## 1. Definirea sistemului

### 1.1 - Lista completa de actiuni (16)
1. **SignUp**: Inregistrarea unui nou utilizator (Seller sau Bidder)
2. **Login**: Autentificarea in sistem cu verificarea credentialelor
3. **Data Seeding**: Popularea automata a sistemului cu date de test (useri, masini, motoare)
4. **Data Reset**: Stergerea completa a datelor din fisierele CSV pentru un "fresh start"
5. **Adaugare Masina (Termica/Electrica/Hibrida)**: Inregistrarea masinilor cu specificatii detaliate de motorizare
6. **Adaugare Motocicletă (Sport/Naked)**: Inregistrarea motocicletelor cu atribute specifice (ABS, Quickshifter, moduri de condus)
7. **Lansare Licitatie Buy Now**: Crearea unei licitatii cu pret fix de cumparare instanta
8. **Lansare Licitatie Blind**: Crearea unei licitatii unde ofertele sunt ascunse pana la final
9. **Vizualizare Inventar Complet**: Listarea tuturor vehiculelor detinute de un Seller
10. **Filtrare Inventar (Masini/Moto)**: Vizualizarea selectiva a tipurilor de vehicule din inventar
11. **Editare Pret Vehicul**: Modificarea pretului unui vehicul existent in inventar
12. **Editare Kilometraj**: Actualizarea rulajului unui vehicul
13. **Cautare Licitatii Active**: Listarea tuturor licitatiilor, sortate automat
14. **Plasare Oferta (Bid)**: Depunerea unei oferte cu verificarea automata a regulilor de licitare si a fondurilor
15. **Gestionare Balanta**: Verificarea fondurilor disponibile pentru Bidderi
16. **Alimentare Cont**: Adaugarea de fonduri in portofelul virtual

### 1.2 - Tipuri de obiecte
`User` -> `Seller`, `Bidder` <br>
`Vehicle` ->`Car`, `Motorcycle`<br>
`Car` -> `ElectricCar`, `HybridCar`, `ThermalCar`<br>
`Motorcycle` -> `Sport`, `Naked`<br>
`Engine` -> `ElectricEngine`, `ThermalEngine`, `HybridEngine`<br>
`Auction` -> `BuyNowAuction`, `BlindAuction`<br>
`DataSeeder` <br>
`Bid`

---

## 2. Implementare Java

### 2.1 - Clase si OOP
* **Clase modelate**: 20+ clase care definesc ierarhia sistemului
* **Incapsulare**: Atribute `private`/`protected`
* **Mostenire (3 niveluri)**: 
    * `Vehicle` -> `Car` -> `ElectricCar`
    * `Vehicle` -> `Motorcycle` -> `Sport`
* **Abstractizare**: Clase abstracte la fiecare nivel de baza: `User`, `Vehicle`, `Car`, `Motorcycle`, `Engine`, `Auction`
* **Suprascriere (equals/hashCode)**: Implementate in `User` si `Vehicle` pentru managementul corect al colectiilor
* **Clasa Imutabila**: `Bid` - folosita pentru inregistrarea ofertelor fara posibilitate de modificare ulterioara
* **Exceptii Custom**: `InsuficientFunds`, `BidMustBeHigher`

### 2.2 - Colectii
* **Tipuri utilizate**: `List` (ArrayList), `Set` (TreeSet), `Map` (HashMap)
* **Colectie sortata**: `TreeSet<Auction>` in `AuctionService`. Sortarea este bazata pe `Comparable` (pret si timp)
* **Map pentru indexare**: 
    * `userCache` in `UserService` (indexare dupa email)
    * `allVehiclesGroupedByOwnerId` in `VehicleService` (grupare `List<Vehicle>` dupa proprietar)

### 2.3 - Servicii (Singleton)
1. **UserService**: Autentificare si inregistrare useri
2. **VehicleService**: Managementul stocului si incarcarea din CSV
3. **AuctionService**: Logica de licitare si sortare
4. **EngineService**: Managementul motoarelor

---

## 3. Organizare Pachete
* `com.pao.project.auction_app.models`: Entitatile de domeniu
* `com.pao.project.auction_app.services`: Logica de business (Singleton)
* `com.pao.project.auction_app.exceptions`: Tratarea erorilor custom
* `com.pao.project.auction_app.utils`: Utilitare (DataSeeder)
* `com.pao.project.auction_app.Main`: Punctul de intrare in aplicatie