/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author ryanl
 */
@Remote
public interface CarCategorySessionBeanRemote {

    public void deleteCarCategory(Long id);

    public void createCarCategory(CarCategory category);

    public List<CarCategory> retrieveAllCarCategories();

    public CarCategory retrieveCarCategoryById(Long id);

    public CarCategory retrieveCarCategoryByName(String name) throws CarCategoryNotFoundException;

    public int numOfCarsInCarCategory(CarCategory c);
}
