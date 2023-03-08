/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.exception.DispatchNotFoundException;

/**
 *
 * @author ryanl
 */
@Stateless
public class DispatchSessionBean implements DispatchSessionBeanRemote, DispatchSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createDispatch(TransitDriverDispatchRecord dispatch) {
        em.persist(dispatch);
    }
    
    @Override
    public void mergeDispatch(TransitDriverDispatchRecord dispatch) {
        em.merge(dispatch);
    }
    
    @Override
    public TransitDriverDispatchRecord retrieveDispatchById(Long dispatchId) throws DispatchNotFoundException {
        TransitDriverDispatchRecord dispatch = em.find(TransitDriverDispatchRecord.class, dispatchId);
        
        if (dispatch != null) {
            return dispatch;
        } else {
            throw new DispatchNotFoundException("Dispatch not found!");
        }
    }
    
    @Override
    public List<TransitDriverDispatchRecord> retrieveDispatchByDateAndStartOutlet(Date startDate, Date endDate, Long outletId) {        
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t JOIN t.reservation r WHERE r.collectDate >= :inStartDate AND r.collectDate < :inEndDate AND t.fromOutlet.id = :inOutletId");
        query.setParameter("inStartDate", startDate, TemporalType.TIMESTAMP);
        query.setParameter("inEndDate", endDate, TemporalType.TIMESTAMP);
        query.setParameter("inOutletId", outletId);
        
        return query.getResultList();
    }
    
    @Override
    public List<TransitDriverDispatchRecord> retrieveDispatchByDateAndEndOutlet(Date startDate, Date endDate, Long outletId) {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t JOIN t.reservation r WHERE r.collectDate >= :inStartDate AND r.collectDate < :inEndDate AND t.toOutlet.id = :inOutletId");
        query.setParameter("inStartDate", startDate, TemporalType.TIMESTAMP);
        query.setParameter("inEndDate", endDate, TemporalType.TIMESTAMP);
        query.setParameter("inOutletId", outletId);
        
        return query.getResultList();
    }
    
    @Override
    public List<TransitDriverDispatchRecord> retrieveAllDispatches() {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t");
        
        return query.getResultList();
    }
    
    @Override
    public void createDispatchWithReservation(TransitDriverDispatchRecord dispatch, Reservation r) {
        r.setDispatch(dispatch);
        em.persist(dispatch);
    }
}
