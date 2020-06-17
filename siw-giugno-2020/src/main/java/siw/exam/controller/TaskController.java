package siw.exam.controller;

import java.util.ArrayList;
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
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.services.CredentialsService;
import siw.exam.services.ProjectService;
import siw.exam.services.TaskService;
import siw.exam.services.UserService;
import siw.exam.validator.TaskValidator;
import siw.exam.controller.session.SessionData;
import siw.exam.model.Credentials;
import siw.exam.model.Project;


@Controller
public class TaskController {
	@Autowired
	CredentialsService credentialsService;
	@Autowired 
	ProjectService projectService;
	@Autowired 
	TaskService taskService;
	@Autowired
	TaskValidator taskValidator;
	@Autowired
	SessionData sessionData;
	@Autowired 
	UserService userService;
	
	/*richiesta get per l'aggiunta di un task.*/
	@RequestMapping (value = {"/tasks/{projectId}/addTask"}, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		
		Project activeProject = this.projectService.getProject(projectId);
		/*controllo di autenticazione. L'utente attualmente loggato non deve essere diverso dall'owner del project*/
		if(!activeProject.getOwner().equals(this.sessionData.getLoggedUser()))
			return "redirect:/home";
		else {
			/*prepara i dati per la prossima vista html*/
			model.addAttribute("taskForm", new Task());
			model.addAttribute("activeProject", activeProject);
			this.sessionData.setActiveProject(activeProject);
			return "addTaskForm";
		}
	}
	/*richiesta post del form di creazione del task.*/
	@RequestMapping (value = {"/tasks/add"}, method = RequestMethod.POST)
	public String addTask(@Validated @ModelAttribute("taskForm") Task task, 
						  BindingResult taskBindingResult, 
						  Model model){
		taskValidator.validate(task, taskBindingResult);
		Project activeProject = this.sessionData.getActiveProject();
		/*dopo aver validato i dati inseriti si procede*/
		if(!taskBindingResult.hasErrors()) {
			//List<User> members = this.userService.getUsersByVisibleProject(activeProject);
			
			task.setCompleted(false);
			model.addAttribute("activeProject", activeProject);
			model.addAttribute("activeTask", task);
			//model.addAttribute("members", members);
			
			/*il task creato è aggiunto alla lista dei task del progetto. Operazione possibile senza
			 * l'ausilio del service dato che i task sono FETCH EAGER*/
			activeProject.getTasks().add(task);
			this.taskService.saveTask(task);
			this.projectService.saveProject(activeProject);
			return "redirect:/projects";
		}
		/*se i dati non sono convalidati prepara la vista dei projects*/
		model.addAttribute("project", activeProject);
		model.addAttribute("activeTask", task);
		return "redirect:/projects";
	}
	/*richiesta per impostare come completato un task*/
	@RequestMapping (value = {"/tasks/{taskId}/{projectId}/setCompleted"}, method = RequestMethod.GET)
	public String setCompleted(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
		Task activeTask = this.taskService.getTask(taskId);
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		/*autenticazione dell'utente richiedente. La modifica può essere effettuata solo dall'owner del project o del task*/
		if(!activeProject.getOwner().equals(loggedUser) && !activeTask.getOwner().equals(loggedUser))
			return "redirect:/home";
		else if(!activeProject.getTasks().contains(activeTask))
			return "redirect:/home";
		else {
			/*update dei dati del task*/
			activeTask.setCompleted(true);
			this.taskService.saveTask(activeTask);
			return "redirect:/projects/"+projectId;
		}
	}
	/*richiesta di cancellazione di un task da un project*/
	@RequestMapping(value= {"/tasks/{taskId}/{projectId}/delete"}, method = RequestMethod.GET)
	public String delete(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
		Task activeTask = this.taskService.getTask(taskId);
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		/*controllo di autenticazione dell'utente richiedente. Egli deve necessariamente essere owner del progetto*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*tramite il service si rimuove il task sia dal project che dal database*/
			this.projectService.removeTask(activeProject, activeTask);
			this.taskService.deleteTask(activeTask);
			return "redirect:/projects/"+projectId;
		}
	}
	/*richiesta di accedere alla vista per aggiunta owner ad un task*/
	@RequestMapping(value = {"/tasks/{taskId}/addOwner"}, method = RequestMethod.GET)
	public String addOwnerForm(Model model, @PathVariable Long taskId) {
		Project activeProject = this.sessionData.getActiveProject(); 
		User loggedUser=this.sessionData.getLoggedUser();
		/*verifica autenticazione dell'user richiedente. Deve essere owner del progetto*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		/*se autentiicato procede*/
		else {
			/*retrieve della lista di members del progetto*/
			List <User> members = userService.getMembers(activeProject);
			List<Credentials> credentials = new ArrayList<>();
			/*la lista di members viene trasformata in una lista di credentials. 
			 * Vogliamo che la vista mostri username non first e last name*/
			for(User u : members)
				credentials.add(this.credentialsService.getCredentialsByUser(u));
			/*prepara i dati per la vista successiva*/
			model.addAttribute("members", credentials);
			Task activeTask = this.taskService.getTask(taskId);
			model.addAttribute("activeTask", activeTask);
			model.addAttribute("activeProject", activeProject);
			return "tasksAddOwnerForm";
		}
	}
	/*rischiesta di aggiunta owner ad un task*/
	@RequestMapping (value = {"/tasks/{taskId}/{userId}/{projectId}/addedOwner"}, method = RequestMethod.GET)
	public String addOwner(Model model, @PathVariable Long taskId, @PathVariable Long userId, @PathVariable Long projectId) {
		Task task = this.taskService.getTask(taskId);
		User user = this.userService.getUser(userId);
		User loggedUser=this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		/*varifica autenticazione dell'user rischiedente. Questa modifica può essere effettuata solo dall'owner del project*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*tramite il service si imposta l'owner del task. 
			 * Per la strategia EAGER sarebbe possibile anche senza ricorrere al service*/
			this.taskService.shareTaskWithUser(user, task);
			return "redirect:/projects/"+activeProject.getId();
		}
	}
	@RequestMapping (value= {"/tasks/{taskId}/{projectId}/updateTask"}, method = RequestMethod.GET)
	public String updateTaskForm(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			Task activeTask = this.taskService.getTask(taskId);
			this.sessionData.setActiveTask(activeTask);
			this.sessionData.setActiveProject(activeProject);
			model.addAttribute("activeTask", activeTask);
			return "updateTaskForm";
		}
	}
	@RequestMapping(value = {"/tasks/updateTask"}, method=RequestMethod.POST)
	public String updateTask(Model model,
							 @Validated @ModelAttribute("activeTask") Task activeTask, 
							 BindingResult taskBindingResult) {
		this.taskValidator.validate(activeTask, taskBindingResult);
		Project activeProject = this.sessionData.getActiveProject();
		Task sessionTask = this.sessionData.getActiveTask();
		if(!taskBindingResult.hasErrors()) {
			
			sessionTask.setName(activeTask.getName());
			sessionTask.setDescription(activeTask.getDescription());
			this.taskService.saveTask(sessionTask);
			return "redirect:/projects/"+activeProject.getId();
		}
		return "redirect:/tasks/"+activeTask.getId()+"/"+activeProject.getId()+"/updateTask";
	}
	
}
