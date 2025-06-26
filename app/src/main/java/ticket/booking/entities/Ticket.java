package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

public class Ticket {

    private String source;
    private String destination;

    @JsonProperty("date_of_travel")
    private String dateOfTravel;

    private Train train;

    private String ticketId;

    @JsonProperty("user_id")
    private String userId;

    private Map<String, String> stations;

    // Seat position in train
    private int seatRow;
    private int seatCol;

    // Default constructor
    public Ticket() {}

    // Parameterized constructor
    public Ticket(String source, String destination, String dateOfTravel, Train train,
                  String ticketId, String userId, Map<String, String> stations,
                  int seatRow, int seatCol) {
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
        this.ticketId = ticketId;
        this.userId = userId;
        this.stations = stations;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
    }

    // Getters
    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, String> getStations() {
        return stations;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    // Setters
    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStations(Map<String, String> stations) {
        this.stations = stations;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    // Display method for printing ticket info (ignored by Jackson during serialization)
    @JsonIgnore
    public String getTicketInfo() {
        return "Ticket ID: " + ticketId +
                ", From: " + source +
                ", To: " + destination +
                ", Date: " + dateOfTravel +
                ", Train ID: " + train.getTrainId() +
                ", Seat: Row " + seatRow + " Col " + seatCol;
    }
}
