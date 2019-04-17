package tn.esprit.overpowered.byusforus.managedbeans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import tn.esprit.overpowered.byusforus.entities.authentication.Session;
import tn.esprit.overpowered.byusforus.entities.users.Candidate;
import tn.esprit.overpowered.byusforus.entities.users.User;
import tn.esprit.overpowered.byusforus.services.authentication.AuthenticationFacadeRemote;
import tn.esprit.overpowered.byusforus.services.candidat.CandidateFacade;
import tn.esprit.overpowered.byusforus.services.candidat.CandidateFacadeLocal;
import tn.esprit.overpowered.byusforus.services.users.UserFacade;
import tn.esprit.overpowered.byusforus.services.users.UserFacadeRemote;

/**
 *
 * @author pc
 */
@ManagedBean(name = "signUpBean")
@SessionScoped
public class SignUpBean implements Serializable{

    @EJB
    private UserFacadeRemote userFacade;

    @EJB
    private AuthenticationFacadeRemote authFacade;

    /**
     * Creates a new instance of SignUpBean
     */
    //Information for SignUp
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    //Information for SignIn
    private String login;
    private String pass;
    private String authUid;
    private String authToken;

    public SignUpBean(String username, String email, String firstName, String lastName, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserFacadeRemote getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacadeRemote userFacade) {
        this.userFacade = userFacade;
    }

    public AuthenticationFacadeRemote getAuthFacade() {
        return authFacade;
    }

    public void setAuthFacade(AuthenticationFacadeRemote authFacade) {
        this.authFacade = authFacade;
    }

    public String SignUpAsUser() throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password.getBytes());
        String goTo = "null";
        String result = userFacade.checkExistence(user.getEmail(), user.getUsername());
        if (result.equals("OK")) {
            Long id = userFacade.createUser(user);
            goTo = "null";//This is just for testing purpose until the actual page is created;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "result", result);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return goTo;
    }

    public void submit() throws NoSuchAlgorithmException {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");
        FacesContext.getCurrentInstance().addMessage("Successfull", msg);
        this.SignUpAsUser();
    }

    public void doLogin() throws NoSuchAlgorithmException {
        authUid = authFacade.login(login, pass);

    }

    public String doFinalizeLogin() {
        String goTo = "null";
        Session session = authFacade.finalizeLogin(authUid, authToken);
        if (session != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", session.getUser().getUsername());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            goTo = "null";//This is just for testing purpose until the actual page is created;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unsuccessful", "Bad Credentials");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return goTo;
    }

    public SignUpBean() {
    }

}
