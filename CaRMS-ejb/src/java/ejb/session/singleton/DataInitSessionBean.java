/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategoryEntitySessionBeanLocal;
import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.DispatchSessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.MakeModelEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.Employee;
import entity.MakeModel;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRole;
import util.enumeration.ReservationStatus;
import util.exception.CarCategoryNotFoundException;
import util.exception.MakeModelNotFoundException;

/**
 *
 * @author wangp
 */
@Singleton
@LocalBean
//@Startup
public class DataInitSessionBean {

    @EJB
    private DispatchSessionBeanLocal dispatchSessionBean;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private MakeModelEntitySessionBeanLocal makeModelEntitySessionBean;

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBean;

    @EJB
    private CarCategoryEntitySessionBeanLocal carCategoryEntitySessionBean;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBean;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBean;
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
//        for (int i = 0; i < 100; i++) {
//            System.out.println("TEST TEST TEST");
//        }
        if (customerEntitySessionBean.retrieveAllCustomers().isEmpty()) {
            Customer c = new Customer("Wang Poh San", "wangpohshan@gmail.com", "81843067", "999-999-999", "hallo");
            customerEntitySessionBean.createCustomer(c);
            em.flush();
        }

        if (outletEntitySessionBean.retrieveAllOutlets(true).isEmpty()) {

            Outlet o = new Outlet("Somewhere street", "07:00", "18:00");
            Outlet o1 = new Outlet("Elsewhere street", "07:00", "18:00");
            outletEntitySessionBean.createOutlet(o);
            outletEntitySessionBean.createOutlet(o1);
            em.flush();

            Employee e = new Employee("salesmanager", "salesmanager", "password", EmployeeRole.SALES_MANAGER);
            employeeEntitySessionBean.createEmployee(e, o.getId());

            Employee e1 = new Employee("opsmanager", "opsmanager", "password", EmployeeRole.OPS_MANAGER);
            employeeEntitySessionBean.createEmployee(e1, o.getId());

            Employee e2 = new Employee("customerserviceexecutive", "customerserviceexecutive", "password", EmployeeRole.CUSTOMER_SERVICE_EXECUTIVE);
            employeeEntitySessionBean.createEmployee(e2, o.getId());
            
            Employee e3 = new Employee("employee", "employee", "password", EmployeeRole.EMPLOYEE);
            employeeEntitySessionBean.createEmployee(e3, o.getId());
        }

        if (carCategoryEntitySessionBean.retrieveAllCarCategories().isEmpty()) {
            CarCategory cc1 = new CarCategory("Standard Sedan");
            CarCategory cc2 = new CarCategory("Family Sedan");
            CarCategory cc3 = new CarCategory("Luxury Sedan");
            CarCategory cc4 = new CarCategory("SUV and Minivan");

            carCategoryEntitySessionBean.createCarCategory(cc1);
            carCategoryEntitySessionBean.createCarCategory(cc2);
            carCategoryEntitySessionBean.createCarCategory(cc3);
            carCategoryEntitySessionBean.createCarCategory(cc4);
            em.flush();
            System.out.println("CAR CATEGORY IS " + em.find(CarCategory.class, 1l));
        }

