package travelplanner.model;

public class ShoppingPlace extends Place {

    public ShoppingPlace(String name, String time, double cost, String note) {
        super(name, time, cost, note);
    }

    @Override
    public String getCategory() {
        return "Shopping";
    }
}
