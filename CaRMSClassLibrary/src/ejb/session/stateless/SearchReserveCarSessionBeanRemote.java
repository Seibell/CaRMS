/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.Customer;
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;

/**
 *
 * @author wangp
 */
@Remote
public interface SearchReserveCarSessionBeanRemote {

    public Pair<List<Reservation>, List<RentalRate>> searchAvailableCarReservations(CarCategory carCategory,
            Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet);

    public String payReservation(Reservation r, Customer c);

    public void confirmReservation(Reservation reservation, List<RentalRate> usedRentalRates, Long customerId, Long pickupOutletId, Long returnOutletId, boolean isPaidImmediate);

    public void confirmReservationWithPartner(Reservation reservation, List<RentalRate> usedRentalRates, Long customerId, Long pickupOutletId, Long returnOutletId, boolean isPaidImmediate, Long partnerId);

}
