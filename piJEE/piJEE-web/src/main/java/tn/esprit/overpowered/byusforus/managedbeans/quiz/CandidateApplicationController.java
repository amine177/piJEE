package tn.esprit.overpowered.byusforus.managedbeans.quiz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import tn.esprit.overpowered.byusforus.entities.candidat.CandidateApplication;
import tn.esprit.overpowered.byusforus.managedbeans.util.JsfUtil;
import tn.esprit.overpowered.byusforus.managedbeans.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.Part;
import tn.esprit.overpowered.byusforus.entities.entrepriseprofile.JobOffer;
import tn.esprit.overpowered.byusforus.entities.users.Candidate;
import tn.esprit.overpowered.byusforus.services.candidat.CandidateApplicationFacadeLocal;
import tn.esprit.overpowered.byusforus.util.JobApplicationState;
import util.authentication.Authenticator;

@ManagedBean
@SessionScoped
public class CandidateApplicationController implements Serializable {

    @EJB
    private CandidateApplicationFacadeLocal ejbFacade;
    private List<CandidateApplication> items = null;
    private CandidateApplication selected;
    private Part file;

    public CandidateApplicationController() {
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public CandidateApplication getSelected() {
        return selected;
    }

    public void setSelected(CandidateApplication selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CandidateApplicationFacadeLocal getFacade() {
        return ejbFacade;
    }

    public CandidateApplication prepareCreate() {
        selected = new CandidateApplication();
        initializeEmbeddableKey();
        return selected;
    }

    public String showApplicationForm(JobOffer jobOffer) {
        selected = new CandidateApplication();
        selected.setJobOffer(jobOffer);
        return "/views/front/JobApplication/apply_to_job_form?faces-redirect=true";
    }

    public void saveResumeFile() throws IOException {
        String randomFileName = new Random().nextInt() + file.getSubmittedFileName();
        this.selected.setResume(randomFileName);
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, new File("../standalone/deployments/piJEE-web-1.0.war/", randomFileName).toPath());
        }
    }

    public String submitApplication() {
        this.selected.setCandidate((Candidate) Authenticator.currentSession.getUser());
        this.selected.setAdditionalInfo("Waiting for company decision");
        this.create();
        return "/views/candidate/candidatesView?faces-redirect=true";
    }

    public String inviteToTakeQuiz() {
        this.selected.setJobApplicationState(JobApplicationState.INVITED_FOR_QUIZ);
        this.selected.setAdditionalInfo("Waiting for candidate to take quiz");
        this.update();
        return "List?faces-redirect=true";
    }

    public String rejectCandidacy() {
        this.selected.setJobApplicationState(JobApplicationState.REFUSED);
        this.selected.setAdditionalInfo("candidacy refuesd.");
        this.update();
        return "List?faces-redirect=true";
    }

    public Boolean hasAlreadyApplied(JobOffer jobOffer) {
        CandidateApplication cApp = ejbFacade.getApplicationByCandidateId(Authenticator.currentSession.getUser().getId(), jobOffer.getId());
        this.selected = cApp;
        return cApp != null;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CandidateApplicationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CandidateApplicationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CandidateApplicationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CandidateApplication> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public CandidateApplication getCandidateApplication(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<CandidateApplication> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CandidateApplication> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CandidateApplication.class)
    public static class CandidateApplicationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CandidateApplicationController controller = (CandidateApplicationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "candidateApplicationController");
            return controller.getCandidateApplication(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CandidateApplication) {
                CandidateApplication o = (CandidateApplication) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CandidateApplication.class.getName()});
                return null;
            }
        }

    }

}