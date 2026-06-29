package travelplanner.model;

public class MuseumPlace extends Place {

    public MuseumPlace(String name, String time, double cost, String note) {
        super(name, time, cost, note);
    }

    @Override
    public String getCategory() {
        return "Museum";
    }
}
