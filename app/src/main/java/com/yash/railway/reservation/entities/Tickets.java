package com.yash.railway.reservation.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Tickets {

    private String userId;
    private String source;
    private String destination;
    private String DateofTravel;
    private String ticketId;
    private Train train;
    private int row;
    private int seat;

    // ✅ REQUIRED for Jackson
    public Tickets() {
    }

    // ✅ Main constructor used while booking
    public Tickets(String userId,
                   String source,
                   String destination,
                   String DateofTravel,
                   String ticketId,
                   Train train,int row,int seat) {

        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.DateofTravel = DateofTravel;
        this.ticketId = ticketId;
        this.train = train;
        this.row = row;
        this.seat = seat;
    }



    // ================= DISPLAY METHOD =================
    public String getTicketInfo() {
        return String.format(
                "Ticket Id: %s | User: %s | From: %s | To: %s | Date: %s",
                ticketId,
                userId,
                source,
                destination,
                DateofTravel
        );
    }

    // ================= GETTERS & SETTERS =================

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateofTravel() {
        return DateofTravel;
    }

    public void setDateofTravel(String DateofTravel) {
        this.DateofTravel = DateofTravel;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

}
