/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.DispatchSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.MakeModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.MakeModel;
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import util.enumeration.CarStatus;
import util.enumeration.ReservationStatus;
import util.exception.CarCategoryNotFoundException;
import util.exception.DispatchNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.MakeModelNotFoundException;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author ryanl
 */
public class MainApp {
    
    private EmployeeEntitySessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private MakeModelEntitySessionBeanRemote makeModelEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private DispatchSessionBeanRemote dispatchSessionBeanRemote;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    
    private Employee currentEmployee;
    
    public MainApp() {    
    }
    
    public MainApp(EmployeeEntitySessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, MakeModelEntitySessionBeanRemote makeModelEntitySessionBeanRemote,
            CarEntitySessionBeanRemote carEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote,
            CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, DispatchSessionBeanRemote dispatchSessionBeanRemote,
            ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.makeModelEntitySessionBeanRemote = makeModelEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.dispatchSessionBeanRemote = dispatchSessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to CaRMS Management Client ***\n");
            System.out.println("1: Login as Employee");
            System.out.println("2: Exit \n");
            response = 0;
            
            while (response < 1 || response > 2) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login Successful! \n");
                        
                        if (currentEmployee.getEmployeeRole().equals(EmployeeRole.SALES_MANAGER)) {
                            menuSalesManager();
                        } else if (currentEmployee.getEmployeeRole().equals(EmployeeRole.OPS_MANAGER)) {
                            menuOperationsManager();
                        } else if (currentEmployee.getEmployeeRole().equals(EmployeeRole.CUSTOMER_SERVICE_EXECUTIVE)) {
                            menuCustomerServiceExecutive();
                        } else if (currentEmployee.getEmployeeRole().equals(EmployeeRole.EMPLOYEE)) {
                            menuGeneral();
                        } else {
                            System.out.println("No role? Please try again");
                        }
                        
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid Login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try agian \n");
                }
            }
            
            if (response == 2) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CaRMS Management Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuSalesManager() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Sales Manager Terminal ***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View all Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Logout\n");
            response = 0;
            
            while (response < 1 || response > 6) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {
                    doViewAllRentalRate();
                } else if (response == 3) {
                    doViewRentalRateDetails();
                } else if (response == 4) {
                    doUpdateRentalRate();
                } else if (response == 5) {
                    doDeleteRentalRate();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again \n");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }
    
     private void menuOperationsManager() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Operations Manager Terminal ***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: About Car Models");
            System.out.println("2: About Cars");
            System.out.println("3: About Transits");
            System.out.println("4: Logout\n");
            response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    menuAboutCarModels();
                } else if (response == 2) {
                    menuAboutCars();
                } else if (response == 3) {
                    menuAboutTransits();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }
     
      private void menuCustomerServiceExecutive() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Customer Service Executive Terminal ***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Logout\n");
            response = 0;
            
            while (response < 1 || response > 3) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doPickupCar();
                } else if (response == 2) {
                    doReturnCar();
                } else {
                    break;
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create new rental rate ***");
        System.out.print("Enter name for rental rate> ");
        String name = sc.nextLine().trim();
        
        System.out.print("Enter rental rate type> ");
        String rentalRateType = sc.nextLine().trim();
        
        System.out.print("Enter name of car category> ");
        String carCategoryName = sc.nextLine().trim();
        
        System.out.print("Enter dailyRate> ");
        BigDecimal dailyRate = sc.nextBigDecimal();
        sc.nextLine();
        
        Date startDate = new Date();
        Date endDate = new Date();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        
        System.out.print("Enter start date (dd-MMM-yyyy)> ");
        String startD = sc.nextLine();
        
        System.out.print("Enter end date (dd-MMM-yyyy)> ");
        String endD = sc.nextLine();
        
        try {
            startDate = dateFormat.parse(startD);
            endDate = dateFormat.parse(endD);
        } catch (ParseException ex) {
            System.out.println("parse exception occured, wrong date! Defaulting to current Date");
        }
        
        try {
            rentalRateSessionBeanRemote.createRentalRate(new RentalRate(name, rentalRateType, dailyRate, startDate, endDate), carCategorySessionBeanRemote.retrieveCarCategoryByName(carCategoryName).getId());
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car category not found!");
        }
        
        System.out.println("Rental Rate: " + name + " has been successfully created!");
    }

    private void doViewAllRentalRate() {
        System.out.println("*** View all rental rates ***");

        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        Collections.sort(rentalRates);
        
        System.out.printf("%10s%40s%20s%20s%40s%40s%20s\n", "ID","Name", "Rental Rate Type", "Daily Rate", "Start Date", "End Date", "Car Category");
        
        for (RentalRate rentalRate : rentalRates) {
            System.out.printf("%10s%40s%20s%20s%40s%40s%20s\n", rentalRate.getId(), rentalRate.getName(), rentalRate.getRentalRateType(), rentalRate.getDailyRate(), rentalRate.getStartDate(), rentalRate.getEndDate(), rentalRate.getCarCategory().getName());
        }
    }

    private void doViewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Rental Rate Details ***");
        System.out.print("Enter ID of rental rate you want to view> ");
        long rentalRateId = sc.nextLong();

        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateById(rentalRateId);
            System.out.printf("%10s%40s%20s%20s%40s%40s%20s\n", "ID","Name", "Rental Rate Type", "Daily Rate", "Start Date", "End Date", "Car Category");
            System.out.println(rentalRate);
        } catch (Exception e) {
            System.out.println("Rental Rate ID not found!");
        }
    }

    private void doUpdateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Rental Rate Details ***");
        System.out.print("Enter ID of rental rate you want to update> ");
        long rentalRateId = sc.nextLong();
        int response = 0;
        CarCategory carCategory = null;
        
        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateById(rentalRateId);
            
            while (response != 7) {
                System.out.println("Selected rental rate: " + rentalRate.getId());
                System.out.printf("%10s%40s%20s%20s%40s%40s%20s\n", "ID","Name", "Rental Rate Type", "Daily Rate", "Start Date", "End Date", "Car Category");
                System.out.println(rentalRate.toString());
                System.out.println("Which attribute do you want to update?");
                System.out.println("1: Name");
                System.out.println("2: Daily Rate");
                System.out.println("3: Start Date");
                System.out.println("4: End Date");
                System.out.println("5: Car Category");
                System.out.println("6: Rental Rate Type");
                System.out.println("7: Back \n");
                
                System.out.print("Enter attribute you want to update> ");
                response = sc.nextInt();
                sc.nextLine();
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                
                if (response == 1) {
                    System.out.print("Enter new name> ");
                    String name = sc.nextLine();
                    rentalRate.setName(name);
                    
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 2) {
                    System.out.print("Enter new daily rate> ");
                    BigDecimal dailyRate = sc.nextBigDecimal();
                    rentalRate.setDailyRate(dailyRate);
                    
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 3) {
                    System.out.print("Enter new start date (dd-MMM-yyyy)> ");
                    String newStartD = sc.nextLine().trim();
                    
                    try {
                        rentalRate.setStartDate(dateFormat.parse(newStartD));
                    } catch (ParseException ex) {
                        System.out.println("Parse exception occured!");
                    }
                    
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 4) {
                    System.out.print("Enter new end date (dd-MMM-yyyy)> ");
                    String newEndD = sc.nextLine().trim();
                    
                    try {
                        rentalRate.setStartDate(dateFormat.parse(newEndD));
                    } catch (ParseException ex) {
                        System.out.println("Parse exception occured!");
                    }
                    
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 5) {
                    System.out.print("Enter new car category name> ");
                    String carCategoryName = sc.nextLine().trim();
                    
                    try {
                        carCategory = carCategorySessionBeanRemote.retrieveCarCategoryByName(carCategoryName);
                        
                        if (rentalRate.getCarCategory().getRentalRates().contains(rentalRate)) {
                            rentalRate.getCarCategory().getRentalRates().remove(rentalRate);
                        }
                        
                        rentalRate.setCarCategory(carCategory);
                        carCategory.getRentalRates().add(rentalRate);
                        
                    } catch (CarCategoryNotFoundException ex) {
                        System.out.println("Car category not found!");
                    } catch (Exception ex) {
                        System.out.println("Unknown error has occured!");
                    }
                    
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 6) {
                    System.out.print("Enter new rental rate type> ");
                    String rentalRateType = sc.nextLine().trim();
                    
                    rentalRate.setRentalRateType(rentalRateType);
                    rentalRateSessionBeanRemote.mergeRentalRate(rentalRate);
                    
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
                if (response == 7) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Rental rate not found!");
        }
    }

    private void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Rental Rate ***");
        System.out.print("Enter ID of rental rate you want to delete> ");
        long rentalRateId = sc.nextLong();
        
        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateById(rentalRateId);
            System.out.println("Deleted Rental Rate is: " + rentalRateId);
            System.out.printf("%10s%40s%20s%20s%40s%40s%20s\n","ID", "Name", "Rental Rate Type", "Daily Rate", "Start Date", "End Date", "Car Category");
            System.out.println(rentalRate.toString());
            
            rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
            System.out.println("Rental rate: " + rentalRateId + " has been successfully deleted!");
            
        } catch (Exception ex) {
            System.out.println("Rental rate not found!");
        }
    }

    private void menuAboutCarModels() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Operations Manager Terminal :: About Car Models***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: Create Car Model");
            System.out.println("2: View All Car Models");
            System.out.println("3: Update Car Model");
            System.out.println("4: Delete Car Model");
            System.out.println("5: Back\n");
            response = 0;
            
            while (response < 1 || response > 5) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doCreateCarModel();
                } else if (response == 2) {
                    doViewAllCarModels();
                } else if (response == 3) {
                    doUpdateCarModel();
                } else if (response == 4) {
                    doDeleteCarModel();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid response, Please try again!");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }
    private void doCreateCarModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create new car model ***");
        System.out.print("Enter make> ");
        String name = sc.nextLine().trim();
        
        System.out.print("Enter model> ");
        name += " " + sc.nextLine().trim();
        
        System.out.print("Enter car category associated> ");
        String categoryName = sc.nextLine().trim();
        
        CarCategory carCategory = null;
        MakeModel newMakeModel = new MakeModel(name);
        
        try {
            carCategory = carCategorySessionBeanRemote.retrieveCarCategoryByName(categoryName);
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car category not found!");
        }
        
        if (carCategory != null) {
            makeModelEntitySessionBeanRemote.createMakeModel(newMakeModel, carCategory.getId());
            System.out.println("MakeModel: " + name + " has been successfully created!");
        }
    }

    private void doViewAllCarModels() {
        System.out.println("*** View all Car Models ***");

        List<MakeModel> makeModels = makeModelEntitySessionBeanRemote.retrieveAllMakeModels();
        Collections.sort(makeModels);
        
        System.out.printf("%10s%40s%40s%20s\n", "ID", "Name", "Car Category", "Disabled");
        
        
        for (MakeModel makeModel : makeModels) {
            System.out.println(makeModel.toString());
        }
    }

    private void doUpdateCarModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Car Model Details ***");
        System.out.print("Enter ID of car model you want to update> ");
        long carModelId = sc.nextLong();
        int response = 0;
        CarCategory carCategory = null;
        
        try {
            MakeModel makeModel = makeModelEntitySessionBeanRemote.retrieveMakeModelById(carModelId);
            
            while (response != 6) {
                System.out.println("Selected make model: " + makeModel.getId() + " : " + makeModel.getName());
                System.out.printf("%10s%40s%40s\n", "ID","Name", "Car Category");
                System.out.println(makeModel.toString());
                System.out.println("Which attribute do you want to update?");
                System.out.println("1: Name");
                System.out.println("2: Car Category");
                System.out.println("3: Back \n");
                
                System.out.print("Enter attribute you want to update> ");
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                    System.out.print("Enter new name> ");
                    String name = sc.nextLine();
                    makeModel.setName(name);
                    
                    makeModelEntitySessionBeanRemote.mergeMakeModel(makeModel);
                    
                } else if (response == 2) {
                    System.out.print("Enter new car category (name)> ");
                    String carCategoryName = sc.nextLine().trim();
                    
                    try {
                        carCategory = carCategorySessionBeanRemote.retrieveCarCategoryByName(carCategoryName);
                        
                        if (makeModel.getCategory().getListOfMakeModelsIncluded().contains(makeModel)) {
                            makeModel.getCategory().getListOfMakeModelsIncluded().remove(makeModel);
                        }
                        
                        makeModel.setCategory(carCategory);
                        carCategory.getListOfMakeModelsIncluded().add(makeModel);
                        
                    } catch (CarCategoryNotFoundException ex) {
                        System.out.println("Car category not found!");
                    } catch (Exception ex) {
                        System.out.println("Unknown error has occured!");
                    }
                    makeModelEntitySessionBeanRemote.mergeMakeModel(makeModel);
                    
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
                if (response == 3) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Make Model not found!");
        }
    }

    private void doDeleteCarModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Make Model ***");
        System.out.print("Enter ID of Make Model you want to delete> ");
        long makeModelId = sc.nextLong();
        
        try {
            MakeModel makeModel = makeModelEntitySessionBeanRemote.retrieveMakeModelById(makeModelId);
            System.out.println("Deleted Make Model is: " + makeModelId);
            System.out.printf("%10s%40s%40s\n", "ID", "Name", "Car Category");
            System.out.println(makeModel.toString());
            
            makeModelEntitySessionBeanRemote.deleteMakeModel(makeModelId);
            System.out.println("Make Model: " + makeModelId + " has been successfully deleted/set to disabled!");
        } catch (Exception ex) {
            System.out.println("Make Model not found!");
        }
    }
    
    private void menuAboutCars() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Operations Manager Terminal :: About Cars***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: Create Car");
            System.out.println("2: View All Cars");
            System.out.println("3: View specific car detail");
            System.out.println("4: Update Car");
            System.out.println("5: Delete Car");
            System.out.println("6: Back\n");
            response = 0;
            
            while (response < 1 || response > 6) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doCreateCar();
                } else if (response == 2) {
                    doViewAllCar();
                } else if (response == 3) {
                    doViewCarDetails();
                } else if (response == 4) {
                    doUpdateCar();
                } else if (response == 5) {
                    doDeleteCar();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid response, Please try again!");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }
    private void doCreateCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Create new car ***");
        System.out.print("Enter license plate> ");
        String licensePlate = sc.nextLine().trim();
        
        System.out.print("Enter color> ");
        String color = sc.nextLine().trim();
        
        System.out.print("Enter car model associated> ");
        String makeModelName = sc.nextLine().trim();
        
        MakeModel makeModel = null;
        Car newCar = new Car(licensePlate, color);
        
        System.out.println("Current car status");
        System.out.println("1: IN_OUTLET");
        System.out.println("2: ON_RENTAL");
        System.out.print("Enter current status> ");
        int statusNum = sc.nextInt();
        
        if (statusNum == 1) {
            newCar.setCarStatus(CarStatus.IN_OUTLET);
        } else if (statusNum == 2) {
            newCar.setCarStatus(CarStatus.ON_RENTAL);
        } else {
            System.out.println("Invalid option!");
        }
        
        Customer cust = null;
        Outlet outlet = null;
        
        if (newCar.getCarStatus().equals(CarStatus.IN_OUTLET)) {
            System.out.print("Enter Outlet ID> ");
            Long outletId = sc.nextLong();
            
            try {
                outlet = outletEntitySessionBeanRemote.retrieveOutletById(outletId, false);
            } catch (Exception ex) {
                System.out.println("Outlet not found!");
            }
            
        } else {
            System.out.print("Enter Customer ID> ");
            Long custId = sc.nextLong();
            
            try {
                cust = customerEntitySessionBeanRemote.retrieveCustomerById(custId);
            } catch (Exception ex) {
                System.out.println("Customer not found!");
            }
        }
        
        if (cust != null) {
            newCar.setLocationCustomer(cust);
        }
        
        if (outlet != null) {
            newCar.setLocationOutlet(outlet);
        }
        
        try {
            makeModel = makeModelEntitySessionBeanRemote.retrieveMakeModelByName(makeModelName);
        } catch (MakeModelNotFoundException ex) {
            System.out.println("Make model not found!");
        }
        
        if (makeModel != null) {
            carEntitySessionBeanRemote.createCar(newCar, makeModel.getId());
//            makeModel.setUsed(true);
            System.out.println("Car: " + licensePlate + " has been successfully created!");
        }
    }

    private void doViewAllCar() {
        System.out.println("*** View all Car ***");

        List<Car> cars = carEntitySessionBeanRemote.retrieveAllCars();
        Collections.sort(cars);
        
        System.out.printf("%10s%40s%40s%40s%20s%20s%40s%40s\n", "ID","License Plate", "Car Model", "Car Category", "Color","Car Status", "Outlet Location", "Current Customer");
        
        for (Car car : cars) {
            System.out.print(car.toString());
        }
    }
    
        private void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Car Details ***");
        System.out.print("Enter ID of car you want to view> ");
        long carId = sc.nextLong();

        try {
            Car car = carEntitySessionBeanRemote.retrieveCarById(carId);
            System.out.printf("%10s%40s%40s%40s%20s%20s%40s%40s\n","ID", "License Plate", "Car Model", "Car Category", "Color", "Car Status" ,"Outlet Location", "Current Customer");
            System.out.println(car.toString());
        } catch (Exception e) {
            System.out.println("Car ID not found!");
        }
    }


    private void doUpdateCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Car Details ***");
        System.out.print("Enter ID of car you want to update> ");
        long carId = sc.nextLong();
        int response = 0;
        Car car = null;
        
        try {
            car = carEntitySessionBeanRemote.retrieveCarById(carId);
            
            while (response != 6) {
                System.out.println("Selected car: " + car.getLicensePlate() + " : " + car.getMakeModel().getName());
                System.out.printf("%10s%40s%40s%40s%20s%20s%40s%40s\n", "ID", "License Plate", "Car Model", "Car Category", "Color","Car Status", "Outlet Location", "Current Customer");
                System.out.println(car.toString());
                System.out.println("Which attribute do you want to update?");
                System.out.println("1: License Plate");
                System.out.println("2: Color");
                System.out.println("3: Status");
                System.out.println("4: Current Outlet");
                System.out.println("5: Car Model");
                System.out.println("6: Back \n");
                
                System.out.print("Enter attribute you want to update> ");
                response = sc.nextInt();
                sc.nextLine();
                
                if (response == 1) {
                    System.out.print("Enter new license plate> ");
                    String licensePlate = sc.nextLine();
                    car.setLicensePlate(licensePlate);
                    
                    carEntitySessionBeanRemote.mergeCar(car);
                    
                } else if (response == 2) {
                    System.out.print("Enter new car color> ");
                    String carColor = sc.nextLine().trim();
                    car.setColour(carColor);
                    
                    carEntitySessionBeanRemote.mergeCar(car);
                } else if (response == 3) {
                    System.out.println("1: IN_OUTLET");
                    System.out.println("2: ON_RENTAL");
                    System.out.print("Enter new car status> ");
                    int statusNum = sc.nextInt();
                    
                    if (statusNum == 1) {
                        car.setCarStatus(CarStatus.IN_OUTLET);
                    } else if (statusNum == 2) {
                        car.setCarStatus(CarStatus.ON_RENTAL);
                    } else {
                        System.out.println("Invalid option");
                    }
                    
                    carEntitySessionBeanRemote.mergeCar(car);
                } else if (response == 4) {
                    System.out.print("Enter new car outlet location ID> ");
                    long outletId = sc.nextLong();
                    
                    Outlet outlet = null;
                    
                    try {
                        outlet = outletEntitySessionBeanRemote.retrieveOutletById(outletId, false);
                    } catch (Exception ex) {
                        System.out.println("Outlet not found!");
                    }
                    
                    if (outlet != null) {
                        car.setLocationOutlet(outlet);
                    }
                    
                    carEntitySessionBeanRemote.mergeCar(car);
                    
                } else if (response == 5) {
                    System.out.print("Enter new car model name> ");
                    String carModelName = sc.nextLine().trim();
                    MakeModel makeModel = null;
                    
                    try {
                        makeModel = makeModelEntitySessionBeanRemote.retrieveMakeModelByName(carModelName);
                    } catch (MakeModelNotFoundException ex) {
                        System.out.println("Car model not found!");
                    } 
                    
                    if (makeModel != null) {
                        car.setMakeModel(makeModel);
                    }
                    
                    carEntitySessionBeanRemote.mergeCar(car);
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
                if (response == 3) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Car not found!");
        }
    }

    private void doDeleteCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Car ***");
        System.out.print("Enter ID of Car you want to delete> ");
        long carId = sc.nextLong();
        
        try {
            Car car = carEntitySessionBeanRemote.retrieveCarById(carId);
            System.out.printf("%10s%40s%40s%40s%20s%40s%40s\n","ID", "License Plate", "Car Model", "Car Category", "Color", "Car Status","Outlet Location", "Current Customer");
            System.out.println(car.toString());
            System.out.println("Deleted Car is: " + carId);
            
            carEntitySessionBeanRemote.deleteCar(carId);
            System.out.println("Car: " + carId + " has been successfully deleted!");
        } catch (Exception ex) {
            System.out.println("Car not found!");
        }
    }

    private void menuAboutTransits() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Operations Manager Terminal :: About Transits***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("2: Assign Transit Driver");
            System.out.println("3: Update Transit As Completed");
            System.out.println("4: Back\n");
            response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doViewTransitDriverDispatchRecordForCurrentDayReservations();
                } else if (response == 2) {
                    doAssignTransitDriver();
                } else if (response == 3) {
                    doUpdateTransitAsCompleted();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid response, Please try again!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doViewTransitDriverDispatchRecordForCurrentDayReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View all Transit Driver Dispatch Record For Current Day Reservations ***");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy-HHmm");
        Date currDate = new Date();
        Date endDate = new Date();
        
        System.out.print("Enter current date (dd-MMM-yyyy)> ");
        String date = sc.nextLine();
        date += "-0000";
        
        try {
            currDate = dateFormat.parse(date);
            endDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Parse Exception Occured!");
        }
  
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.DATE, 1);
        
        endDate = c.getTime();
        
        List<TransitDriverDispatchRecord> dispatchesFrom = dispatchSessionBeanRemote.retrieveDispatchByDateAndStartOutlet(currDate, endDate, currentEmployee.getOutlet().getId());
        List<TransitDriverDispatchRecord> dispatchesTo = dispatchSessionBeanRemote.retrieveDispatchByDateAndEndOutlet(currDate, endDate, currentEmployee.getOutlet().getId());
        
        System.out.printf("%10s%40s%40s%40s%40s%10s%40s\n", "ID", "From Outlet", "To Outlet", "Car", "Employee Assigned", "Complete", "Reservation");
        System.out.println("From: " + currentEmployee.getOutlet().getAddress());
        
        for (TransitDriverDispatchRecord d : dispatchesFrom) {
            System.out.println(d.toString());
        }
        
        System.out.println();
        System.out.println("To: " + currentEmployee.getOutlet().getAddress());
        
        for (TransitDriverDispatchRecord d : dispatchesTo) {
            System.out.println(d.toString());
        }
        
        System.out.println();
    }
    
    private void doAssignTransitDriver() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Assign Transit Driver ***");
        System.out.print("Enter Dispatch Id> ");
        long dispatchId = sc.nextLong();
        
        try {
            TransitDriverDispatchRecord dispatch = dispatchSessionBeanRemote.retrieveDispatchById(dispatchId);
            
            System.out.print("Enter Employee Id to assign to Dispatch> ");
            long employeeId = sc.nextLong();
            
            Employee employee = employeeSessionBeanRemote.retrieveEmployeeById(employeeId);
            
            if (employee == null) {
                System.out.println("Employee not found!");
                return;
            }
            
            if (dispatch.isComplete()) {
                System.out.println("Dispatch has already been completed!");
                return;
            }
            
            if (!employee.getOutlet().equals(dispatch.getFromOutlet())) {
                System.out.println("Employee not from this outlet!");
                return;
            }
            
            //If already assigned, remove
            if (dispatch.getEmployeeAssigned() != null && dispatch.getEmployeeAssigned().getDispatches().contains(dispatch)) {
                dispatch.getEmployeeAssigned().getDispatches().remove(dispatch);
            }
            
            dispatch.setEmployeeAssigned(employee);
            employee.getDispatches().add(dispatch);
            System.out.println("Employee: " + employeeId + " has been assigned to Dispatch: " + dispatchId);
            dispatchSessionBeanRemote.mergeDispatch(dispatch);
            
        } catch (DispatchNotFoundException ex) {
            System.out.println("Dispatch not found!");
        }
    }
    
    private void doUpdateTransitAsCompleted() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Transit As Completed ***");
        System.out.print("Enter Dispatch Id> ");
        long dispatchId = sc.nextLong();
        
        TransitDriverDispatchRecord dispatch = null;
        
        try {
            dispatch = dispatchSessionBeanRemote.retrieveDispatchById(dispatchId);
        } catch (DispatchNotFoundException ex) {
            System.out.println("Dispatch not found!");
        }
        
        if (dispatch != null) {
            dispatch.setComplete(true);
            dispatch.getCarMoved().setLocationOutlet(dispatch.getToOutlet());
            
            if (dispatch.getToOutlet().getCars().contains(dispatch.getCarMoved())) {
                dispatch.getToOutlet().getCars().add(dispatch.getCarMoved());
                outletEntitySessionBeanRemote.mergeOutlet(dispatch.getToOutlet());
            }
            
            carEntitySessionBeanRemote.mergeCar(dispatch.getCarMoved());
            dispatchSessionBeanRemote.mergeDispatch(dispatch);
            System.out.println("*** Transit: " + dispatchId + " has been marked as completed!");
        }
    }
    
    private void doPickupCar() {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        Customer cust = null;
        Reservation reservation = null;
        
        System.out.print("Enter customer email> ");
        String email = sc.nextLine().trim();
            
        cust = customerEntitySessionBeanRemote.retrieveCustomerByEmail(email);
            
        System.out.print("Enter Reservation ID> ");
        long reservationId = sc.nextLong();
            
        reservation = reservationEntitySessionBeanRemote.retrieveReservationById(reservationId);
        
        if (cust != null && reservation != null) {
            if (!reservation.isIsPaid()) {
                System.out.println("Payment for reservation: " + reservationId + " has not been made.");
                System.out.println("1: Customer has made payment.");
                System.out.println("2: Cancel");
                System.out.print("Enter choice> ");
                response = sc.nextInt();
                
                if (response == 1) {
                    reservation.setIsPaid(true);
                    reservation.setReservationStatus(ReservationStatus.OUT);
                    reservationEntitySessionBeanRemote.mergeReservation(reservation);
                    
                    Car car = reservation.getCar();
                    car.setLocationCustomer(cust);
                    car.setCarStatus(CarStatus.ON_RENTAL);
                    car.setLocationOutlet(null);
                    carEntitySessionBeanRemote.mergeCar(car);
                    
                    System.out.print("Enter outlet ID> ");
                    long outletId = sc.nextLong();
                    
                    Outlet outlet = outletEntitySessionBeanRemote.retrieveOutletById(outletId, false);
                    
                    if (outlet.getCars().contains(car)) {
                        outlet.getCars().remove(car);
                        outletEntitySessionBeanRemote.mergeOutlet(outlet);
                    }
                    
                    System.out.println("Pickup of car: " + car.getLicensePlate() + " has been completed successfully.");
                    
                } else if (response == 2) {
                    System.out.println("Pickup has been cancelled");
                } else {
                    System.out.println("Pickup has been cancelled");
                }
            } else {
                System.out.println("This reservation has already been picked up!");
            }
        } else {
            System.out.println("Customer or reservation does not exist in our system!");
        }
    }
    
    private void doReturnCar() {
        Scanner sc = new Scanner(System.in);
        
        Reservation reservation = null;
        
        System.out.print("Enter Reservation ID> ");
        long reservationId = sc.nextLong();
        
        reservation = reservationEntitySessionBeanRemote.retrieveReservationById(reservationId);
        
        if (reservation != null) {
            if (!reservation.getReservationStatus().equals(ReservationStatus.OUT)) {
                System.out.println("This reservation is not on rental at the moment.");
                return;
            }
            
            reservation.setReservationStatus(ReservationStatus.RETURNED);
            reservationEntitySessionBeanRemote.mergeReservation(reservation);
            
            Car car = reservation.getCar();
            Outlet outlet = reservation.getReturnOutlet();
            car.setLocationOutlet(outlet);
            car.setLocationCustomer(null);
            car.setCarStatus(CarStatus.IN_OUTLET);
            
            carEntitySessionBeanRemote.mergeCar(car);
            
            if (!outlet.getCars().contains(car)) {
                outlet.getCars().add(car);
                outletEntitySessionBeanRemote.mergeOutlet(outlet);
                System.out.println("Return of car: " + car.getLicensePlate() + " has been completed successfully.");
            } else {
                System.out.println("Car lost? Shouldnt be here some bug probably.");
            }
        }
    }

    private void menuGeneral() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** General Employee Terminal ***\n");
            System.out.println("You are logged in as " + currentEmployee.getEmployeeRole() + " " + currentEmployee.getName() + "\n");
            System.out.println("1: Allocate Cars To Current Day Reservations");
            System.out.println("2: Generate Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("3: Logout\n");
            response = 0;
            
            while (response < 1 || response > 3) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doAllocateCarsToCurrentDayReservations();
                } else if (response == 2) {
                    doGenerateTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doAllocateCarsToCurrentDayReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter current date (dd-MM-yyyy)> ");
        String date = sc.nextLine().trim();
        date += "-0000";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HHmm");
        
        Date currDate = new Date();
        
        try {
            currDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Parse exception occured!");
        }
        
        List<String> printStuff = ejbTimerSessionBeanRemote.allocateCarsToCurrentDayReservations(currDate);
        
        for(String s : printStuff) {
            System.out.println(s);
        }
    }

    private void doGenerateTransitDriverDispatchRecordsForCurrentDayReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter current date (dd-MM-yyyy)> ");
        String date = sc.nextLine().trim();
        date += "-0000";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HHmm");
        
        Date currDate = new Date();
        Date endDate = new Date();
        
        try {
            currDate = dateFormat.parse(date);
            endDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Parse exception occured!");
        }
        
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.DATE, 1);
        
        endDate = c.getTime();
        
        
