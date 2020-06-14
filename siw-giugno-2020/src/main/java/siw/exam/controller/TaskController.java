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
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.repository.ProjectRepository;
import siw.exam.repository.TaskRepository;
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
	ProjectService projectService;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	TaskRepository taskRepository;
	@Autowired 
	TaskService taskService;
	@Autowired
	TaskValidator taskValidator;
	@Autowired
	SessionData sessionData;
	@Autowired 
	UserService userService;
	@RequestMapping (value = {"/tasks/{projectId}/addTask"}, method = RequestMethod.GET)
	public String addTaskForm(Model model, @PathVariable Long projectId) {
		model.addAttribute("taskForm", new Task());
		Project activeProject = this.projectService.getProject(projectId);
		model.addAttribute("activeProject", activeProject);
		this.sessionData.setActiveProject(activeProject);
		return "addTaskForm";
	}
	@RequestMapping (value = {"/tasks/add"}, method = RequestMethod.POST)
	public String addTask(@Validated @ModelAttribute("taskForm") Task task, 
						  BindingResult taskBindingResult, 
						  Model model){
		taskValidator.validate(task, taskBindingResult);
		Project activeProject = this.sessionData.getActiveProject();
		if(!taskBindingResult.hasErrors()) {
			/*da aggiungere la parte dell'aggiunta dell'user a cui si riferisce. Attesa della vista dei membri*/
			List<User> members = this.userService.getUsersByVisibleProject(activeProject);
			model.addAttribute("activeProject", activeProject);
			model.addAttribute("activeTask", task);
			model.addAttribute("members", members);
			activeProject.getTasks().add(task);
			this.taskRepository.save(task);
			this.projectRepository.save(activeProject);
			return "redirect:/projects";
		}
		model.addAttribute("project", activeProject);
		model.addAttribute("activeTask", task);
		
		
		return "redirect:/projects";
	}
	@RequestMapping (value = {"/tasks/{taskId}/{userId}/addOwner"}, method = RequestMethod.POST)
	public String addOwner(Model model, @PathVariable Long taskId, @PathVariable Long userId) {
		Task task = this.taskService.getTask(taskId);
		User user = this.userService.getUser(userId);
		this.taskService.shareTaskWithUser(user, task);
		Project activeProject = this.sessionData.getActiveProject();
		
		
		return "redirect:/projects";
	}
	@RequestMapping (value = {"/tasks/{projectId}/seeTasks"}, method=RequestMethod.GET)
	public String seeTasks(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		this.sessionData.setActiveProject(activeProject);
		List<Task> taskList = activeProject.getTasks();
		model.addAttribute("project", activeProject);
		model.addAttribute("taskList", taskList);
		return "/tasks";
	}
	
}
