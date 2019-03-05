/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.overpowered.byusforus.services.entrepriseprofile;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tn.esprit.overpowered.byusforus.entities.entrepriseprofile.ProjectManager;
import tn.esprit.overpowered.byusforus.services.quiz.AbstractFacade;

/**
 *
 * @author pc
 */
@Stateless
public class ProjectManagerFacade extends AbstractFacade<ProjectManager> implements ProjectManagerFacadeLocal, ProjectManagerFacadeRemote {

    @PersistenceContext(unitName = "piJEE-ejb")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectManagerFacade() {
        super(ProjectManager.class);
    }
    
}
