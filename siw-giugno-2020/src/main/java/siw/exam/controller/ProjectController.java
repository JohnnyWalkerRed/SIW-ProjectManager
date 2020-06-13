package siw.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import siw.exam.controller.session.SessionData;
import siw.exam.model.Project;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.services.ProjectService;

@Controller
public class ProjectController {
	@Autowired
	SessionData sessionData;
	@Autowired
	ProjectService projectService;
	@RequestMapping (value = {"/projects"}, method = RequestMethod.GET)
	public String projects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectList = projectService.retrieveProjectOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList", projectList);
		return "ownedProjects";
	}
	@RequestMapping (value = {"/projects/{project}/addTask"}, method = RequestMethod.GET)
	public String addTask(Model model, @PathVariable String project) {
		model.addAttribute("taskForm", new Task());
		Project activeProject = this.projectService.getProject(project);
		model.addAttribute("project", activeProject);
		return "/addTaskForm";
	}
	@RequestMapping (value = {"/projects/{project}/seeTasks"}, method = RequestMethod.GET)
	public String seeTasks(Model model, @PathVariable String project) {
		Project activeProject = this.projectService.getProject(project);
		List<Task> taskList = activeProject.getTasks();
		model.addAttribute("project", activeProject);
		model.addAttribute("taskList", taskList);
		return "/tasks";
	}
}
