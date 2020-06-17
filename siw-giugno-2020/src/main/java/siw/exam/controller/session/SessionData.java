package siw.exam.controller.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import siw.exam.model.Credentials;
import siw.exam.model.Project;
import siw.exam.model.Tag;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.repository.CredentialsRepository;

/*Classe che rappresenta una collezione di dati di sessione che devono rimanere memorizzati ad ogni sessione*/
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {
	private User user;
	private Credentials credentials;
	@Autowired
	private CredentialsRepository credentialsRepository;
	private Project activeProject;
	private Task activeTask;
	private Tag activeTag;
	/*Metodo che si occupa di preparare gli attributi prendendone i valori dall'autenticazione di Security*/
	private void update() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUserDetails = (UserDetails) obj;
		this.credentials=this.credentialsRepository.findByUserName(loggedUserDetails.getUsername()).get();
		this.user=this.credentials.getUser();
	}
	/*Metodo getter dell'user che esegue sempre un update in caso il dato non esistesse*/
	public User getLoggedUser() {
		if(this.user==null)
			this.update();
		return this.user;
	}
	/*Metodo getter del credentials che esegue sempre un update in caso il dato non esistesse*/
	public Credentials getLoggedCredentials() {
		if(this.credentials==null)
			this.update();
		return this.credentials;
	}
	
	public void setLoggedCredentials(Credentials credentials) {
		this.credentials= credentials;
	}
	public void setActiveProject(Project project) {
		this.activeProject=project;
	}
	public Project getActiveProject() {
		return this.activeProject;
	}
	public void setActiveTask(Task task) {
		this.activeTask=task;
	}
	public Task getActiveTask() {
		return this.activeTask;
	}
	public Tag getActiveTag() {
		return activeTag;
	}
	public void setActiveTag(Tag activeTag) {
		this.activeTag = activeTag;
	}
	
}