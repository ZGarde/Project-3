

public class Car implements Car_Inter {
    private String vin;
    private String make;
    private String model;
    private int price;
    private int mileage;
    private String color;
    public int index;

    public Car(String vin, String make, String model, int price, int mileage, String color) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.price = price;
        this.mileage = mileage;
        this.color = color;
    }

    // Implement the getter methods from the Car_Inter interface
    @Override
    public String getVIN() {
        return this.vin;
    }

    @Override
    public String getMake() {
        return this.make;
    }

    @Override
    public String getModel() {
        return this.model;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public int getMileage() {
        return this.mileage;
    }

    @Override
    public String getColor() {
        return this.color;
    }


    // Implement the setter methods from the Car_Inter interface
    @Override
    public void setPrice(int newPrice) {
        this.price = newPrice;
    }

    @Override
    public void setMileage(int newMileage) {
        this.mileage = newMileage;
    }

    @Override
    public void setColor(String newColor) {
        this.color = newColor;
    }
}
