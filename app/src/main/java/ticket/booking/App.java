package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Welcome to Train Booking System");

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;

        // Initialize service without a logged-in user
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("Error loading user database. Please check if users.json exists and is valid.");
            ex.printStackTrace();
            return;
        }

        User loggedInUser = null;
        Train trainSelectedForBooking = null;

        while (option != 7) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel a Booking");
            System.out.println("7. Exit");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.println("Enter username to sign up:");
                    String signupName = scanner.next();
                    System.out.println("Enter password:");
                    String signupPass = scanner.next();
                    User newUser = new User(signupName, signupPass,
                            UserServiceUtil.hashPassword(signupPass),
                            new ArrayList<>(),
                            UUID.randomUUID().toString());

                    if (userBookingService.signUp(newUser)) {
                        System.out.println("Sign up successful.");
                    } else {
                        System.out.println("Sign up failed.");
                    }
                }
                case 2 -> {
                    System.out.println("Enter username to log in:");
                    String loginName = scanner.next();
                    System.out.println("Enter password:");
                    String loginPass = scanner.next();
                    User loginUser = new User(loginName, loginPass, null, new ArrayList<>(), UUID.randomUUID().toString());
                    try {
                        userBookingService = new UserBookingService(loginUser);
                        if (userBookingService.loginUser()) {
                            loggedInUser = loginUser;
                            System.out.println("Login successful.");
                        } else {
                            System.out.println("Invalid credentials.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error during login.");
                        e.printStackTrace();
                    }
                }
                case 3 -> {
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Your bookings:");
                    userBookingService.fetchBookings();
                }
                case 4 -> {
                    System.out.println("Enter source station:");
                    String source = scanner.next();
                    System.out.println("Enter destination station:");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);

                    if (trains.isEmpty()) {
                        System.out.println("No trains found between these stations.");
                        break;
                    }

                    System.out.println("Available trains:");
                    int i = 1;
                    for (Train t : trains) {
                        System.out.println(i++ + ". Train ID: " + t.getTrainId());
                        t.getStationTimes().forEach((station, time) ->
                                System.out.println("   " + station + " at " + time));
                    }

                    System.out.println("Select a train by typing its number:");
                    int trainIndex = scanner.nextInt();
                    if (trainIndex <= 0 || trainIndex > trains.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    trainSelectedForBooking = trains.get(trainIndex - 1);
                    System.out.println("Train selected: " + trainSelectedForBooking.getTrainId());
                }
                case 5 -> {
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                        break;
                    }
                    if (trainSelectedForBooking == null) {
                        System.out.println("Please select a train first (option 4).");
                        break;
                    }

                    System.out.println("Available seats (0 = empty, 1 = booked):");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row : seats) {
                        for (Integer seat : row) {
                            System.out.print(seat + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Enter seat row number:");
                    int row = scanner.nextInt();
                    System.out.println("Enter seat column number:");
                    int col = scanner.nextInt();

                    System.out.println("Booking your seat...");
                    boolean success = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if (success) {
                        System.out.println("Seat booked successfully.");
                    } else {
                        System.out.println("Booking failed. Seat may already be taken or invalid.");
                    }
                }
                case 6 -> {
                    if (loggedInUser == null) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Enter ticket ID to cancel:");
                    String ticketId = scanner.next();
                    boolean cancelled = userBookingService.cancelBooking(ticketId);
                    if (cancelled) {
                        System.out.println("Ticket cancelled.");
                    } else {
                        System.out.println("Ticket cancellation failed.");
                    }
                }
                case 7 -> System.out.println("Exiting... Thank you for using the Train Booking System.");
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
