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
import entity.Employee;
import entity.MakeModel;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatus;
import util.enumeration.EmployeeRole;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author ryanl
 */
@Singleton
@LocalBean
@Startup
public class TestDataSessionBean {
    
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
        if (outletEntitySessionBean.retrieveAllOutlets(true).isEmpty()) {
            Outlet a = new Outlet("Outlet A", "00:00", "00:00"); //This indicates 24h ba
            Outlet b = new Outlet("Outlet B", "00:00", "00:00");
            Outlet c = new Outlet("Outlet C", "08:00", "22:00");
            outletEntitySessionBean.createOutlet(a);
            outletEntitySessionBean.createOutlet(b);
            outletEntitySessionBean.createOutlet(c);
            em.flush();
            
            
            //Outlet A
            Employee a1 = new Employee("Employee A1", "a1", "p", EmployeeRole.SALES_MANAGER);
            employeeEntitySessionBean.createEmployee(a1, a.getId());
            Employee a2 = new Employee("Employee A2", "a2", "p", EmployeeRole.OPS_MANAGER);
            employeeEntitySessionBean.createEmployee(a2, a.getId());
            Employee a3 = new Employee("Employee A3", "a3", "p", EmployeeRole.CUSTOMER_SERVICE_EXECUTIVE);
            employeeEntitySessionBean.createEmployee(a3, a.getId());
            Employee a4 = new Employee("Employee A4", "a4", "p", EmployeeRole.EMPLOYEE);
            employeeEntitySessionBean.createEmployee(a4, a.getId());
            Employee a5 = new Employee("Employee A5", "a5", "p", EmployeeRole.EMPLOYEE);
            employeeEntitySessionBean.createEmployee(a5, a.getId());
            
            //Outlet B
            Employee b1 = new Employee("Employee B1", "b1", "p", EmployeeRole.SALES_MANAGER);
            employeeEntitySessionBean.createEmployee(b1, b.getId());
            Employee b2 = new Employee("Employee B2", "b2", "p", EmployeeRole.OPS_MANAGER);
            employeeEntitySessionBean.createEmployee(b2, b.getId());
            Employee b3 = new Employee("Employee B3", "b3", "p", EmployeeRole.CUSTOMER_SERVICE_EXECUTIVE);
            employeeEntitySessionBean.createEmployee(b3, b.getId());
            
            //Outlet C
            Employee c1 = new Employee("Employee C1", "c1", "p", EmployeeRole.SALES_MANAGER);
            employeeEntitySessionBean.createEmployee(c1, c.getId());
            Employee c2 = new Employee("Employee C2", "c2", "p", EmployeeRole.OPS_MANAGER);
            employeeEntitySessionBean.createEmployee(c2, c.getId());
            Employee c3 = new Employee("Employee C3", "c3", "p", EmployeeRole.CUSTOMER_SERVICE_EXECUTIVE);
            employeeEntitySessionBean.createEmployee(c3, c.getId());
            em.flush();
            
            //Car category
            CarCategory cc1 = new CarCategory("Standard Sedan");
            CarCategory cc2 = new CarCategory("Family Sedan");
            CarCategory cc3 = new CarCategory("Luxury Sedan");
            CarCategory cc4 = new CarCategory("SUV and Minivan");

            carCategoryEntitySessionBean.createCarCategory(cc1);
            carCategoryEntitySessionBean.createCarCategory(cc2);
            carCategoryEntitySessionBean.createCarCategory(cc3);
            carCategoryEntitySessionBean.createCarCategory(cc4);
            em.flush();
            
            //Model
            MakeModel mm1 = new MakeModel("Toyota Corolla");
            makeModelEntitySessionBean.createMakeModel(mm1, cc1.getId());
            MakeModel mm2 = new MakeModel("Honda Civic");
            makeModelEntitySessionBean.createMakeModel(mm2, cc1.getId());
            MakeModel mm3 = new MakeModel("Nissan Sunny");
            makeModelEntitySessionBean.createMakeModel(mm3, cc1.getId());
            MakeModel mm4 = new MakeModel("Mercedes E Class");
            makeModelEntitySessionBean.createMakeModel(mm4, cc3.getId());
            MakeModel mm5 = new MakeModel("BMW 5 Series");
            makeModelEntitySessionBean.createMakeModel(mm5, cc3.getId());
            MakeModel mm6 = new MakeModel("Audi A6");
            makeModelEntitySessionBean.createMakeModel(mm6, cc3.getId());
            em.flush();
            
            //Car
            Car car1 = new Car("SS00A1TC", "Green");
            car1.setLocationOutlet(a);
            carEntitySessionBean.createCar(car1, mm1.getId());
            Car car2 = new Car("SS00A2TC", "Green");
            car2.setLocationOutlet(a);
            carEntitySessionBean.createCar(car2, mm1.getId());
            Car car3 = new Car("SS00A3TC", "Green");
            car3.setLocationOutlet(a);
            carEntitySessionBean.createCar(car3, mm1.getId());
            
            Car car4 = new Car("SS00B1HC", "Green");
            car4.setLocationOutlet(b);
            carEntitySessionBean.createCar(car4, mm2.getId());
            Car car5 = new Car("SS00B2HC", "Green");
            car5.setLocationOutlet(b);
            carEntitySessionBean.createCar(car5, mm2.getId());
            Car car6 = new Car("SS00B3HC", "Green");
            car6.setLocationOutlet(b);
            carEntitySessionBean.createCar(car6, mm2.getId());
            
            Car car7 = new Car("SS00C1NS", "Green");
            car7.setLocationOutlet(c);
            carEntitySessionBean.createCar(car7, mm3.getId());
            Car car8 = new Car("SS00C2NS", "Green");
            car8.setLocationOutlet(c);
            carEntitySessionBean.createCar(car8, mm3.getId());
            Car car9 = new Car("SS00C3NS", "Green");
            car9.setLocationOutlet(c);
            car9.setCarStatus(CarStatus.REPAIR);
            carEntitySessionBean.createCar(car9, mm3.getId());
            
            Car car11 = new Car("LS00A4ME", "Green");
            car11.setLocationOutlet(a);
            carEntitySessionBean.createCar(car11, mm4.getId());
            Car car12 = new Car("LS00B4B5", "Green");
            car12.setLocationOutlet(b);
            carEntitySessionBean.createCar(car12, mm5.getId());
            Car car13 = new Car("LS00C4A6", "Green");
            car13.setLocationOutlet(c);
            carEntitySessionBean.createCar(car13, mm6.getId());
            em.flush();
            
            //Rental rate
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

            Date startDate1 = null;
            Date endDate1 = null;
            
            //Always valid - create null ones first
            RentalRate rr1 = new RentalRate("Standard Sedan - Default", "Default", new BigDecimal(100), startDate1, endDate1);
            RentalRate rr2 = new RentalRate("Family Sedan - Default", "Default", new BigDecimal(200), startDate1, endDate1);
            RentalRate rr3 = new RentalRate("Luxury Sedan - Default", "Default", new BigDecimal(300), startDate1, endDate1);
            RentalRate rr4 = new RentalRate("SUV and Minivan - Default", "Default", new BigDecimal(400), startDate1, endDate1);
            
            try {
                startDate1 = df.parse("09/12/2022-12:00");
                endDate1 = df.parse("11/12/2022-00:00");
            } catch (ParseException ex) {
                System.out.println(":(");
            }
            RentalRate rr5 = new RentalRate("Standard Sedan - Weekend Promo", "Promotion", new BigDecimal(80), startDate1, endDate1);
            
            try {
                startDate1 = df.parse("05/12/2022-00:00");
                endDate1 = df.parse("05/12/2022-23:59");
            } catch (ParseException ex) {
                System.out.println(":(");
            }
            RentalRate rr6 = new RentalRate("Luxury Sedan - Monday", "Peak", new BigDecimal(310), startDate1, endDate1);
            
            try {
                startDate1 = df.parse("06/12/2022-00:00");
                endDate1 = df.parse("06/12/2022-23:59");
            } catch (ParseException ex) {
                System.out.println(":(");
            }
            RentalRate rr7 = new RentalRate("Luxury Sedan - Tuesday", "Peak", new BigDecimal(320), startDate1, endDate1);
            
            try {
                startDate1 = df.parse("07/12/2022-00:00");
                endDate1 = df.parse("07/12/2022-23:59");
            } catch (ParseException ex) {
                System.out.println(":(");
            }
            RentalRate rr8 = new RentalRate("Luxury Sedan - Wednesday", "Peak", new BigDecimal(330), startDate1, endDate1);
            
            try {
                startDate1 = df.parse("07/12/2022-12:00");
                endDate1 = df.parse("08/12/2022-12:00");
            } catch (ParseException ex) {
                System.out.println(":(");
            }
            RentalRate rr9 = new RentalRate("Luxury Sedan - Weekday Promo", "Promotion", new BigDecimal(250), startDate1, endDate1);
            em.flush();
            
            
            //Actually creating rental rates
            rentalRateSessionBean.createRentalRate(rr1, cc1.getId());
            rentalRateSessionBean.createRentalRate(rr2, cc2.getId());
            rentalRateSessionBean.createRentalRate(rr3, cc3.getId());
            rentalRateSessionBean.createRentalRate(rr4, cc4.getId());
            
            rentalRateSessionBean.createRentalRate(rr5, cc1.getId());
            
            rentalRateSessionBean.createRentalRate(rr6, cc3.getId());
            rentalRateSessionBean.createRentalRate(rr7, cc3.getId());
            rentalRateSessionBean.createRentalRate(rr8, cc3.getId());
            
            rentalRateSessionBean.createRentalRate(rr9, cc3.getId());
            em.flush();
            
            //Create partner
            Partner p = new Partner("test@holiday.com", "password", "1234568", "Holiday.com");
            partnerEntitySessionBean.createPartner(p);
            em.flush();
        }
    }
    
}
