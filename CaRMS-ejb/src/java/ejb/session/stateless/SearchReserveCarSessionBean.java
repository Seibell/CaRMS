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
import entity.Outlet;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumeration.ReservationStatus;

/**
 *
 * @author wangp
 */
@Stateless
public class SearchReserveCarSessionBean implements SearchReserveCarSessionBeanRemote, SearchReserveCarSessionBeanLocal {

    @EJB
    private CarCategoryEntitySessionBeanLocal carCategoryEntitySessionBean;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBean;

    @EJB
    private MakeModelEntitySessionBeanLocal makeModelEntitySessionBean;

    @Override
    public Pair<List<Reservation>, List<RentalRate>> searchAvailableCarReservations(CarCategory chosenCategory, Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet) {

        String[] pickupOpenHourMin = pickupOutlet.getOpenTime().split(":");
        String[] pickupCloseHourMin = pickupOutlet.getClosingTime().split(":");
        String[] returnOpenHourMin = returnOutlet.getOpenTime().split(":");
        String[] returnCloseHourMin = returnOutlet.getClosingTime().split(":");

        List<Reservation> emptyRes = new ArrayList<>();
        List<RentalRate> emptyRental = new ArrayList<>();
        Pair<List<Reservation>, List<RentalRate>> emptyPair = new Pair(emptyRes, emptyRental);

        // check outlet opening time
        if (pickupOutlet.getOpenTime().equals("00:00") && pickupOutlet.getClosingTime().equals("00:00")) {
            // do nothing
        } else {
            Date pickupOutletOpenDate = new Date(pickupDate.getTime());
            Date pickupOutletCloseDate = new Date(pickupDate.getTime());

            pickupOutletOpenDate.setHours(Integer.parseInt(pickupOpenHourMin[0]));
            pickupOutletOpenDate.setMinutes(Integer.parseInt(pickupOpenHourMin[1]));
            pickupOutletCloseDate.setHours(Integer.parseInt(pickupCloseHourMin[0]));
            pickupOutletCloseDate.setMinutes(Integer.parseInt(pickupCloseHourMin[1]));

            if (pickupDate.compareTo(pickupOutletOpenDate) >= 0 && pickupDate.compareTo((pickupOutletCloseDate)) <= 0) {
                // do nothing
                
            } else {
                return emptyPair;
            }
        }

        if (returnOutlet.getOpenTime().equals("00:00") && returnOutlet.getClosingTime().equals("00:00")) {
            // do nothing
        } else {
            Date returnOutletOpenDate = new Date(returnDate.getTime());
            Date returnOutletCloseDate = new Date(returnDate.getTime());

            returnOutletOpenDate.setHours(Integer.parseInt(returnOpenHourMin[0]));
            returnOutletOpenDate.setMinutes(Integer.parseInt(returnOpenHourMin[1]));
            returnOutletCloseDate.setHours(Integer.parseInt(returnCloseHourMin[0]));
            returnOutletCloseDate.setMinutes(Integer.parseInt(returnCloseHourMin[1]));

            if (returnDate.compareTo(returnOutletOpenDate) >= 0 && returnDate.compareTo((returnOutletCloseDate)) <= 0) {
                // do nothing
            } else {
                return emptyPair;
            }
        }
        

        // finding rental rates that apply during the time period
        List<RentalRate> listOfRentalRatesForCategory = rentalRateSessionBean.retrieveActiveRentalRatesByCategory(chosenCategory);
        List<RentalRate> validRentalRates = new ArrayList<>();
        List<RentalRate> usedRentalRates = new ArrayList<>();
        for (RentalRate r : listOfRentalRatesForCategory) {
            // check if there is overlap (StartA <= EndB) and (EndA >= StartB)
            // DateRangesOverlap = max(start1, start2) < min(end1, end2)
            // https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap

            boolean overlap;
            if (r.getStartDate() == null && r.getEndDate() == null) {
                overlap = true;
            } else {
                overlap = (r.getStartDate().before(returnDate) || r.getStartDate().equals(returnDate))
                        && (r.getEndDate().after(pickupDate) || r.getEndDate().equals(pickupDate));
            }

            if (overlap) {
                validRentalRates.add(r);
            }

        }

        BigDecimal totalCost = new BigDecimal(0);
        Date endOfOneDay = new Date(pickupDate.getTime());
        while (true) {
            Date startOfOneDay = new Date(endOfOneDay.getTime());
            endOfOneDay = new Date(startOfOneDay.getTime());
            endOfOneDay.setDate(endOfOneDay.getDate() + 1);
            if (startOfOneDay.after(returnDate) || startOfOneDay.equals(returnDate)) {
                break;
            }
            if (endOfOneDay.after(returnDate)) {
                // set maximum endofoneday = returnDate
                endOfOneDay = new Date(returnDate.getTime());
            }
            BigDecimal cheapestForTheDay = new BigDecimal(Long.MAX_VALUE); // infinity?
            RentalRate usedRentalRateForTheDay = null;
            for (RentalRate r : validRentalRates) {
                if (r.getEndDate() != null && r.getStartDate() != null) {
                    boolean isRateEndAfterEqualsEndDay = r.getEndDate().after(endOfOneDay) || r.getEndDate().equals(endOfOneDay);
                    boolean isRateStartBeforeEqualsStartDay = r.getStartDate().before(startOfOneDay) || r.getStartDate().equals(startOfOneDay);
                    if (isRateEndAfterEqualsEndDay && isRateStartBeforeEqualsStartDay) {
                        if (r.getDailyRate().compareTo(cheapestForTheDay) == -1) {
                            cheapestForTheDay = r.getDailyRate();
                            usedRentalRateForTheDay = r;
                        }
                    }
                } else {
                    if (r.getDailyRate().compareTo(cheapestForTheDay) == -1) {
                        cheapestForTheDay = r.getDailyRate();
                        usedRentalRateForTheDay = r;
                    }
                }
            }
            totalCost = totalCost.add(cheapestForTheDay);
            if (!usedRentalRates.contains(usedRentalRateForTheDay)) {
                usedRentalRates.add(usedRentalRateForTheDay); // later set the used boolean to true
            }
        }

        int numOfCarsByCategory = carCategoryEntitySessionBean.numOfCarsInCarCategory(chosenCategory);
        List<Reservation> carReservationsByCategory = reservationEntitySessionBean.retrieveReservationsByCarCategory(chosenCategory);
        List<Reservation> finalReservations = new ArrayList<>();
        int numOfCarsRemaining = numOfCarsByCategory;
        // ok we get list of reservation first.
        // then go thru all the reservations of that category for that time period
        // and if any 1 overlap we minus one
        // then in the end we check if its 0. if its 0 then no reservation available.
        // however this only works under the sasumption that 1 car -> 1 reservation per day
        if (carReservationsByCategory.isEmpty() && numOfCarsByCategory != 0) {
            Reservation newR = new Reservation(pickupDate, returnDate, totalCost, ReservationStatus.RESERVED);
            newR.setCarCategory(chosenCategory);
            finalReservations.add(newR);
        } else {
            for (Reservation r : carReservationsByCategory) {

                if (!r.getReturnOutlet().equals(pickupOutlet) && r.getReturnDate().before(pickupDate)) {
                    if (pickupDate.getTime() - r.getReturnDate().getTime() < 7200000) { // 2 hours is 7200000
                        numOfCarsRemaining--;
                    }
                } else if (!r.getPickupOutlet().equals(returnOutlet) && r.getCollectDate().after(returnDate)) {
                    if (r.getCollectDate().getTime() - returnDate.getTime() < 7200000) {
                        numOfCarsRemaining--;
                    }
                } else {
                    boolean overlap = (r.getCollectDate().before(returnDate) || r.getCollectDate().equals(returnDate))
                            && (r.getReturnDate().after(pickupDate) || r.getReturnDate().equals(pickupDate));

                    if (overlap && !r.getReservationStatus().equals(ReservationStatus.CANCELLED)) {
                        numOfCarsRemaining--;
                    }
                }

            }

            if (numOfCarsRemaining > 0) {
                Reservation newR = new Reservation(pickupDate, returnDate, totalCost, ReservationStatus.RESERVED);
                newR.setCarCategory(chosenCategory);
                finalReservations.add(newR);

            }
        }
        return new Pair<>(finalReservations, usedRentalRates); //rservation
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method

    @Override
    public void confirmReservation(Reservation reservation, List<RentalRate> usedRentalRates,
            Long customerId,
            Long pickupOutletId, Long returnOutletId,
            boolean isPaidImmediate
    ) {
        if (isPaidImmediate) {
            reservation.setIsPaid(true);
        }
        reservationEntitySessionBean.createReservationWithoutCar(reservation, customerId, pickupOutletId, returnOutletId);

        for (RentalRate r : usedRentalRates) {
            RentalRate realRFromDatabase = rentalRateSessionBean.retrieveRentalRateById(r.getId());
            realRFromDatabase.setUsed(true);
        }

    }

    public void confirmReservationWithPartner(Reservation reservation, List<RentalRate> usedRentalRates, Long customerId,
            Long pickupOutletId, Long returnOutletId, boolean isPaidImmediate, Long partnerId) {
        reservation.setPartner(partnerEntitySessionBean.retrievePartnerById(partnerId));
        confirmReservation(reservation, usedRentalRates, customerId, pickupOutletId, returnOutletId,
                isPaidImmediate);

    }

    @Override
    public String payReservation(Reservation r, Customer c) {
        String paymentString = "Payment of $" + r.getRentalPrice() + " from credit card "
                + c.getCreditCard() + " confirmed!";

        return paymentString;
    }
}
