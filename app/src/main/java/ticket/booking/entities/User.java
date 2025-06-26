package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class User {

    private String name;
    private String password;

    @JsonProperty("hashed_password")
    private String hashedPassword;

    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;

    @JsonProperty("user_id")
    private String userId;

    // Constructors
    public User() {
    }

    public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userId) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Print bookings
    public void printTickets() {
        if (ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }
}
