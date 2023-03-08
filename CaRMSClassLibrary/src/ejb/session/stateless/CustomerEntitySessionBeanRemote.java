/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wangp
 */
@Remote
public interface CustomerEntitySessionBeanRemote {

    public void createCustomer(Customer c);

    public Customer retrieveCustomerById(Long id);

    public Customer retrieveCustomerByEmail(String email);

    public List<Customer> retrieveAllCustomers();

    public void deleteCustomer(Customer c);
}
