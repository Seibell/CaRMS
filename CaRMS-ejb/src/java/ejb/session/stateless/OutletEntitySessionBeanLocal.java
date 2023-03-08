/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wangp
 */
@Local
public interface OutletEntitySessionBeanLocal {
    
    public void createOutlet(Outlet outlet);
    
    public void deleteOutlet(Long id);

    public Outlet retrieveOutletById(Long id, boolean fetchEmployees);

    public List<Outlet> retrieveAllOutlets(boolean fetchEmployees);

    public void mergeOutlet(Outlet outlet);
}
