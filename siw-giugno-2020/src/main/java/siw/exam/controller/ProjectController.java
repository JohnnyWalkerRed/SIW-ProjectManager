package siw.exam.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import siw.exam.controller.session.SessionData;
import siw.exam.model.Credentials;
import siw.exam.model.Project;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.services.CredentialsService;
import siw.exam.services.ProjectService;
import siw.exam.services.TagService;
import siw.exam.services.TaskService;
import siw.exam.services.UserService;
import siw.exam.validator.ProjectValidator;

@Controller
public class ProjectController {
	@Autowired 
	TaskService taskService;
	@Autowired
	private TagService tagService;
	@Autowired
	private ProjectValidator projectValidator;
	@Autowired
	private SessionData sessionData;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	/*richiesta di visualizzazione della lista dei projects*/
	@RequestMapping (value = {"/projects"}, method = RequestMethod.GET)
	public String projects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		/*carica tramite service i progetti posseduti dall'user loggato*/
		List<Project> projectList = projectService.retrieveProjectOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList", projectList);
		return "ownedProjects";
	}
	/*richiesta di visualizzazione di un project specifico*/
	@RequestMapping(value= { "/projects/{projectId}" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model,
									@PathVariable Long projectId) {

		/* se il progetto non è presente tra quelli in DB
		 * reindirizza ai miei progetti
		 */
		Project project = projectService.getProject(projectId);
		if (project==null)
			return "redirect:/projects";

		User loggedUser = sessionData.getLoggedUser();

		/*se lo user non ha visibilità o è proprietario del progetto,
		 * rimanda alla lista dei progetti dello user
		 */
		List <User> members = userService.getMembers(project);
		if (!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		else {
			/*prepara tutte le informazioni utili ad display del project*/
			model.addAttribute("tasks", project.getTasks());
			model.addAttribute("loggedUser", loggedUser);
			model.addAttribute("project", project);
			model.addAttribute("members", members);
			model.addAttribute("tags", this.tagService.getTags(project));
			this.sessionData.setActiveProject(project);
			return "project";
		}
	}
	/*richiesta di visuazlizzare la pagina di creazione project*/
    @RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
    public String createProjectForm(Model model) {
    	User loggedUser=sessionData.getLoggedUser();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("projectForm", new Project());
    	return "addProject";

    }
    /*richiesta post dopo il form di creazione del project*/
    @RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
    public String createProject(@Validated @ModelAttribute("projectForm")Project project,
    							BindingResult projectBindingResult,
    							Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	/*validazione dei dati inseriti*/
    	projectValidator.validate(project, projectBindingResult);
    	if (!projectBindingResult.hasErrors()) {
    		/*imposta l'utente loggato come proprietario del progetto appena creato*/
    		project.setOwner(loggedUser);
    		this.projectService.saveProject(project);
    		return "redirect:/projects";
    	}
    	/*se non validi torna indietro*/
    	model.addAttribute("loggedUser", loggedUser);
    	return "addProject";

    }
    /*richiesta di visualizzare la pagina per aggiungere la visibilità ad un progetto*/
	@RequestMapping(value = {"/projects/{projectId}/addVisibility"}, method = RequestMethod.GET)
	private String addVisibilityForm(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		/*verifica dell'autenticità del richiedente. Questa richiesta può essere effettuata solo
		 * da un utente che sia anche owner del progetto stesso*/
		if(!activeProject.getOwner().equals(sessionData.getLoggedUser()))
			return "/home";
		else {
			/*prepara i dati per il form che segue*/
			model.addAttribute("activeProject", activeProject);
			model.addAttribute("credentials", new Credentials());
			this.sessionData.setActiveProject(activeProject);
			return "addVisibilityForm";
		}
	}
	/*richiesta POST in seguito al form di aggiunta visibilità*/
	@RequestMapping(value = {"/projects/addVisibility"}, method = RequestMethod.POST)
	private String addVisibility(@ModelAttribute("credentials") Credentials credentials, Model model) {
		/*recupero delle credentials richieste dal form*/
		Credentials userCredentials = this.credentialsService.getCredentials(credentials.getUserName());
		/*se null, ovvero se non corrispondono a credentials esistenti reindirizza indietro*/
		if(userCredentials == null)
			return "redirect:/projects";
		else {
			/*retrieve dell'utente a partire dalle credentials modalità EAGER senza usare il service*/
			User user = userCredentials.getUser();
			Project activeProject = this.sessionData.getActiveProject();
			/*se l'utente riferito alle credentials non dovesse esistere torna indietro*/
			if(user==null)
				return "redirect:/projects";
			else
				/*aggiunta dell'user specificato ai members del project (modalità LAZY)*/
				this.projectService.shareProjectWithUser(activeProject, user);
			return "redirect:/projects";
		}
	}
	/*richiesta di visualizzazione dei progetti su cui un user ha vsibilità*/
	@RequestMapping (value= {"/shared"}, method= RequestMethod.GET)
	public String sharedProjects(Model model) {
		/*l'utente di cui si vuol vedere la lista progetti è quello attualmente loggato*/
		User loggedUser=sessionData.getLoggedUser();
		/*retrieve della lista in modalità LAZY tramite service*/
		List <Project> projectList= projectService.retrieveProjectsSharedWith(loggedUser);
		/*prepara i dati per la vista seguente*/
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList",projectList);
		return "sharedProjects";
	}
	/*richieta di cancellazione project*/
	@RequestMapping (value= {"/projects/{projectId}/delete"}, method = RequestMethod.GET)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		
		User loggedUser = this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		/*controllo autenticazione user. Solo owner del project può cancellare*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*cancellazione del project tramite service*/
			this.projectService.deleteProject(activeProject);
			return "redirect:/projects";
		}
	}
	/*richiesta di un-share per rimuovere la visibility di un user ad un project*/
	@RequestMapping (value = {"/projects/{userId}/{projectId}/deleteUserFromProject"}, method = RequestMethod.GET)
	public String unshareProject(Model model, @PathVariable Long userId, @PathVariable Long projectId) {
		User activeUser = this.userService.getUser(userId);
		User loggedUser = this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		/*controllo autenticità del richiedente. Solo owner del project può rimuovere la visibilità*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*la cancellazione deve avvenire in cascata anche ai task che hanno come owner l'utente scelto*/
			List<Task> tasks = activeProject.getTasks();
			for(Task t : tasks)
				this.taskService.removeOwner(t, activeUser);
			/*infine l'utente è rimosso dalla lista di members (modalità LAZY)*/
			this.projectService.unshareProject(activeProject, activeUser);
			return "redirect:/projects/"+projectId;
		}
	}
}
