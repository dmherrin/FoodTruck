package com.example.dmherrin.maptrial;

/**
 * Created by dmherrin on 4/20/16.
 */
public class FoodTruck {

    private String truck;
    private String location;
    private String yelpUrl;
    private String phoneNumber;
    private String menuUrl;

    public FoodTruck() {}
    public FoodTruck(String t, String l) {
        truck = t; location = l;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }
    public String getYelpUrl()  {return yelpUrl;}
    public String getPhoneNumber()  {return phoneNumber;}

    public String getMenuUrl() {return menuUrl;}

    public String getTruck()     {return truck;}
    public String getLocation() {return location;}

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        return "Truck:\t" + truck + "\nLocation:\t" + location;
    }

}
