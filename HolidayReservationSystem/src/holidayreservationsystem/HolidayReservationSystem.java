/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.util.List;
import java.util.Scanner;
import ws.client.CarCategory;
import ws.client.Employee;
import ws.client.MakeModel;
import ws.client.Outlet;
import ws.client.PartnerReservationWebService;
import ws.client.PartnerReservationWebService_Service;

/**
 *
 * @author wangp
 */
public class HolidayReservationSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PartnerReservationWebService_Service partnerReservationWebService_Service = new PartnerReservationWebService_Service();
        PartnerReservationWebService partnerReservationWebServicePort = partnerReservationWebService_Service.getPartnerReservationWebServicePort();

        Scanner sc = new Scanner(System.in);

        List<Outlet> outlets = partnerReservationWebServicePort.retrieveAllOutlets();
//        for (Outlet o : outlets) {
//            for (Employee e : o.getEmployees()) {
//                System.out.println("employee is " + e);
//            }
//        }
//
//        List<CarCategory> carCats = partnerReservationWebServicePort.retrieveAllCarCategories();
//        for (CarCategory c : carCats) {
//            System.out.println("Car Category " + c + " has makemodels: ");
//            for (MakeModel m : c.getListOfMakeModelsIncluded()) {;
//                System.out.println("makemodel is " + m);
//            }
//        }

        System.out.println("Welcome to Holiday Reservation System!");
        while (true) {

            System.out.println("Please select actions to perform (Enter number):");

            System.out.println("1: Login");
            System.out.println("2: Exit");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    LoggedInActions loggedInActions = new LoggedInActions(sc, partnerReservationWebServicePort);
                    loggedInActions.doLogin();
                    break;
                case "2":
                    System.out.println("Exiting...");
                    return;
            }
        }

    }

}
