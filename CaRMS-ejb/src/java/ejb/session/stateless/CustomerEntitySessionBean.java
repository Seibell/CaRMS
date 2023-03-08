/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
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
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    
    @Override
    public void createCustomer(Customer c) {
        em.persist(c);
    }

    @Override
    public Customer retrieveCustomerById(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        Query q = em.createQuery("SELECT c FROM Customer c");
        return q.getResultList();
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteCustomer(Customer c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer retrieveCustomerByEmail(String email) {
        Query q = em.createQuery("SELECT c FROM Customer c WHERE c.email = ?1");
        q.setParameter(1, email);
        List<Customer> customers = q.getResultList();
        if (customers.isEmpty()) return null;
        return customers.get(0);
    }
    
    @Override
    public void updateCustomer(Customer c) {
        em.merge(c);
    }
}
