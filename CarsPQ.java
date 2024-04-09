
import java.util.Comparator;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;

// https://algs4.cs.princeton.edu/34hash/LinearProbingHashST.java.html
// https://algs4.cs.princeton.edu/24pq/MinPQ.java.html
public class CarsPQ implements CarsPQ_Inter {
    private DLBMap<Car> carsByVin;
    private IndexMinPQ<Car> carsByPrice;
    private IndexMinPQ<Car> carsByMileage;
    private DLBMap<IndexMinPQ<Car>> carsMakeModelByPrice;
    private DLBMap<IndexMinPQ<Car>> carsMakeModelByMileage;

    private int cnt = 0;

    public CarsPQ() {
        carsByVin = new DLBMap<>();
        carsByPrice = new IndexMinPQ<Car>(512, Comparator.comparingInt(Car::getPrice));
        carsByMileage = new IndexMinPQ<Car>(512, Comparator.comparingInt(Car::getMileage));
        carsMakeModelByPrice = new DLBMap<>();
        carsMakeModelByMileage = new DLBMap<>();
    }

    public CarsPQ(String filename) {
        this(); // Call the default constructor to initialize the data structures

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            // Skip the first line (header)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read the remaining lines
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");

                String vin = parts[0];
                String make = parts[1];
                String model = parts[2];
                int price = Integer.parseInt(parts[3]);
                int mileage = Integer.parseInt(parts[4]);
                String color = parts[5];

                Car car = new Car(vin, make, model, price, mileage, color);
                car.index = cnt;
                add(car);
            }

            scanner.close();
        } catch (FileNotFoundException e) {

        }
    }

    public void add(Car c) {
        carsByVin.put(c.getVIN(), c);
        cnt = c.index;
        carsByPrice.insert(cnt,c);
        carsByMileage.insert(cnt, c);
        String key = c.getMake() + ":" + c.getModel();
        IndexMinPQ<Car> tempPricePQ = carsMakeModelByPrice.get(key);
        if( tempPricePQ == null){
            tempPricePQ = new IndexMinPQ<>(512, Comparator.comparingInt(Car::getPrice));
        }
        tempPricePQ.insert(cnt, c);
        carsMakeModelByPrice.put(key,tempPricePQ);

        IndexMinPQ<Car> tempMileagePQ = carsMakeModelByMileage.get(key);
        if( tempMileagePQ == null){
            tempMileagePQ = new IndexMinPQ<>(512, Comparator.comparingInt(Car::getMileage));
        }
        tempMileagePQ.insert(cnt, c);
        carsMakeModelByMileage.put(key,tempMileagePQ);
        cnt++;
    }

    public Car get(String vin){
        Car car =  carsByVin.get(vin);
        if(car == null){
            throw new NoSuchElementException();
        }
        return car;
    }

    public void updatePrice(String vin, int newPrice) {
        Car car = carsByVin.get(vin);
        String key = car.getMake() +":" +car.getModel();
        int index = car.index;
        car.setPrice(newPrice);
        carsByPrice.changeKey(index,car);
        IndexMinPQ<Car> temp = carsMakeModelByPrice.get(key);
        temp.changeKey(index, car);
    }

    public void updateMileage(String vin, int newMileage) {
        Car car = carsByVin.get(vin);
        String key = car.getMake() +":" +car.getModel();
        int index = car.index;
        car.setMileage(newMileage);
        carsByMileage.changeKey(index,car);
        IndexMinPQ<Car> temp = carsMakeModelByMileage.get(key);
        temp.changeKey(index, car);
    }

    public void updateColor(String vin, String newColor) {
        Car car = carsByVin.get(vin);
        if (car != null) {
            car.setColor(newColor);
        }
    }

    public void remove(String vin) {
        Car car = carsByVin.get(vin);
        carsByVin.remove(vin);
        carsByPrice.delete(car.index);
        carsByMileage.delete(car.index);
        String key = car.getMake() +":" +car.getModel();
        IndexMinPQ<Car> temp = carsMakeModelByMileage.get(key);
        temp.delete(car.index);
        temp = carsMakeModelByPrice.get(key);
        temp.delete(car.index);
    }

    public Car getLowPrice() {
        return carsByPrice.minKey();
    }

    public Car getLowPrice(String make, String model) {
        String key = make +":" +model;
        IndexMinPQ<Car> tempPQ = carsMakeModelByPrice.get(key);
        return tempPQ.minKey();
    }

    public Car getLowMileage() {
        return carsByMileage.minKey();
    }

    public Car getLowMileage(String make, String model) {
        String key = make +":" +model;
        IndexMinPQ<Car> tempPQ = carsMakeModelByMileage.get(key);
        return tempPQ.minKey();
    }
}
