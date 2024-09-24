package umbcs681.hw02.Car;

public class CarPriceSum {
    private float sum;
    private int count;

    public CarPriceSum(float sum, int count) {
        this.sum = sum;
        this.count = count;
    }

    public float getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public CarPriceSum combine(CarPriceSum other) {
        return new CarPriceSum(this.sum + other.sum, this.count + other.count);
    }
}