/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ryanl
 */
@Local
public interface RentalRateSessionBeanLocal {

    public void createRentalRate(RentalRate rentalRate, Long carCategoryId);

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateById(Long id);

    public void deleteRentalRate(Long rentalRateId);

    public void mergeRentalRate(RentalRate rentalRate);
    
    public List<RentalRate> retrieveActiveRentalRatesByCategory(CarCategory cat);

    public void updateRentalRate(RentalRate r);
}
