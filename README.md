# Travel Planner Application

A Java Swing-based travel planning desktop application that helps users register, log in, create trips, and organize day-by-day plans.

## Features

- User registration and login
- Dashboard for quick navigation
- Create and save travel trips
- View saved trips
- Plan activities by day
- Store user and trip data locally in text files

## Project Structure

```text
src/
  travelplanner/
    manager/
      TripManager.java
      UserManager.java
    model/
      DayPlan.java
      MuseumPlace.java
      NaturePlace.java
      Place.java
      RestaurantPlace.java
      ShoppingPlace.java
      Trip.java
      User.java
    ui/
      CreateTripUI.java
      DashboardUI.java
      DayPlanUI.java
      LoginUI.java
      RegisterUI.java
      ViewTripsUI.java

data/
  users.txt
```

## Technologies Used

- Java
- Swing (GUI)
- Java standard I/O for local data storage

## Prerequisites

- JDK 8 or newer
- An IDE such as Eclipse, IntelliJ IDEA, or VS Code with Java support

## How to Run

### Option 1: In an IDE

1. Open the project in your Java IDE.
2. Make sure the project is recognized as a Java project.
3. Run the main class:
   - `travelplanner.ui.LoginUI`

### Option 2: From the terminal

Compile the source files and run the application:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin travelplanner.ui.LoginUI
```

> If you are using Windows PowerShell, you can run the app from your IDE for the simplest experience.

## Usage

1. Launch the application.
2. Register a new account or log in with an existing one.
3. Use the dashboard to create a new trip or view saved trips.
4. Add day plans and organize your travel itinerary.

## Data Storage

The application stores user and trip information in the `data` folder as local text files.

## Notes

- Some UI images may be missing from the resources folder; the app will still run with a fallback background.
- This project is intended as a desktop Java learning/demo application.
