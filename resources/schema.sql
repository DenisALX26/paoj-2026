DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS engines;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DOUBLE DEFAULT 0.0  
);

CREATE TABLE engines (
    id VARCHAR(36) PRIMARY KEY,
    horsepower INT NOT NULL,
    torque INT NOT NULL,
    engine_type VARCHAR(20) NOT NULL, 
    
    fuel_type VARCHAR(20),            
    fuel_consumption DOUBLE,
    
    battery_capacity INT,
    range_km INT,
    charging_time DOUBLE,
    has_fast_charging BOOLEAN,
    
    thermal_engine_id VARCHAR(36),
    electric_engine_id VARCHAR(36),
    
    FOREIGN KEY (thermal_engine_id) REFERENCES engines(id),
    FOREIGN KEY (electric_engine_id) REFERENCES engines(id)
);

CREATE TABLE vehicles (
    id VARCHAR(36) PRIMARY KEY,
    manufacturer VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    production_year INT NOT NULL,
    price INT NOT NULL,
    mileage INT NOT NULL,
    engine_id VARCHAR(36) NOT NULL,
    is_sellable BOOLEAN DEFAULT TRUE,
    
    vehicle_category VARCHAR(20) NOT NULL,
    
    car_type VARCHAR(20),              
    number_of_doors INT,
    body_type VARCHAR(20),               
    drive_type VARCHAR(20),                
    color VARCHAR(30),
    
    motorcycle_type VARCHAR(20),       
    engine_capacity DOUBLE,
    weight DOUBLE,
    has_abs BOOLEAN,
    is_a2_compatible BOOLEAN,
    number_of_cylinders INT,
    
    is_street_fighter BOOLEAN,
    riding_modes VARCHAR(255),            
    headlight_type VARCHAR(20),           
    
    has_cornering_abs BOOLEAN,
    has_quick_shifter BOOLEAN,
    
    FOREIGN KEY (engine_id) REFERENCES engines(id)
);

CREATE TABLE auctions (
    id VARCHAR(36) PRIMARY KEY,
    vehicle_id VARCHAR(36) NOT NULL,
    seller_id VARCHAR(36) NOT NULL,
    starting_price DOUBLE NOT NULL,
    current_price DOUBLE NOT NULL,
    end_time DATETIME NOT NULL,
    auction_type VARCHAR(20) NOT NULL,   
    buy_now_price DOUBLE,  
    
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE bids (
    id INT AUTO_INCREMENT PRIMARY KEY,
    auction_id VARCHAR(36) NOT NULL,
    bidder_id VARCHAR(36) NOT NULL,
    amount DOUBLE NOT NULL,
    bid_timestamp DATETIME NOT NULL,
    
    FOREIGN KEY (auction_id) REFERENCES auctions(id),
    FOREIGN KEY (bidder_id) REFERENCES users(id)
);