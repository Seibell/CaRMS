/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wangp
 */
@Local
public interface PartnerEntitySessionBeanLocal {
    
    public void createPartner(Partner p);
    
    public Partner retrievePartnerById(Long id);
    
    public List<Partner> retrieveAllPartners();
    public void deletePartner(Partner p);

    public Partner retrievePartnerByEmailPassword(String email, String password);
}
