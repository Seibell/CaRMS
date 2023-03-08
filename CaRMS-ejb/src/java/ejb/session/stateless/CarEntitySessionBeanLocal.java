/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.MakeModel;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wangp
 */
@Local
public interface CarEntitySessionBeanLocal {
    
    public void createCar(Car car, Long makeModelId);
    
    public List<Car> retrieveAllCars();
    
    public Car retrieveCarById(Long id);
        
    public List<Car> retrieveCarsByMakeModel(MakeModel mm);
    
    public void deleteCar(Car car);

    public void deleteCar(Long carId);

    public void mergeCar(Car car);
}
