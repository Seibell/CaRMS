/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.MakeModel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MakeModelNotFoundException;

/**
 *
 * @author wangp
 */
@Stateless
public class MakeModelEntitySessionBean implements MakeModelEntitySessionBeanLocal, MakeModelEntitySessionBeanRemote {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public void createMakeModel(MakeModel makeAndModel, Long categoryId) {
        CarCategory category = em.find(CarCategory.class, categoryId);
        System.out.println("Make model setting category. Category is: " + category);
        System.out.println("This category name is " + category.getName());
        System.out.println("And it has a list of: " + category.getListOfMakeModelsIncluded());
        category.getListOfMakeModelsIncluded().add(makeAndModel);
        makeAndModel.setCategory(category);

        em.persist(makeAndModel);
    }

    @Override
    public List<MakeModel> retrieveAllMakeModels() {
        Query q = em.createQuery("SELECT m FROM MakeModel m");
        return q.getResultList();
    }
    
    @Override
    public MakeModel retrieveMakeModelByName(String name) throws MakeModelNotFoundException {
        Query query = em.createQuery("SELECT m FROM MakeModel m WHERE m.name = :inName");
        query.setParameter("inName", name);
        
        try {
            return (MakeModel) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new MakeModelNotFoundException("Make model not found");
        }
    }

    @Override
    public MakeModel retrieveMakeModelById(Long id) {
        return em.find(MakeModel.class, id);
    }

    @Override
    public void mergeMakeModel(MakeModel makeModel) {
        em.merge(makeModel);
    }

    @Override
    public void deleteMakeModel(Long makeModelId) {
        MakeModel makeModel = retrieveMakeModelById(makeModelId);

        if (makeModel.isUsed()) {
            makeModel.setDisabled(true);
            em.merge(makeModel);
        } else {
            em.remove(makeModel);
        }
    }

    @Override
    public List<MakeModel> retrieveMakeModelsByCategory(CarCategory category) {
        Query q = em.createQuery("SELECT m FROM MakeModel m WHERE m.category = :category");
        q.setParameter("category", category);
        return q.getResultList();
    }

}
