/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatus;
import util.enumeration.ReservationStatus;

/**
 *
 * @author ryanl
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBean;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @Schedule(hour = "2", minute = "0", second =  "0")
    public void autoAllocateCars() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        
        System.out.println("Allocating cars at: " + date.toString());
        List<String> output = allocateCarsToCurrentDayReservations(date);
        
        for (String s : output) {
            System.out.println(s);
            
            if (output.isEmpty()) {
                System.out.println("No reservations to allocate cars to");
            }
        }
    }

    @Override
    public List<String> allocateCarsToCurrentDayReservations(Date date) {
        
        List<String> output = new ArrayList<>();
        
        Date currDate = date;
        Date endDate = date;
        
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.DATE, 1);
        
        endDate = c.getTime();
        
        List<Reservation> reservations = reservationEntitySessionBean.retrieveReservationBetweenTwoDates(currDate, endDate);
        List<Reservation> toAllocate = new ArrayList<>();
        
        for (Reservation r : reservations) {
            if (r.getCar() == null) {
                toAllocate.add(r); //Add those reservations without car to allocate list
            }
        }
        
        if (toAllocate.isEmpty()) {
            output.add("No reservations for current date that require allocation");
        } else {
            
            //Case where current outlet has car category reserved
            for (Reservation r : toAllocate) {
                if (r.getReservationStatus().equals(ReservationStatus.CANCELLED)) continue;
                List<Car> allCars = carEntitySessionBean.retrieveAllCars();
                List<Car> cars = new ArrayList<>();
                
                for (Car car : allCars) { //Add all cars that are in current outlet
                    if (car.getLocationOutlet().equals(r.getPickupOutlet())) {
                        cars.add(car);
                    }
                }
                
                for (Car car : allCars) { //Add rest
                    if (!car.getLocationOutlet().equals(r.getPickupOutlet())) {
                        cars.add(car);
                    }
                }
                
                for (Car car : cars) {
                    if (car.getCarStatus().equals(CarStatus.IN_OUTLET) && car.getMakeModel().getCategory().equals(r.getCarCategory())) {
                        r.setCar(car);
                        car.setCarStatus(CarStatus.RESERVED);
                        car.setUsed(true);
                        
                        output.add("Car: " + car.getLicensePlate() + " assigned to Reservation: " + r.getId());
                        break;
                    }
                }
            }
        }
        return output;
    }
}