/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wangp
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createPartner(Partner p) {
        em.persist(p);
    }

    @Override
    public Partner retrievePartnerById(Long id) {
        return em.find(Partner.class, id);

    }

    @Override
    public List<Partner> retrieveAllPartners() {
        Query q = em.createQuery("SELECT p FROM Partner p");
        return q.getResultList();
    }

    @Override
    public void deletePartner(Partner p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Partner retrievePartnerByEmailPassword(String email, String password) {
        Query q = em.createQuery("SELECT p FROM Partner p WHERE p.email = :email AND p.password = :pass");
        q.setParameter("email", email);
        q.setParameter("pass", password);
        try {
            return (Partner) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
}