//        Outlet outlet = currentEmployee.getOutlet();
//        outlet = outletEntitySessionBeanRemote.retrieveOutletById(outletId, false);
        
        List<Reservation> reservations = reservationEntitySessionBeanRemote.retrieveReservationBetweenTwoDates(currDate, endDate);
        List<Reservation> toGenerate = new ArrayList<>();
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations for today!");
            return;
        }
                    
        for (Reservation r : reservations) {
            //Add to generate list if car not at current outlet and not cancelled.
            Car reservationCar = r.getCar();
            if (reservationCar == null) continue;
            if (!r.getPickupOutlet().equals(reservationCar.getLocationOutlet())) {
                if (r.getReservationStatus().equals(ReservationStatus.RESERVED) && r.getDispatch() == null) {
                    toGenerate.add(r);
                }
            }
        }
        
        if (toGenerate.isEmpty()) {
            System.out.println("No cars to allocate for these reservations.");
            return;
        }
        
        for (Reservation r : toGenerate) {
            TransitDriverDispatchRecord dispatch = new TransitDriverDispatchRecord();
            
            Car car = r.getCar();
            
            dispatch.setReservation(r);
            dispatch.setCarMoved(car);
            dispatch.setFromOutlet(car.getLocationOutlet());
            dispatch.setToOutlet(r.getPickupOutlet());
            dispatchSessionBeanRemote.createDispatchWithReservation(dispatch, r);
            
            System.out.println("Dispatch has been created for Reservation ID: " + r.getId());
            System.out.println("Car: " + car.getMakeModel().getName() + " " + car.getLicensePlate() + " From: " + dispatch.getFromOutlet() + " To: " + dispatch.getToOutlet().getAddress());
        }
    }
}
