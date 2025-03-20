import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;


class Bus {
    int busID;
    String busNumber;
    String route;
    int capacity;
    double ticketPrice;
    String departureDate;
    String departureTime;
    int driverID;
    String driverName;

    public Bus(String busNumber, String route, int capacity, double ticketPrice,
               String departureDate, String departureTime, int driverID, String driverName) {
        this.busID = generateBusID(); // Auto-generate Bus ID
        this.busNumber = busNumber;
        this.route = route;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.driverID = driverID;
        this.driverName = driverName;
    }

    private static int generateBusID() {
        return new Random().nextInt(9000) + 1000; // Generate random 4-digit ID
    }

    public void updateTicketPrice(double price) {
        this.ticketPrice = price;
    }

    public void displayBusInfo() {
        System.out.printf("Bus ID: %-5d | Number: %-5s | Route: %-20s | Capacity: %-3d | Ticket Price: $%-6.2f | " +
                          "Departure: %s at %s | Driver ID: %-5d | Driver Name: %s%n",
                busID, busNumber, route, capacity, ticketPrice, departureDate, departureTime, driverID, driverName);
    }

    public String toCSV() {
        return busID + "," + busNumber + "," + route + "," + capacity + "," +
               ticketPrice + "," + departureDate + "," + departureTime + "," +
               driverID + "," + driverName;
    }

    public static Bus fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 9) {
            return new Bus(
                parts[1],                      // busNumber
                parts[2],                      // route
                Integer.parseInt(parts[3]),    // capacity
                Double.parseDouble(parts[4]),  // ticketPrice
                parts[5],                      // departureDate
                parts[6],                      // departureTime
                Integer.parseInt(parts[7]),    // driverID
                parts[8]                       // driverName
            );
        }
        return null;
    }
}

class Booking {
    private static int nextBookingID = 1; // Static counter for sequential IDs
    int bookingID;
    int busID;
    int userID;
    int seatsBooked;
    double totalPrice;
    String status;
    String departureDate;
    String departureTime;

    public Booking(int busID, int userID, int seatsBooked, double totalPrice,
                  String status, String departureDate, String departureTime) {
        this.bookingID = generateBookingID(); // Auto-generate Booking ID
        this.busID = busID;
        this.userID = userID;
        this.seatsBooked = seatsBooked;
        this.totalPrice = totalPrice;
        this.status = status;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
    }

    private static int generateBookingID() {
        return nextBookingID++; // Increment the counter after assigning
    }

    public void displayBookingInfo() {
        System.out.printf("Booking ID: %02d | Bus ID: %-5d | User ID: %-5d | Seats: %-3d | Total Price: $%-6.2f | " +
                          "Status: %-10s | Departure: %s at %s%n",
                bookingID, busID, userID, seatsBooked, totalPrice, status, departureDate, departureTime);
    }

    public String toCSV() {
        return bookingID + "," + busID + "," + userID + "," + seatsBooked + "," +
               totalPrice + "," + status + "," + departureDate + "," + departureTime;
    }

    public static Booking fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 8) {
            int loadedID = Integer.parseInt(parts[0]);
            if (loadedID >= nextBookingID) {
                nextBookingID = loadedID + 1; // Update the counter if necessary
            }
            return new Booking(
                Integer.parseInt(parts[1]),    // busID
                Integer.parseInt(parts[2]),    // userID
                Integer.parseInt(parts[3]),    // seatsBooked
                Double.parseDouble(parts[4]),  // totalPrice
                parts[5],                      // status
                parts[6],                      // departureDate
                parts[7]                       // departureTime
            );
        }
        return null;
    }
}

class User {
    int userID;
    String name;
    String email;
    String phoneNumber;
    String password;
    List<Booking> bookingHistory = new ArrayList<>();

