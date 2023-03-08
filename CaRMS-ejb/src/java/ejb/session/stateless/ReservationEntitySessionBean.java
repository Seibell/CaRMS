/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.Outlet;
import entity.Partner;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.enumeration.ReservationStatus;

/**
 *
 * @author wangp
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query q = em.createQuery("SELECT r FROM Reservation r");
        return q.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long id) {
        return em.find(Reservation.class, id);
    }

    @Override
    public List<Reservation> retrieveReservationsByCustomer(Customer c) { // should do customerId, but it works
        // so i dont wanna break it
        // in any case im 99% sure its always called from a local context so it will always work
        // since both database Customer and program Customer are synced
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.customer = :cus");
        q.setParameter("cus", c);
        return q.getResultList();
    }

    @Override
    public List<Reservation> retrieveReservationsByPartner(Partner p) {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.partner = :partner");
        q.setParameter("partner", p);
        return q.getResultList();
    }

    @Override
    public void createReservation(Reservation reservation, Long customerId, Long carId,
            Long pickupOutletId, Long returnOutletId) {
        reservation.setCustomer(em.find(Customer.class, customerId));
        reservation.setCar(em.find(Car.class, carId));
        reservation.setPickupOutlet(em.find(Outlet.class, pickupOutletId));
        reservation.setReturnOutlet(em.find(Outlet.class, returnOutletId));
        em.persist(reservation);
    }

    @Override
    public void createReservationWithoutCar(Reservation reservation, Long customerId,
            Long pickupOutletId, Long returnOutletId) {
        reservation.setCustomer(em.find(Customer.class, customerId));
        reservation.setPickupOutlet(em.find(Outlet.class, pickupOutletId));
        reservation.setReturnOutlet(em.find(Outlet.class, returnOutletId));
        em.persist(reservation);
    }

    // with partner
    @Override
    public void createReservation(Reservation reservation, Long customerId, Long carId,
            Long pickupOutletId, Long returnOutletId, Long partnerId) {
        reservation.setCustomer(em.find(Customer.class, customerId));
        reservation.setCar(em.find(Car.class, carId));
        reservation.setPickupOutlet(em.find(Outlet.class, pickupOutletId));
        reservation.setReturnOutlet(em.find(Outlet.class, returnOutletId));
        reservation.setPartner(em.find(Partner.class, partnerId));
        em.persist(reservation);
    }

    @Override
    public void createReservationWithoutCar(Reservation reservation, Long customerId,
            Long pickupOutletId, Long returnOutletId, Long partnerId) {
        reservation.setCustomer(em.find(Customer.class, customerId));
        reservation.setPickupOutlet(em.find(Outlet.class, pickupOutletId));
        reservation.setReturnOutlet(em.find(Outlet.class, returnOutletId));
        reservation.setPartner(em.find(Partner.class, partnerId));
        em.persist(reservation);
    }

    @Override
    public List<Reservation> retrieveReservationsByCar(Car car) {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.car = :car");
        q.setParameter("car", car);
        return q.getResultList();
    }

    @Override // returns status/result of cancelling
    public String cancelReservation(Reservation r) {
        Reservation rToCancel = retrieveReservationById(r.getId());

        if (rToCancel.getReservationStatus().equals(ReservationStatus.CANCELLED)) {
            return "Reservation already cancelled!";
        }

        rToCancel.setReservationStatus(ReservationStatus.CANCELLED);

        Date now = new Date();
        BigDecimal originalFee = rToCancel.getRentalPrice();
        BigDecimal penaltyFee = new BigDecimal(0);
        long daysBeforePickup = TimeUnit.DAYS.convert(rToCancel.getCollectDate().getTime() - now.getTime(), TimeUnit.MILLISECONDS);

        if (daysBeforePickup < 3) {
            penaltyFee = originalFee.multiply(new BigDecimal(0.7));
        } else if (daysBeforePickup < 7) {
            penaltyFee = originalFee.multiply(new BigDecimal(0.5));
        } else if (daysBeforePickup < 14) {
            penaltyFee = originalFee.multiply(new BigDecimal(0.2));
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String creditCard = r.getCustomer().getCreditCard();
        if (rToCancel.isIsPaid()) {
            // == fake credit card session bean ==
            // refunds
            return "Refunded balance of $" + df.format(originalFee.subtract(penaltyFee)) + " to credit card " + creditCard;
        } else {
            return "Charged penalty fee of $" + df.format(penaltyFee) + " to credit card " + creditCard;
        }
    }

    @Override
    public void mergeReservation(Reservation reservation) {
        em.merge(reservation);
    }

    @Override
    public List<Reservation> retrieveReservationBetweenTwoDates(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.collectDate >= :inStartDate AND r.collectDate < :inEndDate");
        query.setParameter("inStartDate", startDate, TemporalType.TIMESTAMP);
        query.setParameter("inEndDate", endDate, TemporalType.TIMESTAMP);

        return query.getResultList();
    }

    @Override
    public List<Reservation> retrieveReservationsByCarCategory(CarCategory car) {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.carCategory = :car");
        q.setParameter("car", car);
        return q.getResultList();
    }
}