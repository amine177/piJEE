/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.overpowered.byusforus.services.users;

import java.util.List;
import javax.ejb.Remote;
import tn.esprit.overpowered.byusforus.entities.users.CompanyAdmin;
import tn.esprit.overpowered.byusforus.entities.users.CompanyProfile;

/**
 *
 * @author pc
 */
@Remote
public interface CompanyAdminFacadeRemote {

    void create(CompanyAdmin companyAdmin);

    void edit(CompanyAdmin companyAdmin);

    void remove(CompanyAdmin companyAdmin);

    CompanyAdmin find(Object id);

    List<CompanyAdmin> findAll();

    List<CompanyAdmin> findRange(int[] range);

    int count();
    
    public Long addCompanyAdmin(CompanyAdmin companyAdmin);
    public void updateCompanyAdmin(CompanyAdmin companyAdmin);
    public void deleteCompanyAdmin(Long idAdmin);
    void bindCompanyAdminToCompanyProfile(Long idAmin, Long idComp);
    public void createCompanyProfile(CompanyProfile compProfile);
    public void updateCompanyProfile(CompanyProfile compProfile);
    public CompanyProfile viewCompanyProfile(Long idComp);
    public List<CompanyProfile> searchCompanyProfileByName(String name);
    public CompanyProfile searchCompanyProfileById(Long id);
    public void deleteCompanyProfile(Long idComp);
}
