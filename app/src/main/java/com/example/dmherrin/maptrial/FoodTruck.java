package com.example.dmherrin.maptrial;

/**
 * Created by dmherrin on 4/20/16.
 */
public class FoodTruck {

    private String truck;
    private String location;

    public FoodTruck() {}
    public FoodTruck(String t, String l) {
        truck = t; location = l;
    }

    public String getTruck()     {return truck;}
    public String getLocation() {return location;}

    public String toString() {
        return "Truck:\t" + truck + "\nLocation:\t" + location;
    }

}
