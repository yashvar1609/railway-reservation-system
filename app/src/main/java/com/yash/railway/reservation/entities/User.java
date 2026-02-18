package com.yash.railway.reservation.entities;

import java.util.List;


public class User {

    public String username;

    private String password;

    private String hashedPassword;

    private String userId;

    private String email;

    private String phoneNumber;

    private List<Tickets> ticketsBooked;

    public User(String username, String password, String hashedPassword,
                String userId, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public User(){}  //default constructor if nothing is passed then nothing would be returned

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Tickets> getTicketsBooked() {
        return ticketsBooked;
    }
    public void setTicketsBooked(List<Tickets> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }
    public void printTickets(){
        if(ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No Bookings Found, please book the ticket first!");
            return;
        }
        for(int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }
    public String userInfo()
    {
        return String.format("UserID belongs to user %s  " +
                "Contact number %s " +
                "Email address %s", username, phoneNumber,  email);

    }

}





