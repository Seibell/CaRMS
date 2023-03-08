/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.MakeModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.SearchReserveCarSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author wangp
 */
public class LoggedInActions {

    private CustomerEntitySessionBeanRemote customerBean;
    private CarCategorySessionBeanRemote carCategoryBean;
    private MakeModelEntitySessionBeanRemote makeModelBean;
    private RentalRateSessionBeanRemote rentalRateBean;
    private OutletEntitySessionBeanRemote outletBean;
    private CarEntitySessionBeanRemote carBean;

    private Customer customer;
    private Scanner sc;

    public LoggedInActions() {

    }

    public LoggedInActions(CarEntitySessionBeanRemote carBean, CustomerEntitySessionBeanRemote customerBean,
            CarCategorySessionBeanRemote carCategoryBean, RentalRateSessionBeanRemote rentalRateBean,
            OutletEntitySessionBeanRemote outletBean, MakeModelEntitySessionBeanRemote makeModelBean,
            Customer customer, Scanner sc) {
        this.carBean = carBean;
        this.customerBean = customerBean;
        this.carCategoryBean = carCategoryBean;
        this.rentalRateBean = rentalRateBean;
        this.outletBean = outletBean;
        this.customer = customer;
        this.sc = sc;
    }

    public void mainMenu() {
        System.out.println("Welcome " + customer.getName() + "!");
        while (true) {
            System.out.println("Please select actions to perform (Enter number):");
            System.out.println("1: Search Car");
            System.out.println("2: View All My Reservations");
            System.out.println("3: Logout");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    searchCar();
                    // remember to update customer if he reserves a car?
                    break;
                case "2":
                    viewAllMyReservations();
                    break;
                case "3":
                    System.out.println("Logging out...");
                    return;
            }

        }
    }

    public void viewAllMyReservations() {
        System.out.println("Viewing your reservations...");
        // uses reservation bean because need to update customer again, eg. if he made a new reservation
        ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote = lookupReservationEntitySessionBeanRemote();
        List<Reservation> reservations = reservationEntitySessionBeanRemote.retrieveReservationsByCustomer(customer);

        if (reservations.isEmpty()) {
            System.out.println("You currently have no reservations!");
            return;
        }

        System.out.println("Select reservation details to view (Enter number): ");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy : HHmm");
        int count = 0;
        for (Reservation r : reservations) {
            count++;
            String formattedDate = df.format(r.getCollectDate());
            System.out.println(count + ": Reservation " + r.getId() + " from: " + formattedDate);
        }
        String selectionString = sc.nextLine();
        // can do input validation here

        // == show selected reservation ==
        Reservation selectedReservation = null;
        try {
            int selectedNum = Integer.parseInt(selectionString);
            if (selectedNum <= reservations.size()) {
                selectedReservation = reservations.get(selectedNum - 1);
            } else {
                return;
            }
        } catch (NumberFormatException n) {
            return;
        }
        DecimalFormat moneyFormat = new DecimalFormat("0.00");

        Car car = selectedReservation.getCar();
        System.out.println("Reservation ID: " + selectedReservation.getId());
        System.out.println("Car Category: " + selectedReservation.getCarCategory().getName());
        //System.out.println("Car Make and Model: " + car.getMakeModel().getName());
        //System.out.println("Car License Plate: " + car.getLicensePlate());
        System.out.println("Pickup Location: " + selectedReservation.getPickupOutlet().getAddress());
        System.out.println("Pickup Date & Time: " + df.format(selectedReservation.getCollectDate()));
        System.out.println("Return Location: " + selectedReservation.getReturnOutlet().getAddress());
        System.out.println("Return Date & Time: " + df.format(selectedReservation.getReturnDate()));
        System.out.println("Rental Fee: $" + moneyFormat.format(selectedReservation.getRentalPrice()));
        String paymentStatus = selectedReservation.isIsPaid() ? "Paid" : "Unpaid";
        System.out.println("Payment status: " + paymentStatus);
        System.out.println("Reservation status: " + selectedReservation.getReservationStatus());

        System.out.println("Please select actions to perform (Enter number): ");
        System.out.println("1: Cancel Reservation");
        System.out.println("2: Exit to main menu");

        String choice = sc.nextLine();
        if (choice.equals("1")) {
            String cancelText = reservationEntitySessionBeanRemote.cancelReservation(selectedReservation);
            System.out.println(cancelText);
        }
        // else
        return;

    }

    public void searchCar() {

        SearchReserveCarSessionBeanRemote searchReserveCarSessionBeanRemote = lookupSearchReserveCarSessionBeanRemote();

        System.out.println("Select car category: ");
        List<CarCategory> allCategories = carCategoryBean.retrieveAllCarCategories();

        int count = 0;
        for (CarCategory c : allCategories) {
            count++;
            System.out.println(count + ": " + c.getName());
        }

        String categoryChoice = sc.nextLine();

        // we can do invalid input check here
        CarCategory chosenCategory = allCategories.get(Integer.parseInt(categoryChoice) - 1);
        // to do all stuff

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HHmm");
        // == PICKUP TIME INPUT ==
        System.out.println("Enter pickup date (DD-MM-YYYY): ");
        String pickupDateString = sc.nextLine();

        System.out.println("Enter pickup time (24H): ");
        String pickupTimeString = sc.nextLine();

        Date pickupDate = null;
        try {
            pickupDate = dateFormat.parse(pickupDateString + "-" + pickupTimeString);
        } catch (ParseException ex) {
            Logger.getLogger(LoggedInActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Select pickup outlet (Enter number): ");
        List<Outlet> allOutlets = outletBean.retrieveAllOutlets(true); // should be eager alrd? but to be safe.
        count = 0;
        for (Outlet o : allOutlets) {
            count++;
            System.out.println(count + ": " + o.getAddress());
        }

        String choice = sc.nextLine();
        // we can do a check for invalid input here
        Outlet pickupOutlet = allOutlets.get(Integer.parseInt(choice) - 1);

        // == RETURN TIME INPUT ==
        System.out.println("Enter return date (DD-MM-YYYY): ");
        String returnDateString = sc.nextLine();

        System.out.println("Enter return time (24H): ");
        String returnTimeString = sc.nextLine();

        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(returnDateString + "-" + returnTimeString);
        } catch (ParseException ex) {
            Logger.getLogger(LoggedInActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Select return outlet (Enter number): "); // no need to retrieve all outlet again
        count = 0;
        for (Outlet o : allOutlets) {
            count++;
            System.out.println(count + ": " + o.getAddress());
        }

        choice = sc.nextLine();
        // we can do a check for invalid input here
        Outlet returnOutlet = allOutlets.get(Integer.parseInt(choice) - 1);

        Pair<List<Reservation>, List<RentalRate>> reservationsRentalPair = searchReserveCarSessionBeanRemote
                .searchAvailableCarReservations(chosenCategory, pickupDate, returnDate, pickupOutlet, returnOutlet);

        List<Reservation> reservationsAvailable = reservationsRentalPair.getKey();
        List<RentalRate> usedRentalRates = reservationsRentalPair.getValue();

        // show all reservations
        if (reservationsAvailable.isEmpty()) {
            System.out.println("Our sincere apologies, there are no available cars of that category during that time period!");
            return;
        }
        
        DecimalFormat moneyFormat = new DecimalFormat("0.00");
        System.out.println("A " + chosenCategory.getName() + " is available on that date!");
        System.out.println("Do you want to book a reservation? ('Y'/'N'):");
        String reserveChoice = sc.nextLine().toUpperCase();
        if (reserveChoice.equals("N")) {
            return;
        }
//        count = 0;
//        for (Reservation r : reservationsAvailable) {
//            count++;
//            System.out.println(count + ": " + r.getCar().getMakeModel().getName() + " (License Plate: "
//                    + r.getCar().getLicensePlate() + ") (Colour: " + r.getCar().getColour()
//                    + ") (Price: $" + moneyFormat.format(r.getRentalPrice()) + ")");
//        }

//        choice = sc.nextLine();
//        if (choice.equals("Q")) {
//            return;
//        }
        Reservation selectedReservation = reservationsAvailable.get(0);

        System.out.println("Would you like to pay the rental fee amount of $" 
                + moneyFormat.format(selectedReservation.getRentalPrice())
                + " immediately or on the day of pickup?");
        System.out.println("'Y' for immediate payment, 'N' for deferred: ");

        choice = sc.nextLine().toUpperCase();
        boolean isImmediate = false;
        if (choice.equals("Y")) {
            isImmediate = true;
            searchReserveCarSessionBeanRemote.payReservation(selectedReservation, customer);
        }

        searchReserveCarSessionBeanRemote.confirmReservation(selectedReservation, usedRentalRates,
                customer.getId(), pickupOutlet.getId(), returnOutlet.getId(), isImmediate);

        System.out.println("Reservation completed successfully!");
        System.out.println("Thank you for your patronage.");

        return;

    }

    public static LoggedInActions doLogin(CarEntitySessionBeanRemote carBean, CustomerEntitySessionBeanRemote customerBean,
            CarCategorySessionBeanRemote carCategoryBean, RentalRateSessionBeanRemote rentalRateBean, OutletEntitySessionBeanRemote outletBean,
            MakeModelEntitySessionBeanRemote makeModelBean, Scanner sc) {

        while (true) {
            System.out.println("Please enter your email: ");
            String email = sc.nextLine();

            Customer retrievedCustomer = customerBean.retrieveCustomerByEmail(email);
            if (retrievedCustomer == null) {
                System.out.println("No such customer exists! Returning to main menu...");
                return null;
            }

            System.out.println("Please enter your password: ");
            String enteredPassword = sc.nextLine();

            String accountPassword = retrievedCustomer.getPassword();

            if (enteredPassword.equals(accountPassword)) {
                System.out.println("Login success!");
                return new LoggedInActions(carBean, customerBean, carCategoryBean,
                        rentalRateBean, outletBean, makeModelBean, retrievedCustomer, sc);
            }
            // else
            System.out.println("Wrong password! Returning to main menu...");
            return null;
        }
    }

    private SearchReserveCarSessionBeanRemote lookupSearchReserveCarSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (SearchReserveCarSessionBeanRemote) c.lookup("java:comp/env/SearchReserveCarSessionBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ReservationEntitySessionBeanRemote lookupReservationEntitySessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ReservationEntitySessionBeanRemote) c.lookup("java:comp/env/ReservationEntitySessionBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
