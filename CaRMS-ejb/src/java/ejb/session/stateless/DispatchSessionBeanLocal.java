/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DispatchNotFoundException;

/**
 *
 * @author ryanl
 */
@Local
public interface DispatchSessionBeanLocal {

    public void createDispatch(TransitDriverDispatchRecord dispatch);

    public void mergeDispatch(TransitDriverDispatchRecord dispatch);

    public TransitDriverDispatchRecord retrieveDispatchById(Long dispatchId) throws DispatchNotFoundException;

    public List<TransitDriverDispatchRecord> retrieveDispatchByDateAndStartOutlet(Date date, Date date1, Long outletId);

    public List<TransitDriverDispatchRecord> retrieveDispatchByDateAndEndOutlet(Date date, Date date1, Long outletId);

    public List<TransitDriverDispatchRecord> retrieveAllDispatches();

    public void createDispatchWithReservation(TransitDriverDispatchRecord dispatch, Reservation r);
    
}
