package umbcs681.hw02.Car;

import java.util.List;
import java.util.Optional;


public class CarService {

    
    public Optional<Car> findMinPriceCar(List<Car> cars) {
        return cars.stream()
                   .reduce((car1, car2) -> car1.getPrice() < car2.getPrice() ? car1 : car2);
    }

   
    public Optional<Car> findMaxPriceCar(List<Car> cars) {
        return cars.stream()
                   .reduce((car1, car2) -> car1.getPrice() > car2.getPrice() ? car1 : car2);
    }

   
    public double calculateAveragePrice(List<Car> cars) {
        CarPriceSum result = cars.stream()
                                   .map(car -> new CarPriceSum(car.getPrice(), 1))
                                   .reduce(new CarPriceSum(0, 0), CarPriceSum::combine);

        return result.getCount() == 0 ? 0 : result.getSum() / result.getCount();
    }
}
