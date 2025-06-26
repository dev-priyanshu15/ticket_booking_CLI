package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> userList;
    private User user;
    private final String USER_FILE_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            userList = new ArrayList<>();
        } else {
            userList = objectMapper.readValue(file, new TypeReference<List<User>>() {});
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_FILE_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    private Optional<User> getValidatedUser() {
        return userList.stream()
                .filter(u -> u.getName().equals(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword()))
                .findFirst();
    }

    public Boolean loginUser() {
        return getValidatedUser().isPresent();
    }

    public Boolean signUp(User newUser) {
        try {
            userList.add(newUser);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            System.out.println("Error saving user: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }

    public void fetchBookings() {
        Optional<User> userFetched = getValidatedUser();
        if (userFetched.isPresent()) {
            userFetched.get().printTickets();
        } else {
            System.out.println("Invalid credentials or user not found.");
        }
    }

    public Boolean cancelBooking(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        Optional<User> matchedUser = getValidatedUser();
        if (matchedUser.isEmpty()) {
            System.out.println("User not found or invalid credentials.");
            return Boolean.FALSE;
        }

        User actualUser = matchedUser.get();

        // Find the ticket to cancel
        Optional<Ticket> ticketOpt = actualUser.getTicketsBooked()
                .stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId))
                .findFirst();

        if (ticketOpt.isEmpty()) {
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }

        Ticket ticketToCancel = ticketOpt.get();

        // Free the seat in the train
        Train train = ticketToCancel.getTrain();
        List<List<Integer>> seats = train.getSeats();
        int row = ticketToCancel.getSeatRow();
        int col = ticketToCancel.getSeatCol();

        if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
            seats.get(row).set(col, 0); // unbook the seat
            train.setSeats(seats);

            // ✅ Try to update train
            try {
                TrainService trainService = new TrainService();
                trainService.addTrain(train);
            } catch (IOException e) {
                System.out.println("Error updating train seat info: " + e.getMessage());
                return Boolean.FALSE;
            }
        } else {
            System.out.println("Invalid seat coordinates in ticket.");
            return Boolean.FALSE;
        }

        // Remove ticket from user
        boolean removed = actualUser.getTicketsBooked()
                .removeIf(ticket -> ticket.getTicketId().equals(ticketId));

        if (removed) {
            try {
                saveUserListToFile();
                System.out.println("Ticket with ID " + ticketId + " has been canceled.");
                return Boolean.TRUE;
            } catch (IOException e) {
                System.out.println("Failed to update user data after cancellation.");
                return Boolean.FALSE;
            }
        } else {
            System.out.println("Ticket removal failed.");
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            System.out.println("Error fetching trains: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    Optional<User> validatedUser = getValidatedUser();
                    if (validatedUser.isPresent()) {
                        User actualUser = validatedUser.get();

                        Ticket ticket = new Ticket();
                        ticket.setTicketId(UUID.randomUUID().toString());
                        ticket.setUserId(actualUser.getUserId());
                        ticket.setTrain(train);
                        ticket.setStations(train.getStationTimes());
                        ticket.setSource(train.getStations().get(0));
                        ticket.setDestination(train.getStations().get(train.getStations().size() - 1));
                        ticket.setDateOfTravel(new Date().toString());
                        ticket.setSeatRow(row);
                        ticket.setSeatCol(seat);

                        List<Ticket> tickets = actualUser.getTicketsBooked();
                        if (tickets == null) tickets = new ArrayList<>();
                        tickets.add(ticket);
                        actualUser.setTicketsBooked(tickets);

                        saveUserListToFile();
                        System.out.println("Seat booked successfully.");
                        return Boolean.TRUE;
                    } else {
                        System.out.println("User not logged in or invalid credentials.");
                        return Boolean.FALSE;
                    }
                } else {
                    System.out.println("Seat already booked.");
                    return Boolean.FALSE;
                }
            } else {
                System.out.println("Invalid seat coordinates.");
                return Boolean.FALSE;
            }
        } catch (IOException ex) {
            System.out.println("Error booking seat: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }
}
