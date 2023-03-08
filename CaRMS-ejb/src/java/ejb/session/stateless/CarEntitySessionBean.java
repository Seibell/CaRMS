/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.MakeModel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wangp
 */
@Stateless
public class CarEntitySessionBean implements CarEntitySessionBeanRemote, CarEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createCar(Car car, Long makeModelId) {
        MakeModel m = em.find(MakeModel.class, makeModelId);
        car.setMakeModel(m);
        
        //set Make model to used
        m.setUsed(true);
        
        em.persist(car);
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query q = em.createQuery("SELECT c FROM Car c");
        return q.getResultList();
    }

    @Override
    public Car retrieveCarById(Long id) {
        return em.find(Car.class, id);
    }

    @Override
    public List<Car> retrieveCarsByMakeModel(MakeModel mm) {
        Query q = em.createQuery("SELECT c FROM Car c WHERE c.makeModel = :makemodel");
        q.setParameter("makemodel" , mm);
        return q.getResultList();
    }

    @Override
    public void deleteCar(Car car) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteCar(Long carId) {
        Car car = retrieveCarById(carId);
        
        List<Car> carsOfSameMM = retrieveCarsByMakeModel(car.getMakeModel());
        if (carsOfSameMM.size() == 1) {
            MakeModel mm = car.getMakeModel();
            mm.setUsed(false);
        }
        
        if (car.isUsed()) {
            car.setDisabled(true);
            em.merge(car);
        } else {
            em.remove(car);
        }
    }
    
    @Override
    public void mergeCar(Car car) {
        em.merge(car);
    }
}
