/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.SearchReserveCarSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.MakeModel;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wangp
 */
@WebService(serviceName = "PartnerReservationWebService")
@Stateless()
public class PartnerReservationWebService {

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBean;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBean;

    @EJB
    private SearchReserveCarSessionBeanLocal searchReserveCarSessionBean;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private CarCategoryEntitySessionBeanLocal carCategoryEntitySessionBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "retrievePartnerByEmailPassword")
    public Partner retrievePartnerByEmailPassword(String email, String password) {

        Partner partnerFound = partnerEntitySessionBean.retrievePartnerByEmailPassword(email, password);
        // no bidirectional relationship in partners so no need nullify rlnships
        return partnerFound;
    }

    /**
     * Web service operation
     *
     * @param chosenCategory
     * @param pickupDate
     * @param returnOutlet
     * @param returnDate
     * @param pickupOutlet
     * @return
     */
    @WebMethod(operationName = "partnerSearchCar")
    public List<Reservation> partnerSearchCar(CarCategory chosenCategory,
            Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet) {
        Pair<List<Reservation>, List<RentalRate>> pairReturned = searchReserveCarSessionBean.searchAvailableCarReservations(chosenCategory, pickupDate, returnDate, pickupOutlet, returnOutlet);

        List<Reservation> reservationsAvailable = pairReturned.getKey();
        for (Reservation r : reservationsAvailable) {
            em.detach(r);
            r.setPickupOutlet(null);
            r.setReturnOutlet(null);
            // customer & car already null
        }
        return reservationsAvailable;
    }

    /**
     * Web service operation
     *
     * @param chosenCategory
     * @param pickupDate
     * @param returnDate
     * @param pickupOutlet
     * @param returnOutlet
     * @return
     */
    @WebMethod(operationName = "partnerSearchCarGetUsedRentals")
    public List<RentalRate> partnerSearchCarGetUsedRentals(CarCategory chosenCategory,
            Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet) {
        //TODO write your implementation code here:
        Pair<List<Reservation>, List<RentalRate>> pairReturned = searchReserveCarSessionBean.searchAvailableCarReservations(chosenCategory, pickupDate, returnDate, pickupOutlet, returnOutlet);
        List<RentalRate> usedRentals = pairReturned.getValue();
        for (RentalRate r : usedRentals) {
            em.detach(r);
            r.setCarCategory(null);
        }

        return usedRentals;
    }

    /**
     * Web service operation
     *
     * @param reservation
     * @param usedRentalRates
     * @param customer
     * @param partner
     * @param isPaidImmediate
     */
    @WebMethod(operationName = "partnerReserveCar")
    public void partnerReserveCar(Reservation reservation, List<RentalRate> usedRentalRates,
            Customer customer, Long pickupOutletId, Long returnOutletId, Long partnerId, boolean isPaidImmediate) {
        // check if customer exists in db
        Customer customerRetrieved = customerEntitySessionBean.retrieveCustomerByEmail(customer.getEmail());
        if (customerRetrieved == null) {
            customerEntitySessionBean.createCustomer(customer);
            //need to retrieve again to get the generated id
            customerRetrieved = customerEntitySessionBean.retrieveCustomerByEmail(customer.getEmail());
        } else {
            customerRetrieved.setCreditCard(customer.getCreditCard());
            customerRetrieved.setName(customer.getName());
            customerRetrieved.setPassword(customer.getPassword());
            customerRetrieved.setPhone(customer.getPhone());
            customerEntitySessionBean.updateCustomer(customerRetrieved);
        }

        searchReserveCarSessionBean.confirmReservationWithPartner(reservation, usedRentalRates,
                customerRetrieved.getId(), pickupOutletId,
                returnOutletId, isPaidImmediate, partnerId);

    }

    /**
     * Web service operation
     *
     * @return
     */
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<Outlet> retrieveAllOutlets() {
        //TODO write your implementation code here:
        List<Outlet> listOfOutlets = outletEntitySessionBean.retrieveAllOutlets(true);// does this work? we can test
        for (Outlet o : listOfOutlets) {
            em.detach(o);
            o.setDispatches(null);
            List<Car> listCars = o.getCars();
            for (Car c : listCars) {
                em.detach(c);
                c.setMakeModel(null);
                c.setLocationOutlet(null);
                c.setLocationCustomer(null);
            }
        
//            for (Employee e : o.getEmployees()) {
//                em.detach(e);
//                System.out.println("employee is: " + e);
//                e.setOutlet(null);
//            }
        o.setEmployees(null); // we dont need employees anyway
    }
    return listOfOutlets ;
}

/**
 * Web service operation
 *
 * @return
 */
@WebMethod(operationName = "retrieveAllCarCategories")
        public List<CarCategory> retrieveAllCarCategories() {
        List<CarCategory> allCats = carCategoryEntitySessionBean.retrieveAllCarCategories();

        for (CarCategory c : allCats) {
            em.detach(c);
            for (MakeModel m : c.getListOfMakeModelsIncluded()) {
                em.detach(m);
                m.setCategory(null);
            }

            for (RentalRate r : c.getRentalRates()) {
                em.detach(r);
                r.setCarCategory(null);
            }
        }
        return allCats;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "payReservation")
        public String payReservation(Reservation r, Customer c) {
        return searchReserveCarSessionBean.payReservation(r, c);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retrieveReservationsByPartner")
        public List<Reservation> retrieveReservationsByPartner(Partner p) {
        //TODO write your implementation code here:
        List<Reservation> reservations = reservationEntitySessionBean.retrieveReservationsByPartner(p);

        for (Reservation r : reservations) {
            CarCategory carCat = r.getCarCategory();
            em.detach(carCat);
            carCat.setListOfMakeModelsIncluded(null);
            carCat.setRentalRates(null);
            
            em.detach(r);
            Outlet pickupOutlet = r.getPickupOutlet();
            Outlet returnOutlet = r.getReturnOutlet();
            
            em.detach(pickupOutlet);
            em.detach(returnOutlet);
            pickupOutlet.setEmployees(null);
            returnOutlet.setEmployees(null);
            pickupOutlet.setDispatches(null);
            returnOutlet.setDispatches(null);
            pickupOutlet.setCars(null);
            returnOutlet.setCars(null);
            
            Customer rCustomer = r.getCustomer();
            em.detach(rCustomer);
            rCustomer.setReservations(null);
            
//            Car rCar = r.getCar();
//            em.detach(rCar);
//            rCar.setLocationOutlet(null);
//            rCar.setLocationCustomer(null);
//
//            MakeModel rCarMakeModel = rCar.getMakeModel();
//            em.detach(rCarMakeModel);
//            
//            CarCategory rCarCategory = rCarMakeModel.getCategory();
//            em.detach(rCarCategory);
//            rCarCategory.setRentalRates(null);
//            rCarCategory.setListOfMakeModelsIncluded(null);
        }
        
        return reservations;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "cancelReservation")
        public String cancelReservation(Reservation r) {
        return reservationEntitySessionBean.cancelReservation(r);
    }

    
}
