package travelplanner.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import travelplanner.model.Trip;

public class TripManager {

    private List<Trip> trips = new ArrayList<>();

    private String ownerUsername;   // ✅ which user’s trips
    private File tripFile;

    // Main constructor – use this everywhere
    public TripManager(String ownerUsername) {
        this.ownerUsername = ownerUsername;

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        String safeName;
        if (ownerUsername == null || ownerUsername.trim().isEmpty()) {
            safeName = "default";
        } else {
            safeName = ownerUsername.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        }

        this.tripFile = new File(dataDir, "trips_" + safeName + ".txt");
        loadTripsFromFile();
    }

    // Fallback for old code / testing
    public TripManager() {
        this("default");
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    // Save: TripName|StartDate|EndDate|Budget|HomeCountry
    public void saveTripsToFile() {
        try {
            if (!tripFile.getParentFile().exists()) {
                tripFile.getParentFile().mkdirs();
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(tripFile, false))) {
                for (Trip t : trips) {
                    pw.println(
                        t.getTripName() + "|" +
                        t.getStartDate() + "|" +
                        t.getEndDate() + "|" +
                        t.getBudget() + "|" +
                        t.getHomeCountry()
                    );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load trips from this user's file
    private void loadTripsFromFile() {
        trips.clear();

        if (!tripFile.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(tripFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                String name  = parts[0];
                String start = parts[1];
                String end   = parts[2];

                double budget;
                try {
                    budget = Double.parseDouble(parts[3]);
                } catch (NumberFormatException ex) {
                    continue;
                }

                String homeCountry = (parts.length >= 5) ? parts[4] : "India";

                trips.add(new Trip(name, start, end, budget, homeCountry));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
