/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.MakeModel;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wangp
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long id);

    public void createReservation(Reservation reservation, Long customerId, Long carId,
            Long pickupOutletId, Long returnOutletId);

    // this is for partner
    public void createReservation(Reservation reservation, Long customerId, Long carId,
            Long pickupOutletId, Long returnOutletId, Long partnerId);

    public List<Reservation> retrieveReservationsByCar(Car car);

    public List<Reservation> retrieveReservationsByCustomer(Customer c);

    public String cancelReservation(Reservation r);

    public void mergeReservation(Reservation reservation);

    public List<Reservation> retrieveReservationBetweenTwoDates(Date startDate, Date endDate);

    public void createReservationWithoutCar(Reservation reservation, Long customerId, Long pickupOutletId, Long returnOutletId, Long partnerId);

    public void createReservationWithoutCar(Reservation reservation, Long customerId, Long pickupOutletId, Long returnOutletId);
}
