/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.MakeModel;
import java.util.List;
import javax.ejb.Remote;
import util.exception.MakeModelNotFoundException;

/**
 *
 * @author wangp
 */
@Remote
public interface MakeModelEntitySessionBeanRemote {

    public List<MakeModel> retrieveAllMakeModels();

    public MakeModel retrieveMakeModelById(Long id);

    public void createMakeModel(MakeModel makeAndModel, Long categoryId);
    
    public void mergeMakeModel(MakeModel makeModel);
    
    public void deleteMakeModel(Long makeModelId);
    
    public List<MakeModel> retrieveMakeModelsByCategory(CarCategory category);
    
    public MakeModel retrieveMakeModelByName(String name) throws MakeModelNotFoundException;
}