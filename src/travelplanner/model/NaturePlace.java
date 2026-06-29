package travelplanner.model;

public class NaturePlace extends Place {

    public NaturePlace(String name, String time, double cost, String note) {
        super(name, time, cost, note);
    }

    @Override
    public String getCategory() {
        return "Nature";
    }
}
