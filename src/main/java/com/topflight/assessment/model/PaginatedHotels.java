package com.topflight.assessment.model;

import java.util.ArrayList;
import java.util.List;

public class PaginatedHotels {
    private int totalResults;

    private List<Hotel> hotels = new ArrayList<>();

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}
