/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ryanl
 */
@Remote
public interface EjbTimerSessionBeanRemote {
    
    public List<String> allocateCarsToCurrentDayReservations(Date date);
}
