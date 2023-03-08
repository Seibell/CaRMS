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
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.MakeModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;

/**
 *
 * @author wangp
 */
public class Main {

    @EJB
    private static EjbTimerSessionBeanRemote ejbTimerSessionBean;

    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBean1;

    @EJB
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBean;

    @EJB
    private static DispatchSessionBeanRemote dispatchSessionBean;

    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBean;

    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBean;

    @EJB(name = "CarEntitySessionBeanRemote")
    private static CarEntitySessionBeanRemote carEntitySessionBeanRemote;

    @EJB
    private static MakeModelEntitySessionBeanRemote makeModelEntitySessionBean;

    @EJB
    private static CarCategorySessionBeanRemote carCategoryEntitySessionBean;

    @EJB(name = "RentalRateSessionBeanRemote")
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBean;
    
    
    
        
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeEntitySessionBean, rentalRateSessionBeanRemote, carCategoryEntitySessionBean,
                makeModelEntitySessionBean, carEntitySessionBeanRemote, outletEntitySessionBean, customerEntitySessionBean,
                dispatchSessionBean, reservationEntitySessionBean, ejbTimerSessionBean);
        mainApp.runApp();
        
    }
    
}
