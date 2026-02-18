
package com.yash.railway.reservation;

import com.yash.railway.reservation.entities.Train;
import com.yash.railway.reservation.entities.User;
import com.yash.railway.reservation.service.UserBookingService;
import com.yash.railway.reservation.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;
import java.util.Map;


public class App
{

    private static String nameTosignUp;
    private static Train trainSelectedForBooking;


    public static void main(String[] args) {
        System.out.println("Hello, Railway Reservation System");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("There was an error trying to open the file");
            return;
        }


        while (choice != 7) {

            System.out.println("Please choose one of the following");
            System.out.println("1. SignUp");
            System.out.println("2. LogIn");
            System.out.println("3. Fetch Booking Details");
            System.out.println("4. Search Train");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");

            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Enter your Username: ");
                    String nameToSignUp = scanner.next();

                    System.out.println("Enter your Password: ");
                    String passTosignup = scanner.next();

                    System.out.println("Enter your Email: ");
                    String emailTosignup = scanner.next();

                    System.out.println("Enter your Mobile Number: ");
                    String mobileNumberTosignup = scanner.next();

                    User userToSignup = new User(
                            nameToSignUp,
                            passTosignup,
                            UserServiceUtil.hashPassword(passTosignup),
                            UUID.randomUUID().toString(),
                            emailTosignup,
                            mobileNumberTosignup
                    );

                    boolean isSignedUp = userBookingService.signUp(userToSignup);

                    if (isSignedUp) {
                        System.out.println("Signup successful!");
                    } else {
                        System.out.println("Signup failed!");
                    }
                    break;

                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();

                    System.out.println("Enter the password to Login");
                    String passwordToLogin = scanner.next();

                    User userToLogin = new User(nameToLogin, passwordToLogin, null, null, null, null);

                    try {
                        userBookingService = new UserBookingService(userToLogin);

                        if (userBookingService.loginUser()) {
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Invalid username or password.");
                        }

                    } catch (IOException ex) {
                        System.out.println("Login failed due to system error.");
                    }
                    break;

                case 3:
                    userBookingService.fetchBooking();
                    break;

                case 4:
                    System.out.println("Type your source station");
                    String source = scanner.next();

                    System.out.println("Type your destination station");
                    String dest = scanner.next();

                    List<Train> trains = userBookingService.getTrains(source, dest);

                    if (trains.isEmpty()) {
                        System.out.println("No trains found.");
                        break;
                    }

                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train ID : " + t.getTrainId());
                        index++;
                    }

                    System.out.println("Select a train by typing 1, 2, 3...");
                    int userChoice = scanner.nextInt();

                    if (userChoice < 1 || userChoice > trains.size()) {
                        System.out.println("Invalid train selection.");
                        break;
                    }

                    trainSelectedForBooking = trains.get(userChoice - 1);
                    System.out.println("You selected train " + trainSelectedForBooking.getTrainId());
                    break;

                case 5:

                    if (trainSelectedForBooking == null) {
                        System.out.println("Please search and select a train first.");
                        break;
                    }

                    System.out.println("Select a seat out of these seats");

                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);

                    for (int i = 0; i < seats.size(); i++) {
                        for (int j = 0; j < seats.get(i).size(); j++) {
                            System.out.print(seats.get(i).get(j) + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Enter the row (starting from 1)");
                    int row = scanner.nextInt();

                    System.out.println("Enter the column (starting from 1)");
                    int col = scanner.nextInt();

                    row = row - 1;
                    col = col - 1;

                    boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);

                    if (booked) {
                        System.out.println("Booked your journey");
                    } else {
                        System.out.println("Can't book this seat");
                    }

                    break;

                case 6:
                    System.out.println("Cancelling your bookings");
                    userBookingService.cancelBooking();
                    break;

                case 7:
                    System.out.println("Thank you for using our Reservation System");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                    break;
            }
        }
    }
}


