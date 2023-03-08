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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wangp
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanLocal, EmployeeEntitySessionBeanRemote {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void createEmployee(Employee e, Long outletId) {
        e.setOutlet(em.find(Outlet.class, outletId));
        em.persist(e);
    }

    @Override
    public Employee retrieveEmployeeById(Long id) {
        return em.find(Employee.class, id);

    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query q = em.createQuery("SELECT e FROM Employee e");
        return q.getResultList();

    }

    @Override
    public void deleteEmployee(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Employee) query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee " + username + " does not exist");
        }
    }
    
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Invalid password");
            }
        }
         catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist");
        }
    }
}