        if (rentalRateSessionBean.retrieveAllRentalRates().isEmpty()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HHmm");

            Date startDate1 = null;
            Date endDate1 = null;
            try {
                startDate1 = df.parse("23-04-2022-1200");
                endDate1 = df.parse("05-05-2022-2359");
            } catch (ParseException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            RentalRate rr1 = new RentalRate("Luxury Sedan: Super Discount", new BigDecimal(5), startDate1, endDate1);

            Date startDate2 = null;
            Date endDate2 = null;
            try {
                startDate2 = df.parse("01-01-1980-1234");
                endDate2 = df.parse("01-01-3000-2345");
            } catch (ParseException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            RentalRate rr2 = new RentalRate("Luxury Sedan: Base Rate", new BigDecimal(10), startDate2, endDate2);

            CarCategory c1 = null;
            try {
                c1 = carCategoryEntitySessionBean.retrieveCarCategoryByName("Luxury Sedan");
            } catch (CarCategoryNotFoundException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            rentalRateSessionBean.createRentalRate(rr1, c1.getId());
            rentalRateSessionBean.createRentalRate(rr2, c1.getId());

            RentalRate rr3 = new RentalRate("Family Sedan: Base Rate", new BigDecimal(10), null, null);
            CarCategory c2 = null;
            try {
                c2 = carCategoryEntitySessionBean.retrieveCarCategoryByName("Family Sedan");
            } catch (CarCategoryNotFoundException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            rentalRateSessionBean.createRentalRate(rr3, c2.getId());
            em.flush();

        }

        if (makeModelEntitySessionBean.retrieveAllMakeModels().isEmpty()) {
            MakeModel mm1 = new MakeModel("Toyota Prius");
            CarCategory c1 = null;
            try {
                c1 = carCategoryEntitySessionBean.retrieveCarCategoryByName("Family Sedan");
            } catch (CarCategoryNotFoundException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            makeModelEntitySessionBean.createMakeModel(mm1, c1.getId()); // same for Category;
            em.flush();
        }
        if (carEntitySessionBean.retrieveAllCars().isEmpty()) {
            
            MakeModel m1 = null;
            
            try {
                m1 = makeModelEntitySessionBean.retrieveMakeModelByName("Toyota Prius");
            } catch (MakeModelNotFoundException ex) {
                System.out.println("Make model not found!");
            }
            
            Car car1 = new Car("ABC-123", "Green");
            car1.setLocationOutlet(em.find(Outlet.class, 1l));

            if (m1 == null) {
                return;
            }
            
            carEntitySessionBean.createCar(car1, m1.getId()); // 1l is the id of makemodel

            Car car2 = new Car("DEF-456", "Blue");
            car2.setLocationOutlet(em.find(Outlet.class, 1l));
            carEntitySessionBean.createCar(car2, m1.getId());

            Car car3 = new Car("XYZ-789", "Red");
            car3.setLocationOutlet(em.find(Outlet.class, 1l));
            carEntitySessionBean.createCar(car3, m1.getId());

        }

        if (reservationEntitySessionBean.retrieveAllReservations().isEmpty()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-HHmm");
            Date collectDate1 = null;
            Date returnDate1 = null;
            try {
                collectDate1 = df.parse("23-APR-2022-1200");
                returnDate1 = df.parse("05-MAY-2022-2359");
            } catch (ParseException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            Reservation res1 = new Reservation(collectDate1, returnDate1, BigDecimal.ZERO, ReservationStatus.RESERVED);
            reservationEntitySessionBean.createReservation(res1, 1l, 1l, 1l, 2l);

        }

        if (partnerEntitySessionBean.retrieveAllPartners().isEmpty()) {
            Partner p = new Partner("foobar@holiday.com", "password", "1234568", "Foo Bar");
            partnerEntitySessionBean.createPartner(p);
        }
        
        if (dispatchSessionBean.retrieveAllDispatches().isEmpty()) {
            TransitDriverDispatchRecord dispatch = new TransitDriverDispatchRecord();
            dispatch.setCarMoved(carEntitySessionBean.retrieveAllCars().get(0));
            dispatch.setEmployeeAssigned(employeeEntitySessionBean.retrieveEmployeeById((long) 1));
            
            dispatch.setFromOutlet(outletEntitySessionBean.retrieveOutletById((long) 1, false));
            dispatch.setToOutlet(outletEntitySessionBean.retrieveOutletById((long) 2, false));
            dispatch.setReservation(reservationEntitySessionBean.retrieveReservationById((long) 1));
            
            dispatchSessionBean.createDispatch(dispatch);
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