    public User(String name, String email, String phoneNumber, String password) {
        this.userID = generateUserID(); // Auto-generate User ID
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    private static int generateUserID() {
        return new Random().nextInt(9000) + 1000; // Generate random 4-digit ID
    }

    public List<Bus> searchBus(String route, List<Bus> buses) {
        List<Bus> result = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.route.equalsIgnoreCase(route)) {
                result.add(bus);
            }
        }
        return result;
    }

    public List<Bus> searchBusByDate(String date, List<Bus> buses) {
        List<Bus> result = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.departureDate.equals(date)) {
                result.add(bus);
            }
        }
        return result;
    }

    public Booking bookTicket(int busID, int seats, List<Bus> buses, List<Booking> bookings) {
        for (Bus bus : buses) {
            if (bus.busID == busID && bus.capacity >= seats) {
                bus.capacity -= seats;
                Booking booking = new Booking(
                    busID, 
                    this.userID, 
                    seats, 
                    bus.ticketPrice * seats, 
                    "Confirmed",
                    bus.departureDate,
                    bus.departureTime
                );
                bookingHistory.add(booking);
                bookings.add(booking);
                return booking;
            }
        }
        return null;
    }

    public void viewBookingHistory() {
        if (bookingHistory.isEmpty()) {
            System.out.println("No booking history found.");
        } else {
            System.out.println("Your Booking History:");
            for (Booking booking : bookingHistory) {
                booking.displayBookingInfo();
            }
        }
    }

    public String toCSV() {
        return userID + "," + name + "," + email + "," + phoneNumber + "," + password;
    }

    public static User fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) {
            User user = new User(parts[1], parts[2], parts[3], parts[4]);
            user.userID = Integer.parseInt(parts[0]);
            return user;
        }
        return null;
    }
}

class Admin {
    int userID;
    String name;
    String email;
    String phoneNumber;
    String password;

    public Admin(String name, String email, String phoneNumber, String password) {
        this.userID = generateAdminID(); // Auto-generate Admin ID
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    private static int generateAdminID() {
        return new Random().nextInt(9000) + 1000; // Generate random 4-digit ID
    }

    public void addNewBus(List<Bus> buses, Bus bus) {
        buses.add(bus);
        System.out.println("Bus added successfully.");
    }

    public Bus searchBusById(List<Bus> buses, int busID) {
        for (Bus bus : buses) {
            if (bus.busID == busID) {
                return bus;
            }
        }
        return null;
    }

    public void editBus(List<Bus> buses, int busID, String newRoute, int newCapacity,
                        double newPrice, String newDate, String newTime,
                        int newDriverID, String newDriverName) {
        Bus bus = searchBusById(buses, busID);
        if (bus != null) {
            bus.route = newRoute;
            bus.capacity = newCapacity;
            bus.ticketPrice = newPrice;
            bus.departureDate = newDate;
            bus.departureTime = newTime;
            bus.driverID = newDriverID;
            bus.driverName = newDriverName;
            System.out.println("Bus details updated successfully.");
        } else {
            System.out.println("Bus not found.");
        }
    }

    public void removeBus(List<Bus> buses, int busID) {
        Bus bus = searchBusById(buses, busID);
        if (bus != null) {
            buses.remove(bus);
            System.out.println("Bus removed successfully.");
        } else {
            System.out.println("Bus not found.");
        }
    }

    public Booking searchBookingById(List<Booking> bookings, int bookingID) {
        for (Booking booking : bookings) {
            if (booking.bookingID == bookingID) {
                return booking;
            }
        }
        return null;
    }

    public void cancelBooking(List<Booking> bookings, int bookingID) {
        Booking booking = searchBookingById(bookings, bookingID);
        if (booking != null) {
            bookings.remove(booking);
            System.out.println("Booking canceled successfully.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void viewAllBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            System.out.println("All Booking History:");
            for (Booking booking : bookings) {
                booking.displayBookingInfo();
            }
        }
    }

    public void viewAllUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.println("\nAll User Information:");
        System.out.println("=====================================================================================================");
        System.out.printf("%-5s | %-15s | %-25s | %-15s | %-10s%n", "ID", "Name", "Email", "Phone", "Password");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-5d | %-15s | %-25s | %-15s | %-10s%n",
                    user.userID, user.name, user.email, user.phoneNumber, user.password);
        }
        System.out.println("=====================================================================================================");
    }

    public void editUser(List<User> users, int userID, String newName, String newEmail, String newPhone, String newPassword) {
        User user = findUserById(users, userID);
        if (user != null) {
            user.name = newName;
            user.email = newEmail;
            user.phoneNumber = newPhone;
            user.password = newPassword;
            System.out.println("User details updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    public void removeUser(List<User> users, int userID) {
        User user = findUserById(users, userID);
        if (user != null) {
            users.remove(user);
            System.out.println("User removed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    public User findUserById(List<User> users, int userID) {
        for (User user : users) {
            if (user.userID == userID) {
                return user;
            }
        }
        return null;
    }

    public double calculateTotalRevenue(List<Booking> bookings, String startDate, String endDate, Integer busID) {
        double totalRevenue = 0.0;
        for (Booking booking : bookings) {
            // Check if the booking is confirmed
            if ("Confirmed".equalsIgnoreCase(booking.status)) {
                // Apply filters if provided
                boolean withinDateRange = (startDate == null || booking.departureDate.compareTo(startDate) >= 0) &&
                                          (endDate == null || booking.departureDate.compareTo(endDate) <= 0);
                boolean matchesBusID = (busID == null || booking.busID == busID);
    
                if (withinDateRange && matchesBusID) {
                    totalRevenue += booking.totalPrice;
                }
            }
        }
        return totalRevenue;
    }
}

class FileManager {
    private static final String BUS_CSV_FILE = "Businfo.csv";
    private static final String BUS_CSV_HEADER = "BusID,BusNumber,Route,Capacity,TicketPrice,DepartureDate,DepartureTime,DriverID,DriverName";

    public static void saveBusesToCSV(List<Bus> buses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BUS_CSV_FILE))) {
            writer.println(BUS_CSV_HEADER);
            for (Bus bus : buses) {
                writer.println(bus.toCSV());
            }
            System.out.println("Bus data saved to " + BUS_CSV_FILE + " successfully!");
        } catch (IOException e) {
            System.out.println("Error saving to CSV file: " + e.getMessage());
        }
    }

    public static List<Bus> loadBusesFromCSV() {
        List<Bus> buses = new ArrayList<>();
        File file = new File(BUS_CSV_FILE);
        if (!file.exists()) {
            System.out.println("CSV file not found. Starting with empty bus list.");
            return buses;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Bus bus = Bus.fromCSV(line);
                if (bus != null) {
                    buses.add(bus);
                }
            }
            System.out.println("Loaded " + buses.size() + " buses from " + BUS_CSV_FILE);
        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
        }
        return buses;
    }
}

