package travelplanner.manager;

import java.util.ArrayList;
import java.util.List;
import travelplanner.model.User;
import java.io.FileWriter;
import java.io.IOException;


public class UserManager {

    private List<User> users;

    public UserManager() {
    	users = new ArrayList<>();
        loadUsersFromFile(); 
        
        // TODO: later we will load existing users from file
    }

    // TODO: check if username already exists
    public boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }


    // TODO: register a new user and save to file
    public boolean registerUser(String username, String password) {
        if (isUsernameTaken(username)) return false;

        User newUser = new User(username, password);
        users.add(newUser);
        saveUsersToFile();
        return true;
    }



    // TODO: validate login
    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) &&
                user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;  // login failed
    }

    // TODO: load users from users.txt
    public void loadUsersFromFile() {
    	users.clear();

        try {
            java.io.File file = new java.io.File("data/users.txt");

            if (!file.exists()) {
                return; // no file yet → no users yet
            }

            java.util.Scanner scanner = new java.util.Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    User user = new User(username, password);
                    users.add(user);
                }
            }
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
       

    }

    // TODO: save users to users.txt
    public void saveUsersToFile() {
        try (FileWriter writer = new FileWriter("data/users.txt")) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
