package umbcs681.hw02.Car;

public class Car {
    private String make, model;
    private int year;
    private float price;
    private int mileage;

    
    private Car(String make, String model, int year, float price, int mileage) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
    }

    
    public static Car createCar(String make, String model, int year, float price, int mileage) {
        return new Car(make, model, year, price, mileage);
    }

    
    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public float getPrice() {
        return price;
    }

    public int getMileage() {
        return mileage;
    }
}