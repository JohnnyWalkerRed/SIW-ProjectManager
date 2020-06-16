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
import siw.exam.model.Project;
import siw.exam.model.Tag;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.services.ProjectService;
import siw.exam.services.TagService;
import siw.exam.services.TaskService;
import siw.exam.validator.TagValidator;
@Controller
public class TagController {
	@Autowired
	TagService tagService;
	@Autowired 
	TagValidator tagValidator;
	@Autowired 
	ProjectService projectService;
	@Autowired
	TaskService taskService;
	@Autowired
	SessionData sessionData;
	/*richiesta di accedere ala vista di aggiunta tag ai task*/
	@RequestMapping (value = {"/tags/tasks/{task}/addTag/{projectId}"}, method=RequestMethod.GET)
	public String addTag(Model model, @PathVariable Long task, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		/*verifica autenticità del richiedente. Solo owner del project può taggare i task*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*prepara i dati per la vista*/
			Task activeTask = this.taskService.getTask(task);
			model.addAttribute("activeTask", activeTask);
			model.addAttribute("activeProject", activeProject);
			/*prende i tag disponibili da quelli associati al project, non tutti quelli esistenti dunque*/
			List<Tag> tags = this.tagService.getTags(activeProject);
			model.addAttribute("tags", tags);
			return "addTagToTaskForm";
		}
	}
	/*richiesta di accedere al form per creare un nuovo tag e associarlo al project*/
	@RequestMapping (value = {"/tags/projects/{projectId}/addTag"}, method=RequestMethod.GET)
	public String  addTagToProjectForm(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		/*controllo autenticità del richiedente. Questa richiesta può essere effettuata solo daall'owner del project*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*prepara i dati per il form, incluso un nuovo tag vuoto*/
			model.addAttribute("activeProject", activeProject);
			this.sessionData.setActiveProject(activeProject);
			model.addAttribute("tagForm", new Tag());
			return "addTagToProjectForm";
		}
	}
	/*richiesta POST dopo il form di creazione del tag*/
	@RequestMapping (value = {"/tags/projects/add"}, method = RequestMethod.POST)
	public String addTagToProjectForm(Model model, @Validated @ModelAttribute Tag tagForm, BindingResult tagBindingResult) {
		this.tagValidator.validate(tagForm, tagBindingResult);
		/*il project viene preso dai dati di sessione invece che richiesto al service. 
		 * è stato inserito nei dati di sessione dalla schermata precedente a questa (metodo "addTagToProjectForm")*/
		Project project = this.sessionData.getActiveProject();
		/*validazione dei dati inseriti nel form. 
		 * Se validi il tag è aggiunto alla lista di tags del project (strategia LAZY) ed è salvato*/
		if(!tagBindingResult.hasErrors()) {
			this.tagService.saveTag(tagForm);
			this.projectService.addTag(project, tagForm);
		}
		return "redirect:/projects";
	}
	/*richiesta di aggiunta di un tag ad un task*/
	@RequestMapping (value = {"/tags/{taskId}/{tagId}/addTag/{projectId}"}, method=RequestMethod.GET)
	public String addTagToProject(Model model, @PathVariable Long taskId, @PathVariable Long tagId, @PathVariable Long projectId) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		/*verifica autenticitàà: solo l'owner del project può taggare i suoi tasks*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*task e tag sono legati reciprocamente tramite il service (strategia LAZY)*/
			Tag activeTag = this.tagService.getTag(tagId);
			Task activeTask = this.taskService.getTask(taskId);
			this.tagService.addTask(activeTag, activeTask);
			this.taskService.addTag(activeTag, activeTask);
			return "redirect:/projects/"+this.sessionData.getActiveProject().getId();
		}
	}
	/*richiesta di rimozione di un tag da un project*/
	@RequestMapping (value = {"/tags/{tagId}/{projectId}/deleteTagFromProject"}, method = RequestMethod.GET)
	public String deleteTagFromProject(Model model, @PathVariable Long tagId, @PathVariable Long projectId)
	{
		Tag activeTag = this.tagService.getTag(tagId);
		Project activeProject = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		/*controllo autenticazione, solo owner può rimuovere il tag*/
		if(!activeProject.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*rimozione del tag dal project (strategia LAZY)*/
			this.projectService.removeTag(activeProject, activeTag);
			/*rimozione del tag da tutti i task soottostanti al project (strategia LAZY)*/
			for(Task t : activeProject.getTasks())
			{
				this.taskService.removeTag(activeTag, t);
			}	
			/*rimozione del tag dal database*/
			this.tagService.deleteTag(activeTag);
			return "redirect:/projects/"+projectId;
		}
	}
}