class BookingFileManager {
    private static final String BOOKING_CSV_FILE = "Bookinginfo.csv";
    private static final String BOOKING_CSV_HEADER = "BookingID,BusID,UserID,SeatsBooked,TotalPrice,Status,DepartureDate,DepartureTime";

    public static void saveBookingsToCSV(List<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKING_CSV_FILE))) {
            writer.println(BOOKING_CSV_HEADER);
            for (Booking booking : bookings) {
                writer.println(booking.toCSV());
            }
            System.out.println("Booking data saved to " + BOOKING_CSV_FILE + " successfully!");
        } catch (IOException e) {
            System.out.println("Error saving to CSV file: " + e.getMessage());
        }
    }

    public static List<Booking> loadBookingsFromCSV() {
        List<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKING_CSV_FILE);
        if (!file.exists()) {
            System.out.println("CSV file not found. Starting with empty booking list.");
            return bookings;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Booking booking = Booking.fromCSV(line);
                if (booking != null) {
                    bookings.add(booking);
                }
            }
            System.out.println("Loaded " + bookings.size() + " bookings from " + BOOKING_CSV_FILE);
        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
        }
        return bookings;
    }
}

class UserFileManager {
    private static final String USER_CSV_FILE = "UserInfo.csv";
    private static final String USER_CSV_HEADER = "UserID,Name,Email,PhoneNumber,Password";

    public static void saveUsersToCSV(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_CSV_FILE))) {
            writer.println(USER_CSV_HEADER);
            for (User user : users) {
                writer.println(user.toCSV());
            }
            System.out.println("User data saved to " + USER_CSV_FILE + " successfully!");
        } catch (IOException e) {
            System.out.println("Error saving to CSV file: " + e.getMessage());
        }
    }

    public static List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_CSV_FILE);
        if (!file.exists()) {
            System.out.println("CSV file not found. Starting with empty user list.");
            return users;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                User user = User.fromCSV(line);
                if (user != null) {
                    users.add(user);
                }
            }
            System.out.println("Loaded " + users.size() + " users from " + USER_CSV_FILE);
        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
        }
        return users;
    }
}

