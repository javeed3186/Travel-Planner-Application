package travelplanner.model;

public class RestaurantPlace extends Place {

    public RestaurantPlace(String name, String time, double cost, String note) {
        super(name, time, cost, note);
    }

    @Override
    public String getCategory() {
        return "Restaurant";
    }
}
