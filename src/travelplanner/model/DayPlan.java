package travelplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayPlan implements Serializable {

    private int dayNumber;
    private List<Place> places;

    public DayPlan(int dayNumber) {
        this.dayNumber = dayNumber;
        this.places = new ArrayList<>();
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public List<Place> getPlaces() {
        return places;
    }

    public double getTotalCost() {
        double sum = 0;
        for (Place p : places) {
            sum += p.getCost();
        }
        return sum;
    }
}