public class BusBookingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Bus> buses = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    static {
        buses = FileManager.loadBusesFromCSV();
        bookings = BookingFileManager.loadBookingsFromCSV();
        users = UserFileManager.loadUsersFromCSV();

        if (buses.isEmpty()) {
            buses.add(new Bus("B101", "New York-Boston", 50, 45.0, "01/03/2025", "08:00", 501, "John Smith"));
            buses.add(new Bus("B102", "Boston-Chicago", 45, 60.0, "01/03/2025", "10:30", 502, "Mary Johnson"));
            buses.add(new Bus("B103", "Chicago-Detroit", 40, 50.0, "02/03/2025", "09:15", 503, "Robert Davis"));
            FileManager.saveBusesToCSV(buses);
        }

        if (users.isEmpty()) {
            users.add(new User("John Doe", "john@example.com", "9876543210", "password123"));
            UserFileManager.saveUsersToCSV(users);
        }
    }

    public static void main(String[] args) {
        displayMainMenu();
    }

    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("Welcome to the Bus Booking System!");
            System.out.println("\nAre you an admin or user?: \n1. Admin \n2. User \n3. Exit");
            int roleChoice = getIntInput("Enter your role: ");
            if (roleChoice == 1) {
                handleAdminFlow();
            } else if (roleChoice == 2) {
                displayUserMenu();
            } else if (roleChoice == 3) {
                System.out.println("Thank you for using Bus Booking System. Goodbye!");
                UserFileManager.saveUsersToCSV(users);
                FileManager.saveBusesToCSV(buses);
                BookingFileManager.saveBookingsToCSV(bookings);
                scanner.close();
                return;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAdminFlow() {
        boolean isCorrect = false;
        Set<String> validPasswords = new HashSet<>(Arrays.asList("pheavy", "lyvann", "sreynin"));
        while (!isCorrect) {
            System.out.print("Enter Admin Password: ");
            String password = scanner.next();
            scanner.nextLine(); // Consume newline
            if (validPasswords.contains(password)) {
                isCorrect = true;
                Admin admin = new Admin("Admin", "admin@example.com", "1234567890", password);
                displayAdminMenu(admin);
            } else {
                System.out.println("Incorrect password! Try again or press 0 to go back to the main menu.");
                int choice = getIntInput("Enter your choice: ");
                if (choice == 0) {
                    return;
                }
            }
        }
    }

    private static void displayAdminMenu(Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Bus\n" +
                               "2. Edit Bus\n" +
                               "3. Remove Bus\n" +
                               "4. View All Buses\n" +
                               "5. Cancel Booking\n" +
                               "6. View All Bookings\n" +
                               "7. Edit User\n" +
                               "8. Remove User\n" +
                               "9. View All Users\n" +
                               "10. View Revenue\n" + // New option
                               "11. Exit to Main Menu");
            int adminChoice = getIntInput("Enter your choice: ");
            switch (adminChoice) {
                case 1:
                    handleAddBus(admin);
                    FileManager.saveBusesToCSV(buses);
                    break;
                case 2:
                    handleEditBus(admin);
                    FileManager.saveBusesToCSV(buses);
                    break;
                case 3:
                    handleRemoveBus(admin);
                    FileManager.saveBusesToCSV(buses);
                    break;
                case 4:
                    showAllBuses();
                    break;
                case 5:
                    handleCancelBooking(admin);
                    BookingFileManager.saveBookingsToCSV(bookings);
                    break;
                case 6:
                    admin.viewAllBookings(bookings);
                    break;
                case 7:
                    handleEditUser(admin);
                    UserFileManager.saveUsersToCSV(users);
                    break;
                case 8:
                    handleRemoveUser(admin);
                    UserFileManager.saveUsersToCSV(users);
                    break;
                case 9:
                    admin.viewAllUsers(users);
                    break;
                case 10: // Handle Revenue Calculation
                    handleViewRevenue(admin);
                    break;
                case 11:
                    FileManager.saveBusesToCSV(buses);
                    UserFileManager.saveUsersToCSV(users);
                    BookingFileManager.saveBookingsToCSV(bookings);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleEditUser(Admin admin) {
        int userID = getIntInput("\nEnter User ID to Edit: ");
        User user = admin.findUserById(users, userID);
        if (user != null) {
            System.out.println("Current User Details:");
            System.out.println("Name: " + user.name + ", Email: " + user.email + ", Phone: " + user.phoneNumber);
            System.out.print("Enter New Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter New Email: ");
            String newEmail = scanner.nextLine();
            System.out.print("Enter New Phone Number: ");
            String newPhone = scanner.nextLine();
            System.out.print("Enter New Password: ");
            String newPassword = scanner.nextLine();
            admin.editUser(users, userID, newName, newEmail, newPhone, newPassword);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void handleRemoveUser(Admin admin) {
        int userID = getIntInput("\nEnter User ID to Remove: ");
        admin.removeUser(users, userID);
    }

    private static void handleAddBus(Admin admin) {
        System.out.println("\nEnter Bus Details:");
        System.out.print("Bus Number: ");
        String number = scanner.next();
        scanner.nextLine(); // Consume newline
        System.out.print("Route: ");
        String route = scanner.nextLine();
        int capacity = getIntInput("Capacity: ");
        double price = getDoubleInput("Ticket Price: $");
        String departureDate = getValidatedDate("Departure Date (DD/MM/YYYY): ");
        String departureTime = getValidatedTime("Departure Time (HH:MM): ");
        int driverID = getIntInput("Driver ID: ");
        System.out.print("Driver Name: ");
        String driverName = scanner.nextLine();
        admin.addNewBus(buses, new Bus(number, route, capacity, price, departureDate, departureTime, driverID, driverName));
    }

    private static void handleEditBus(Admin admin) {
        int busID = getIntInput("\nEnter Bus ID to Edit: ");
        Bus bus = admin.searchBusById(buses, busID);
        if (bus != null) {
            bus.displayBusInfo();
            System.out.print("Enter New Route: ");
            String route = scanner.nextLine();
            int capacity = getIntInput("Enter New Capacity: ");
            double price = getDoubleInput("Enter New Ticket Price: $");
            String departureDate = getValidatedDate("New Departure Date (DD/MM/YYYY): ");
            String departureTime = getValidatedTime("New Departure Time (HH:MM): ");
            int driverID = getIntInput("New Driver ID: ");
            System.out.print("New Driver Name: ");
            String driverName = scanner.nextLine();
            admin.editBus(buses, busID, route, capacity, price, departureDate, departureTime, driverID, driverName);
        } else {
            System.out.println("Bus not found.");
        }
    }

    private static void handleRemoveBus(Admin admin) {
        int busID = getIntInput("\nEnter Bus ID to Remove: ");
        admin.removeBus(buses, busID);
    }

    private static void handleCancelBooking(Admin admin) {
        int bookingID = getIntInput("\nEnter Booking ID to Cancel: ");
        admin.cancelBooking(bookings, bookingID);
    }

    private static void displayUserMenu() {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Search Bus by Route\n2. Search Bus by Date\n3. View All Available Buses\n" +
                               "4. Book Ticket\n5. View Booking History\n6. Cancel Booking\n7. Exit to Main Menu");
            int userChoice = getIntInput("Enter your choice: ");
            switch (userChoice) {
                case 1:
                    handleSearchBusByRoute();
                    break;
                case 2:
                    handleSearchBusByDate();
                    break;
                case 3:
                    showAllBuses();
                    break;
                case 4:
                    handleDirectBookingProcess();
                    FileManager.saveBusesToCSV(buses);
                    BookingFileManager.saveBookingsToCSV(bookings);
                    break;
                case 5:
                    handleViewBookingHistory();
                    break;
                case 6:
                    handleUserCancelBooking();
                    BookingFileManager.saveBookingsToCSV(bookings);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleSearchBusByDate() {
        String searchDate = getValidatedDate("\nEnter Date to Search (DD/MM/YYYY): ");
        List<Bus> foundBuses = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.departureDate.equals(searchDate)) {
                foundBuses.add(bus);
            }
        }
        if (foundBuses.isEmpty()) {
            System.out.println("No buses found on this date.");
        } else {
            System.out.println("\nBuses available on date '" + searchDate + "':");
            for (Bus bus : foundBuses) {
                bus.displayBusInfo();
            }
        }
    }

    private static void showAllBuses() {
        if (buses.isEmpty()) {
            System.out.println("\nNo buses available in the system.");
            return;
        }
        System.out.println("\nAll Available Buses:");
        System.out.println("===================================================================================================================");
        System.out.printf("%-5s | %-8s | %-20s | %-8s | %-10s | %-15s | %-8s | %-8s | %-15s%n",
                "ID", "Number", "Route", "Capacity", "Price", "Departure Date", "Time", "Driver ID", "Driver Name");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        for (Bus bus : buses) {
            System.out.printf("%-5d | %-8s | %-20s | %-8d | $%-8.2f | %-15s | %-8s | %-8d | %-15s%n",
                    bus.busID, bus.busNumber, bus.route, bus.capacity, bus.ticketPrice,
                    bus.departureDate, bus.departureTime, bus.driverID, bus.driverName);
        }
        System.out.println("===================================================================================================================");
    }

    private static void handleSearchBusByRoute() {
        System.out.print("\nEnter Route to Search (e.g., 'City1-City2'): ");
        String route = scanner.nextLine();
        List<Bus> foundBuses = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.route.equalsIgnoreCase(route)) {
                foundBuses.add(bus);
            }
        }
        if (foundBuses.isEmpty()) {
            System.out.println("No buses found on this route.");
        } else {
            System.out.println("\nBuses available on route '" + route + "':");
            for (Bus bus : foundBuses) {
                bus.displayBusInfo();
            }
        }
    }

    private static void handleDirectBookingProcess() {
        System.out.println("\nAre you an existing user or a new user?");
        System.out.println("1. Existing User\n2. New User\n3. Back to User Menu");
        int choice = getIntInput("Enter your answer: ");
        User currentUser = null;
        if (choice == 1) {
            int userID = getIntInput("Enter User ID: ");
            currentUser = findUserById(userID);
            if (currentUser == null) {
                System.out.println("User not found. Would you like to register as a new user?");
                System.out.println("1. Yes\n2. No, go back to User Menu");
                int registerChoice = getIntInput("Enter your choice: ");
                if (registerChoice == 1) {
                    currentUser = registerNewUser();
                } else {
                    return;
                }
            }
        } else if (choice == 2) {
            currentUser = registerNewUser();
        } else {
            return;
        }
    
        // Verify password before proceeding
        if (currentUser != null) {
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            if (!currentUser.validatePassword(password)) {
                System.out.println("Incorrect password. Booking process canceled.");
                return;
            }
            bookTicketForUser(currentUser);
        }
    }

    private static User registerNewUser() {
        System.out.println("\nEnter User Details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User newUser = new User(name, email, phone, password);
        users.add(newUser);
        System.out.println("User registered successfully! Your User ID is: " + newUser.userID);
        return newUser;
    }

    private static void handleViewBookingHistory() {
        int userID = getIntInput("\nEnter User ID: ");
        List<Booking> userBookings = new ArrayList<>();
    
        // Filter bookings by userID
        for (Booking booking : bookings) {
            if (booking.userID == userID) {
                userBookings.add(booking);
            }
        }
    
        // Display the results
        if (userBookings.isEmpty()) {
            System.out.println("No booking history found for User ID: " + userID);
        } else {
            System.out.println("\nBooking History for User ID: " + userID);
            System.out.println("=====================================================================================================");
            System.out.printf("%-10s | %-5s | %-5s | %-5s | %-10s | %-10s | %-15s | %-8s%n",
                    "Booking ID", "Bus ID", "User ID", "Seats", "Total Price", "Status", "Departure Date", "Time");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            for (Booking booking : userBookings) {
                System.out.printf("%-10d | %-5d | %-5d | %-5d | $%-9.2f | %-10s | %-15s | %-8s%n",
                        booking.bookingID, booking.busID, booking.userID, booking.seatsBooked,
                        booking.totalPrice, booking.status, booking.departureDate, booking.departureTime);
            }
            System.out.println("=====================================================================================================");
        }
    }

    private static void handleUserCancelBooking() {
        int userID = getIntInput("\nEnter User ID: ");
        User user = findUserById(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (user.bookingHistory.isEmpty()) {
            System.out.println("You have no bookings to cancel.");
            return;
        }
        System.out.println("\nYour Booking History:");
        for (Booking booking : user.bookingHistory) {
            booking.displayBookingInfo();
        }
        int bookingID = getIntInput("\nEnter Booking ID to cancel: ");
        Booking bookingToCancel = null;
        for (Booking booking : user.bookingHistory) {
            if (booking.bookingID == bookingID) {
                bookingToCancel = booking;
                break;
            }
        }
        if (bookingToCancel != null) {
            user.bookingHistory.remove(bookingToCancel);
            bookings.remove(bookingToCancel);
            System.out.println("Booking canceled successfully.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    private static User findUserById(int userID) {
        for (User user : users) {
            if (user.userID == userID) {
                return user;
            }
        }
        return null;
    }

    private static void bookTicketForUser(User user) {
        showAllBuses();
        System.out.println("\nWould you like to search for buses?");
        System.out.println("1. Search by Route\n2. Search by Date\n3. No, continue with booking");
        int searchChoice = getIntInput("Enter your choice: ");
        List<Bus> foundBuses = new ArrayList<>();
        if (searchChoice == 1) {
            System.out.print("\nEnter Route to Search: ");
            String route = scanner.nextLine();
            foundBuses = user.searchBus(route, buses);
            if (foundBuses.isEmpty()) {
                System.out.println("No buses found on this route.");
                return;
            }
            System.out.println("\nBuses available on route '" + route + "':");
            for (Bus bus : foundBuses) {
                bus.displayBusInfo();
            }
        } else if (searchChoice == 2) {
            String searchDate = getValidatedDate("\nEnter Date to Search (DD/MM/YYYY): ");
            foundBuses = user.searchBusByDate(searchDate, buses);
            if (foundBuses.isEmpty()) {
                System.out.println("No buses found on this date.");
                return;
            }
            System.out.println("\nBuses available on date '" + searchDate + "':");
            for (Bus bus : foundBuses) {
                bus.displayBusInfo();
            }
        }
    
        int busID = getIntInput("Enter Bus ID to book: ");
        Bus selectedBus = null;
        for (Bus bus : buses) {
            if (bus.busID == busID) {
                selectedBus = bus;
                break;
            }
        }
        if (selectedBus == null) {
            System.out.println("Bus not found with ID: " + busID);
            return;
        }
        System.out.println("\nSelected Bus Details:");
        selectedBus.displayBusInfo();
        int seats = getIntInput("Enter number of seats to book: ");
        Booking booking = user.bookTicket(busID, seats, buses, bookings);
        if (booking != null) {
            System.out.println("\nBooking successful!");
            System.out.println("Booking Details:");
            booking.displayBookingInfo();
        } else {
            System.out.println("Booking failed. Bus ID not found or insufficient seats available.");
        }
    }

    private static void handleViewRevenue(Admin admin) {
        System.out.println("\nRevenue Calculation:");
        System.out.println("Would you like to apply filters?");
        System.out.println("1. No filters (view total revenue)\n" +
                           "2. Filter by date range\n" +
                           "3. Filter by bus ID");
        int filterChoice = getIntInput("Enter your choice: ");
    
        String startDate = null, endDate = null;
        Integer busID = null;
    
        switch (filterChoice) {
            case 1:
                // No filters
                break;
            case 2:
                // Filter by date range
                startDate = getValidatedDate("Enter start date (DD/MM/YYYY): ");
                endDate = getValidatedDate("Enter end date (DD/MM/YYYY): ");
                break;
            case 3:
                // Filter by bus ID
                busID = getIntInput("Enter Bus ID: ");
                break;
            default:
                System.out.println("Invalid choice. Using no filters.");
        }
    
        // Calculate revenue
        double totalRevenue = admin.calculateTotalRevenue(bookings, startDate, endDate, busID);
    
        // Display results
        System.out.println("\nRevenue Summary:");
        if (startDate != null && endDate != null) {
            System.out.println("Date Range: " + startDate + " to " + endDate);
        }
        if (busID != null) {
            System.out.println("Bus ID: " + busID);
        }
        System.out.printf("Total Revenue: $%.2f%n", totalRevenue);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static String getValidatedDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            try {
                dateFormat.setLenient(false);
                dateFormat.parse(dateStr);
                return dateStr;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use DD/MM/YYYY format.");
            }
        }
    }

    private static String getValidatedTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String timeStr = scanner.nextLine();
            try {
                timeFormat.setLenient(false);
                timeFormat.parse(timeStr);
                return timeStr;
            } catch (ParseException e) {
                System.out.println("Invalid time format. Please use HH:MM format (24-hour format).");
            }
        }
    }
}