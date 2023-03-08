/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.Car;
import ws.client.CarCategory;
import ws.client.Customer;
import ws.client.Outlet;
import ws.client.Partner;
import ws.client.PartnerReservationWebService;
import ws.client.RentalRate;
import ws.client.Reservation;

/**
 *
 * @author wangp
 */
public class LoggedInActions {

    private Partner loggedInPartner;

    private Scanner sc;
    private PartnerReservationWebService partnerReservationWebServicePort;

    public LoggedInActions(Scanner sc, PartnerReservationWebService partnerReservationWebServicePort) {
        this.sc = sc;
        this.partnerReservationWebServicePort = partnerReservationWebServicePort;
    }

    public void doLogin() {
        System.out.println("Enter email: ");
        String emailInput = sc.nextLine();
        System.out.println("Enter password: ");
        String passwordInput = sc.nextLine();

        Partner partnerRetrieved = partnerReservationWebServicePort.retrievePartnerByEmailPassword(emailInput, passwordInput);

        if (partnerRetrieved == null) {
            System.out.println("Partner does not exist!");
            return;
        }

        this.loggedInPartner = partnerRetrieved;

        System.out.println("Welcome " + partnerRetrieved.getName() + "!");

        while (true) {
            System.out.println("Please select actions to perform (Enter number):");

            System.out.println("1: Search car");
            System.out.println("2: View all reservations");
            System.out.println("3: Logout");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    searchCar();
                    break;
                case "2":
                    viewAllReservations();
                    break;
                case "3":
                    System.out.println("Logging out...");
                    return;
            }
        }
    }

    private void viewAllReservations() {
        List<Reservation> partnerReservations = partnerReservationWebServicePort.retrieveReservationsByPartner(loggedInPartner);
        if (partnerReservations.isEmpty()) {
            System.out.println("There are no reservations for this partner!");
            return;
        }
        
        System.out.println("Select a reservation (Enter number): ");

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy : HHmm");

        int count = 0;
        for (Reservation r : partnerReservations) {
            count++;
            System.out.println(count + ": Customer " + r.getCustomer().getName() + " reserving a " + r.getCarCategory().getName()
                    + " collecting on " + r.getCollectDate().toGregorianCalendar().getTime());
        }

        String selectionString = sc.nextLine();
        
        Reservation selectedReservation = null;
        try {
            int selectedNum = Integer.parseInt(selectionString);
            if (selectedNum <= partnerReservations.size()) {
                selectedReservation = partnerReservations.get(selectedNum - 1);
            } else {
                return;
            }
        } catch (NumberFormatException n) {
            return;
        }
        
        DecimalFormat moneyFormat = new DecimalFormat("0.00");

        //Car car = selectedReservation.getCar();
        System.out.println("Reservation ID: " + selectedReservation.getId());
        System.out.println("Car Category: " + selectedReservation.getCarCategory().getName());
        //System.out.println("Car Make and Model: " + car.getMakeModel().getName());
        //System.out.println("Car License Plate: " + car.getLicensePlate());
        System.out.println("Pickup Location: " + selectedReservation.getPickupOutlet().getAddress());
        System.out.println("Pickup Date & TIme: " + df.format(selectedReservation.getCollectDate().toGregorianCalendar().getTime()));
        System.out.println("Return Location: " + selectedReservation.getReturnOutlet().getAddress());
        System.out.println("Return Date & Time: " + df.format(selectedReservation.getReturnDate().toGregorianCalendar().getTime()));
        System.out.println("Rental Fee: " + moneyFormat.format(selectedReservation.getRentalPrice()));
        String paymentStatus = selectedReservation.isIsPaid() ? "Paid" : "Unpaid";
        System.out.println("Payment status: " + paymentStatus);
        System.out.println("Reservation status: " + selectedReservation.getReservationStatus());

        System.out.println("Please select actions to perform (Enter number): ");
        System.out.println("1: Cancel Reservation");
        System.out.println("2: Exit to main menu");

        String choice = sc.nextLine();
        if (choice.equals("1")) {
            String cancelText = partnerReservationWebServicePort.cancelReservation(selectedReservation);
            System.out.println(cancelText);
        }

    }

    private void searchCar() {

        System.out.println("Select car category: ");
        List<CarCategory> allCategories = partnerReservationWebServicePort.retrieveAllCarCategories();

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
        List<Outlet> allOutlets = partnerReservationWebServicePort.retrieveAllOutlets();
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
        // setup to call partnerSearchCar
        GregorianCalendar pickupGregCalendar = new GregorianCalendar();
        pickupGregCalendar.setTime(pickupDate);
        GregorianCalendar returnGregCalendar = new GregorianCalendar();
        returnGregCalendar.setTime(returnDate);

        XMLGregorianCalendar xmlPickupGregCalendar = null;
        XMLGregorianCalendar xmlReturnGregCalendar = null;

        try {
            xmlReturnGregCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(returnGregCalendar);
            xmlPickupGregCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(pickupGregCalendar);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(LoggedInActions.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Reservation> reservationsAvailable = partnerReservationWebServicePort
                .partnerSearchCar(chosenCategory, xmlPickupGregCalendar, xmlReturnGregCalendar,
                        pickupOutlet, returnOutlet);

        List<RentalRate> usedRentalRates = partnerReservationWebServicePort
                .partnerSearchCarGetUsedRentals(chosenCategory, xmlPickupGregCalendar, xmlReturnGregCalendar,
                        pickupOutlet, returnOutlet);

        if (reservationsAvailable.isEmpty()) {
            System.out.println("There are no available cars of that category during that time period!");
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
//                    + ") (Price: " + moneyFormat.format(r.getRentalPrice()) + ")");
//        }
//
//        choice = sc.nextLine();
//        if (choice.equals("Q")) {
//            return;
//        }
        Reservation selectedReservation = reservationsAvailable.get(0);

        // CUSTOMER DETAILS FOR PARTNER
        System.out.println("Enter customer details: ");
        Customer customer = new Customer();
        System.out.println("Enter email: ");
        customer.setEmail(sc.nextLine());
        System.out.println("Enter name: ");
        customer.setName(sc.nextLine());
        System.out.println("Enter credit card number: ");
        customer.setCreditCard(sc.nextLine());
        System.out.println("Enter phone number: ");
        customer.setPhone(sc.nextLine());
        customer.setPassword(loggedInPartner.getPassword());
        // password how? set by default to the partner password?

        System.out.println("Would you like to pay the rental fee amount of $"
                + moneyFormat.format(selectedReservation.getRentalPrice())
                + " immediately or on the day of pickup?");
        System.out.println("'Y' for immediate payment, 'N' for deferred: ");

        choice = sc.nextLine().toUpperCase();
        boolean isImmediate = false;
        if (choice.equals("Y")) {
            isImmediate = true;
            partnerReservationWebServicePort.payReservation(selectedReservation, customer); // this is for credit card string only
            // nothing is changed in db from payReservation
        }

        partnerReservationWebServicePort.partnerReserveCar(selectedReservation, usedRentalRates,
                customer, pickupOutlet.getId(), returnOutlet.getId(), loggedInPartner.getId(), isImmediate);

        System.out.println("Reservation completed successfully!");
        System.out.println("Thank you for your patronage.");

    }
}
