/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author ryanl
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createRentalRate(RentalRate rentalRate, Long categoryId) {
        CarCategory cat = em.find(CarCategory.class, categoryId);

        rentalRate.setCarCategory(cat);
        em.persist(rentalRate);
    }
    
    @Override
    public void updateRentalRate(RentalRate r) {
        em.merge(r);
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr");
        return query.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateById(Long id) {
        return em.find(RentalRate.class, id);
    }
    
    @Override
    public void mergeRentalRate(RentalRate rentalRate) {
        em.merge(rentalRate);
    }
    
    @Override
    public void deleteRentalRate(Long rentalRateId) {
        RentalRate rentalRate = retrieveRentalRateById(rentalRateId);
        
        if (rentalRate.isUsed()) {
            rentalRate.setDisabled(true);
            em.merge(rentalRate);
        } else {
            em.remove(rentalRate);
        }
    }

    @Override
    public List<RentalRate> retrieveActiveRentalRatesByCategory(CarCategory cat) {
        Query q = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.carCategory = :cat");
        q.setParameter("cat", cat);
//        q.setParameter("start", startDate);
//        q.setParameter("end", endDate);
        return q.getResultList();
    }

}
