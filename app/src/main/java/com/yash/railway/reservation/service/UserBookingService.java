package com.yash.railway.reservation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.railway.reservation.entities.Tickets;
import com.yash.railway.reservation.entities.Train;
import com.yash.railway.reservation.entities.User;
import com.yash.railway.reservation.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private User user;
    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "data/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        this.userList = loadUsers();
    }

    public UserBookingService() throws IOException {
        this.userList = loadUsers();
    }

    // ================= LOAD USERS =================
    public List<User> loadUsers() throws IOException {

        File file = new File(USERS_PATH);

        List<User> users = objectMapper.readValue(
                file,
                new TypeReference<List<User>>() {}
        );

        // Fix null tickets list
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getTicketsBooked() == null) {
                users.get(i).setTicketsBooked(new ArrayList<>());
            }
        }

        return users;
    }

    // ================= LOGIN =================
    public Boolean loginUser() {

        Optional<User> foundUser = userList.stream().filter(user1 ->
                user.getUsername().equalsIgnoreCase(user1.getUsername())
                        && UserServiceUtil.checkPassword(
                        user.getPassword(),
                        user1.getHashedPassword()
                )
        ).findFirst();

        if (foundUser.isPresent()) {
            this.user = foundUser.get(); // VERY IMPORTANT
            return true;
        }

        return false;
    }

    // ================= SIGNUP =================
    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void saveUserListToFile() throws IOException {
        objectMapper.writeValue(new File(USERS_PATH), userList);
    }

    // ================= FETCH BOOKINGS =================
    public void fetchBooking() {
        if (user != null) {
            user.printTickets();
        } else {
            System.out.println("No Bookings Found, please book the tickets first!");
        }
    }

    // ================= CANCEL BOOKING =================
    public Boolean cancelBooking() {

        if (user == null) {
            System.out.println("No user logged in.");
            return false;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your ticketID to cancel:");
        String ticketID = sc.nextLine();

        if (ticketID == null || ticketID.isEmpty()) {
            System.out.println("Invalid ticket ID.");
            return false;
        }

        // CHANGE 1: Find the ticket manually instead of removeIf
        Tickets ticketToCancel = null;

        for (int i = 0; i < user.getTicketsBooked().size(); i++) {
            if (user.getTicketsBooked().get(i).getTicketId().equals(ticketID)) {
                ticketToCancel = user.getTicketsBooked().get(i);
                break;
            }
        }

        if (ticketToCancel != null) {

            try {
                //CHANGE 2: Restore seat back to available (1)
                TrainService trainService = new TrainService();

                Train train = ticketToCancel.getTrain();

                List<List<Integer>> seats = train.getSeats();

                // Set seat back to 1 (available)
                seats.get(ticketToCancel.getRow())
                        .set(ticketToCancel.getSeat(), 1);

                train.setSeats(seats);

                //CHANGE 3: Update train in file
                trainService.addTrain(train);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // CHANGE 4: Remove ticket from user AFTER restoring seat
            user.getTicketsBooked().remove(ticketToCancel);

            // CHANGE 5: Save updated user list
            try {
                saveUserListToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Ticket cancelled successfully.");
            return true;

        } else {
            System.out.println("No ticket found with ID " + ticketID);
            return false;
        }
    }


    // ================= TRAIN SEARCH =================
    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        if (train == null) {
            System.out.println("No train selected.");
            return new ArrayList<>();
        }

        return train.getSeats();
    }

    // ================= BOOK SEAT =================
    public Boolean bookTrainSeat(Train train, int row, int seat) {

        if (user == null) {
            System.out.println("Please login first.");
            return false;
        }

        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size()
                    && seat >= 0 && seat < seats.get(row).size()) {

                if (seats.get(row).get(seat) == 1) {

                    // mark as booked
                    seats.get(row).set(seat, 0);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    // Proper Ticket Creation
                    String sourceStation = train.getStationsName().get(0);
                    String destinationStation = train.getStationsName()
                            .get(train.getStationsName().size() - 1);
                    Tickets ticket = new Tickets(
                            user.getUsername(),             // username
                            sourceStation,                              // source (update later)
                            destinationStation,                              // destination (update later)
                            new Date().toString(),              // date
                            UUID.randomUUID().toString(),       // ticketId
                            train,                              // train object
                            row,
                            seat
                    );

                    // ✅ Add ticket to logged-in user
                    user.getTicketsBooked().add(ticket);

                    // ✅ Save users file
                    saveUserListToFile();

                    System.out.println("Booking Successful!");
                    return true;

                } else {
                    System.out.println("Seat already booked.");
                    return false;
                }
            } else {
                System.out.println("Invalid seat position.");
                return false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}


