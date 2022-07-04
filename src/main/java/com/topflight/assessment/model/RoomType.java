package com.topflight.assessment.model;

public class RoomType {
    private String typeName;

    private double pricePerPerson;

    private int maxPeople;

    private int numRoomsAvailable;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getNumRoomsAvailable() {
        return numRoomsAvailable;
    }

    public void setNumRoomsAvailable(int numRoomsAvailable) {
        this.numRoomsAvailable = numRoomsAvailable;
    }
}
