
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
	private TaskService taskService;
	@Autowired
	private CredentialsService credentialsService;

	@RequestMapping (value = {"/projects"}, method = RequestMethod.GET)
	public String projects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectList = projectService.retrieveProjectOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList", projectList);
		return "ownedProjects";
	}

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
		model.addAttribute("tasks", project.getTasks());
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		model.addAttribute("tags", this.tagService.getTags(project));
		this.sessionData.setActiveProject(project);
		return "project";
	}

    @RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
    public String createProjectForm(Model model) {
    	User loggedUser=sessionData.getLoggedUser();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("projectForm", new Project());
    	return "addProject";

    }

    @RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
    public String createProject(@Validated @ModelAttribute("projectForm")Project project,
    							BindingResult projectBindingResult,
    							Model model) {
    	User loggedUser = sessionData.getLoggedUser();

    	projectValidator.validate(project, projectBindingResult);
    	if (!projectBindingResult.hasErrors()) {
    		project.setOwner(loggedUser);
    		this.projectService.saveProject(project);
    		return "redirect:/projects";
    	}
    	model.addAttribute("loggedUser", loggedUser);
    	return "addProject";

    }

	@RequestMapping(value = {"/projects/{projectId}/addVisibility"}, method = RequestMethod.GET)
	private String addVisibilityForm(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		model.addAttribute("activeProject", activeProject);
		model.addAttribute("credentials", new Credentials());
		this.sessionData.setActiveProject(activeProject);
		return "addVisibilityForm";
	}
	@RequestMapping(value = {"/projects/addVisibility"}, method = RequestMethod.POST)
	private String addVisibility(@ModelAttribute("credentials") Credentials credentials, Model model) {
		Credentials userCredentials = this.credentialsService.getCredentials(credentials.getUserName());
		User user = userCredentials.getUser();
		Project activeProject = this.sessionData.getActiveProject();
		if(user==null)
			return userCredentials.getId().toString();
		else
			this.projectService.shareProjectWithUser(activeProject, user);
		return "redirect:/projects";
	}

	@RequestMapping (value= {"/shared"}, method= RequestMethod.GET)
	public String sharedProjects(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		List <Project> projectList= projectService.retrieveProjectsSharedWith(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList",projectList);
		return "sharedProjects";
	}
	@RequestMapping (value= {"/projects/{projectId}/delete"}, method = RequestMethod.GET)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		this.projectService.deleteProject(activeProject);
		return "redirect:/projects";
	}
	@RequestMapping (value = {"/projects/{userId}/{projectId}/deleteUserFromProject"}, method = RequestMethod.GET)
	public String unshareProject(Model model, @PathVariable Long userId, @PathVariable Long projectId) {
		User activeUser = this.userService.getUser(userId);
		Project activeProject = this.projectService.getProject(projectId);
		List<Task> tasks = activeProject.getTasks();
		for(Task t : tasks) {
			this.taskService.removeOwner(t, activeUser);
		}
		this.projectService.unshareProject(activeProject, activeUser);
		return "redirect:/projects/"+projectId;
	}
}
