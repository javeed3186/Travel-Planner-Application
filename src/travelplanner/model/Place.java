package travelplanner.model;

import java.io.Serializable;

public abstract class Place implements Serializable {
    protected String name;
    protected String time;   // ex: "10:30 AM"
    protected double cost;
    protected String note;

    public Place(String name, String time, double cost, String note) {
        this.name = name;
        this.time = time;
        this.cost = cost;
        this.note = note;
    }

    public String getName() { return name; }
    public String getTime() { return time; }
    public double getCost() { return cost; }
    public String getNote() { return note; }

    // Polymorphism method
    public abstract String getCategory();

    public String getDisplayInfo() {
        return "[" + getCategory() + "] " + name + " at " + time + " | cost: " + cost;
    }
}
