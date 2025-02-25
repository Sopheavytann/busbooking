package busbooking;

import java.util.Scanner;

public class busbooking {

    // Admin password (for demonstration purposes)
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Welcome message
        System.out.println("Welcome to the Bus Booking System!");
        System.out.println("Are you an Admin or a User?");
        System.out.print("Enter 'A' for Admin or 'U' for User: ");
        String role = sc.nextLine();

        if (role.equalsIgnoreCase("A")) {
            // Admin login
            if (adminLogin(sc)) {
                adminFeatures(sc);
            } else {
                System.out.println("Too many incorrect attempts. Exiting the program.");
            }
        } else if (role.equalsIgnoreCase("U")) {
            // User features
            userFeatures(sc);
        } else {
            System.out.println("Invalid input. Exiting the program.");
        }

        sc.close();
    }

    // Admin login with password verification
    private static boolean adminLogin(Scanner sc) {
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("Enter admin password: ");
            String password = sc.nextLine();
            if (password.equals(ADMIN_PASSWORD)) {
                System.out.println("Login successful!");
                return true;
            } else {
                attempts--;
                System.out.println("Incorrect password. " + attempts + " attempts remaining.");
            }
        }
        return false;
    }

    // Admin features
    private static void adminFeatures(Scanner sc) {
        while (true) {
            System.out.println("\nAdmin Features:");
            System.out.println("1. Add new bus details");
            System.out.println("2. Manage ticket prices");
            System.out.println("3. Cancel bookings");
            System.out.println("4. View history");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("Adding new bus details...");
                    // Add logic for adding new bus details
                    break;
                case 2:
                    System.out.println("Managing ticket prices...");
                    // Add logic for managing ticket prices
                    break;
                case 3:
                    System.out.println("Canceling bookings...");
                    // Add logic for canceling bookings
                    break;
                case 4:
                    System.out.println("Viewing history...");
                    // Add logic for viewing history
                    break;
                case 5:
                    System.out.println("Exiting admin features.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // User features
    private static void userFeatures(Scanner sc) {
        while (true) {
            System.out.println("\nUser Features:");
            System.out.println("1. Search buses by route");
            System.out.println("2. Book tickets");
            System.out.println("3. View booking history");
            System.out.println("4. Cancel bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("Searching buses by route...");
                    // Add logic for searching buses by route
                    break;
                case 2:
                    bookTickets(sc);
                    break;
                case 3:
                    System.out.println("Viewing booking history...");
                    // Add logic for viewing booking history
                    break;
                case 4:
                    System.out.println("Canceling bookings...");
                    // Add logic for canceling bookings
                    break;
                case 5:
                    System.out.println("Exiting user features.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Book tickets (user feature)
    private static void bookTickets(Scanner sc) {
        System.out.print("Enter the number of seats on the bus (25-45): ");
        int number = sc.nextInt();
        sc.nextLine(); // Consume the newline character

        if (number < 25 || number > 45) {
            System.out.println("Invalid number of seats. Please try again.");
            return;
        }

        int[] seatStates = new int[number]; // 0 = available, 1 = booked
        int availableCount = number;
        int unavailableCount = 0;

        while (true) {
            // Display the current state of the seats
            System.out.println("\nBus Seat Status:");
            for (int i = 0; i < number; i++) {
                if (seatStates[i] == 0) {
                    System.out.print("[+]" + (i + 1) + "\t");
                } else {
                    System.out.print("[-]" + (i + 1) + "\t");
                }
                if ((i + 1) % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("Available seats: " + availableCount);
            System.out.println("Booked seats: " + unavailableCount);

            // Ask if the user wants to book a seat
            System.out.print("Do you want to book a seat (Y/N)? ");
            String yn = sc.nextLine();
            if (yn.equalsIgnoreCase("Y")) {
                System.out.print("Enter the seat number to book: ");
                if (sc.hasNextInt()) {
                    int chairNumber = sc.nextInt();
                    sc.nextLine(); // Consume the newline character
                    if (chairNumber >= 1 && chairNumber <= number) {
                        if (seatStates[chairNumber - 1] == 0) {
                            seatStates[chairNumber - 1] = 1;
                            availableCount--;
                            unavailableCount++;
                            System.out.println("Seat number " + chairNumber + " booked successfully!");
                        } else {
                            System.out.println("Seat number " + chairNumber + " is already booked!");
                        }
                    } else {
                        System.out.println("Invalid seat number! Please try again.");
                    }
                } else {
                    sc.nextLine(); // Consume the invalid input
                    System.out.println("Invalid input! Please enter a valid seat number.");
                }
            } else if (yn.equalsIgnoreCase("N")) {
                System.out.println("Thank you! Returning to user features.");
                break;
            } else {
                System.out.println("Invalid input! Please enter 'Y' or 'N'.");
            }
        }
    }
}