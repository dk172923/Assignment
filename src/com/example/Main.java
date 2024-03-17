package com.example;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class User implements Serializable {
	private static final long serialVersionUID = -4933018237570010756L;
	private String username;
    private String password;
    private UserType type;

    public User(String username, String password, UserType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }
}

enum UserType {
    ADMIN,
    USER
}

public class Main {
    private static final String USER_DATA_FILE = "userdata.ser";
    private static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        loadUserData(); // Load user data from file

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    signup(scanner);
                    break;
                case 3:
                    saveUserData(); 
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again.");
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getUsername() + " (" + user.getType() + ")");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void signup(Scanner scanner) {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        users.put(username, new User(username, password, UserType.USER));
        System.out.println("Signup successful!");
    }

    private static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
                users = (HashMap<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
