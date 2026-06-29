package travelplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    // Destination name (city / country)
    private String tripName;
    private String startDate;
    private String endDate;
    private double budget;        // budget in TRIP currency

    // ===== Trip currency (destination) =====
    private String currencyCode;      // e.g., "JPY"
    private String currencySymbol;    // e.g., "¥"
    private double ratePerINR;        // e.g., 1 INR = 1.8 JPY → 1.8

    // ===== Home currency (user’s own country) =====
    private String homeCountry;
    private String homeCurrencyCode;     // e.g., "INR"
    private String homeCurrencySymbol;   // e.g., "₹"
    private double homeRatePerINR;       // 1 INR = X (home currency)

    // (optional, for future use)
    private List<DayPlan> days = new ArrayList<>();

    // ===== Constructors =====

    // Old style (if called) → default home = India
    public Trip(String tripName, String startDate, String endDate, double budget) {
        this(tripName, startDate, endDate, budget, "India");
    }

    // New constructor with HOME COUNTRY
    public Trip(String tripName, String startDate, String endDate, double budget, String homeCountry) {
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;

        setTripCurrencyByDestination(tripName); // destination → trip currency
        setHomeCountry(homeCountry);            // home country → home currency
    }

    // ===== Trip currency from DESTINATION text =====
    private void setTripCurrencyByDestination(String destination) {
        String d = (destination == null ? "" : destination.toLowerCase());

        if (d.contains("japan")) {
            currencyCode = "JPY";  currencySymbol = "¥";  ratePerINR = 1.8;
        } else if (d.contains("korea")) {
            currencyCode = "KRW";  currencySymbol = "₩";  ratePerINR = 16.0;
        } else if (d.contains("china")) {
            currencyCode = "CNY";  currencySymbol = "¥";  ratePerINR = 0.085;
        } else if (d.contains("usa") || d.contains("united states") || d.contains("america")) {
            currencyCode = "USD";  currencySymbol = "$";  ratePerINR = 0.012;
        } else if (d.contains("canada")) {
            currencyCode = "CAD";  currencySymbol = "$";  ratePerINR = 0.015;
        } else if (d.contains("switz") || d.contains("switzerland")) {
            currencyCode = "CHF";  currencySymbol = "CHF"; ratePerINR = 0.011;
        } else if (d.contains("france") || d.contains("paris") || d.contains("euro")) {
            currencyCode = "EUR";  currencySymbol = "€";  ratePerINR = 0.011;
        } else if (d.contains("india")) {
            currencyCode = "INR";  currencySymbol = "₹";  ratePerINR = 1.0;
        } else {
            // default
            currencyCode = "INR";  currencySymbol = "₹";  ratePerINR = 1.0;
        }
    }

    // ===== Home currency from HOME COUNTRY (dropdown) =====
    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
        setHomeCurrencyByCountry(homeCountry);
    }

    private void setHomeCurrencyByCountry(String country) {
        String c = (country == null ? "" : country.toLowerCase());

        if (c.contains("japan")) {
            homeCurrencyCode = "JPY";  homeCurrencySymbol = "¥";  homeRatePerINR = 1.8;
        } else if (c.contains("korea")) {
            homeCurrencyCode = "KRW";  homeCurrencySymbol = "₩";  homeRatePerINR = 16.0;
        } else if (c.contains("china")) {
            homeCurrencyCode = "CNY";  homeCurrencySymbol = "¥";  homeRatePerINR = 0.085;
        } else if (c.contains("usa") || c.contains("united states") || c.contains("america")) {
            homeCurrencyCode = "USD";  homeCurrencySymbol = "$";  homeRatePerINR = 0.012;
        } else if (c.contains("canada")) {
            homeCurrencyCode = "CAD";  homeCurrencySymbol = "$";  homeRatePerINR = 0.015;
        } else if (c.contains("switz") || c.contains("switzerland")) {
            homeCurrencyCode = "CHF";  homeCurrencySymbol = "CHF"; homeRatePerINR = 0.011;
        } else if (c.contains("france") || c.contains("euro")) {
            homeCurrencyCode = "EUR";  homeCurrencySymbol = "€";  homeRatePerINR = 0.011;
        } else { // default = India
            homeCurrencyCode = "INR";  homeCurrencySymbol = "₹";  homeRatePerINR = 1.0;
        }
    }

    // ===== Getters / setters =====

    public String getTripName() { return tripName; }

    public void setTripName(String name) {
        this.tripName = name;
        setTripCurrencyByDestination(name);  // if destination text changes
    }

    public String getStartDate() { return startDate; }
    public String getEndDate()   { return endDate;   }
    public double getBudget()    { return budget;    }

    public List<DayPlan> getDays() { return days; }

    // Trip currency
    public String getCurrencyCode()   { return currencyCode; }
    public String getCurrencySymbol() { return currencySymbol; }
    public double getRatePerINR()     { return ratePerINR; }

    // Home currency
    public String getHomeCountry()          { return homeCountry; }
    public String getHomeCurrencyCode()     { return homeCurrencyCode; }
    public String getHomeCurrencySymbol()   { return homeCurrencySymbol; }
    public double getHomeRatePerINR()       { return homeRatePerINR; }

    // Optional: total cost from DayPlans
    public double getTripTotalCost() {
        double total = 0;
        if (days != null) {
            for (DayPlan d : days) {
                total += d.getTotalCost();
            }
        }
        return total;
    }
}
