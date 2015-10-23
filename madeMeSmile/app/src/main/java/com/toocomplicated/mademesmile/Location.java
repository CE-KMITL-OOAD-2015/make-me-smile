package com.toocomplicated.mademesmile;

public class Location {
    private int id;
    private String name;
    private String address;
    public Location(int id, String name, String address)
    {
        this.id = id;
        this.name = name;
        this.address = address;
    }
    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getAddress() { return address;}
}
