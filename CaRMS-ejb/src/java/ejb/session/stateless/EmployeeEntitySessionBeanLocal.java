/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wangp
 */
@Local
public interface EmployeeEntitySessionBeanLocal {
    
    public void createEmployee(Employee e, Long outletId);
    
    public Employee retrieveEmployeeById(Long id);
    
    public List<Employee> retrieveAllEmployees();
    
    public void deleteEmployee(Long id);

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;
}
