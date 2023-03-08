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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author wangp
 */
@Stateless
public class CarCategoryEntitySessionBean implements CarCategoryEntitySessionBeanLocal, CarCategorySessionBeanRemote {

    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createCarCategory(CarCategory category) {
        em.persist(category);
    }
    
    @Override
    public void deleteCarCategory(Long id) {
        // do we need this?
    }
    
    @Override
    public List<CarCategory> retrieveAllCarCategories() {
        Query q = em.createQuery("SELECT c FROM CarCategory c");
        return q.getResultList();
    }
    
    @Override
    public CarCategory retrieveCarCategoryById(Long id) {
        return em.find(CarCategory.class, id);
    }
    
    @Override
    public CarCategory retrieveCarCategoryByName(String name) throws CarCategoryNotFoundException {
        Query query = em.createQuery("SELECT c FROM CarCategory c WHERE c.name = :inName");
        query.setParameter("inName", name);
        
        try {
            return (CarCategory) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new CarCategoryNotFoundException("Car category not found");
        }
    }
    
    @Override
    public int numOfCarsInCarCategory(CarCategory c) {
        int num = 0;
        for (MakeModel m : c.getListOfMakeModelsIncluded()) {
            
            for (Car car: carEntitySessionBean.retrieveCarsByMakeModel(m)) {
                num += 1;
            }
        }
        return num;
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
