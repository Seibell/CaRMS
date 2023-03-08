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
import entity.Customer;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 *
 * @author wangp
 */
public class Main {

    @EJB
    private static MakeModelEntitySessionBeanRemote makeModelEntitySessionBean;

    @EJB
    private static CarEntitySessionBeanRemote carEntitySessionBean;

    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBean;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private static CarCategorySessionBeanRemote carCategoryEntitySessionBean;

    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBean;

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        System.out.println("Welcome to CaRMS Reservation Client!");
        Scanner sc = new Scanner(System.in);
        
        LoggedInActions loggedInActions = null;
        while(true) {
            
            System.out.println("Please select actions to perform (Enter number):");
            
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit");
            
            String choice = sc.nextLine();
            
            switch(choice) {
                case "1":
                    loggedInActions = LoggedInActions.doLogin(carEntitySessionBean, customerEntitySessionBean,
                            carCategoryEntitySessionBean, rentalRateSessionBean, outletEntitySessionBean,
                            makeModelEntitySessionBean, sc);
                    break;
                case "2":
                    Customer newCustomer = registerAsCustomer(customerEntitySessionBean, sc);
                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
            }
            
            if (loggedInActions != null) loggedInActions.mainMenu();
            loggedInActions = null;
        }
    }
    
    public static Customer registerAsCustomer(CustomerEntitySessionBeanRemote customerBean, Scanner sc) {
        System.out.println("Please enter your email: ");
        String email = sc.nextLine();
        Customer customerCheck = customerBean.retrieveCustomerByEmail(email);
        if (customerCheck != null) {
            System.out.println("Customer with same email already exists! Returning to main menu...");
            return null;
        }
        
        System.out.println("Please enter your name: ");
        String name = sc.nextLine();
        System.out.println("Please enter your phone number: ");
        String phoneNumber = sc.nextLine();
        System.out.println("Please enter your credit card number: ");
        String creditCardString = sc.nextLine();
        System.out.println("Please enter your desired password: ");
        String password = sc.nextLine();
        
        Customer customerToRegister = new Customer(name, email, phoneNumber, creditCardString, password);
        customerBean.createCustomer(customerToRegister);
        
        System.out.println("Customer successfully registered!");
        return customerToRegister;
    }
}
