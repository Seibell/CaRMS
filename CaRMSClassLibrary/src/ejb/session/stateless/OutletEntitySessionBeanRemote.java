/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wangp
 */
@Remote
public interface OutletEntitySessionBeanRemote {
    
    public void createOutlet(Outlet outlet);
    
    public void deleteOutlet(Long id);
    
    public void mergeOutlet(Outlet outlet);

    public Outlet retrieveOutletById(Long id, boolean fetchEmployees);

    public List<Outlet> retrieveAllOutlets(boolean fetchEmployees);
}
