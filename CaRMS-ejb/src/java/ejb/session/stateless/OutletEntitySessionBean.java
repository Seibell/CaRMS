/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wangp
 */
@Stateless
public class OutletEntitySessionBean implements OutletEntitySessionBeanLocal, OutletEntitySessionBeanRemote {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createOutlet(Outlet outlet) {
         em.persist(outlet);
    }

    @Override
    public void deleteOutlet(Long id) { // actually this one no need implement
        Outlet outlet = em.find(Outlet.class, id);
        List<Employee> listOfEmployees = outlet.getEmployees();
        for (Employee e: listOfEmployees) {
            e.setOutlet(null);
        }
        em.remove(outlet);
    }

    @Override
    public Outlet retrieveOutletById(Long id, boolean fetchEmployees) { // i think all these also don't need, but good for reference
        Outlet outlet = em.find(Outlet.class, id);
        // lazy fetch the list of employees
        if (fetchEmployees) outlet.getEmployees().size();
        return outlet;
    }

    @Override
    public List<Outlet> retrieveAllOutlets(boolean fetchEmployees) {
        Query q = em.createQuery("SELECT o FROM Outlet o");
        List<Outlet> outlets = q.getResultList();
        if (fetchEmployees) {
            for (Outlet o : outlets) {
                o.getEmployees().size();
            }
        }
        return outlets;
    }

    @Override
    public void mergeOutlet(Outlet outlet) {
        em.merge(outlet);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
