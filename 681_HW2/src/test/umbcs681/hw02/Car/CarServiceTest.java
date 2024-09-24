package umbcs681.hw02.Car;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CarServiceTest {

    private static CarService carService;
    private static List<Car> cars;
    @BeforeAll
    public static void setup() {
        carService = new CarService();
        cars = Arrays.asList(
                Car.createCar("Toyota", "Corolla", 2019, 15000, 10000),
                Car.createCar("Honda", "Civic", 2018, 12000, 15000),
                Car.createCar("Ford", "Focus", 2020, 18000, 8000),
                Car.createCar("Chevrolet", "Malibu", 2017, 14000, 12000),
                Car.createCar("Nissan", "Altima", 2021, 17000, 9000),
                Car.createCar("Hyundai", "Elantra", 2022, 16000, 6000),
                Car.createCar("Kia", "Optima", 2016, 11000, 20000),
                Car.createCar("BMW", "3 Series", 2022, 35000, 3000)
        );
    }

    
    @Test
    public void testFindMinPriceCar() {
        Optional<Car> minPriceCar = carService.findMinPriceCar(cars);
        assertTrue(minPriceCar.isPresent());
        assertEquals(11000, minPriceCar.get().getPrice()); 
    }

   
    @Test
    public void testFindMaxPriceCar() {
        Optional<Car> maxPriceCar = carService.findMaxPriceCar(cars);
        assertTrue(maxPriceCar.isPresent());
        assertEquals(35000, maxPriceCar.get().getPrice()); 
    }

    
    @Test
    public void testCalculateAveragePrice() {
        double averagePrice = carService.calculateAveragePrice(cars);
        assertEquals(17250.0, averagePrice, 0.001);  
    }
}
