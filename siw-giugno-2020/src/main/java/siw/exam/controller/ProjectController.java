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
import siw.exam.model.User;
import siw.exam.services.ProjectService;
import siw.exam.services.UserService;
import siw.exam.validator.ProjectValidator;

@Controller
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	SessionData sessionData;
	
	@RequestMapping(value= { "/projects{projectId}" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model,
									@PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		if (project==null)
			return "redirect:/projects";
		
		User loggedUser = sessionData.getLoggedUser();
		
		List <User> members = userService.getMembers(project);
		if (!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		
		List <Project> projectsList =  projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList", projectsList);
		return "myOwnedProjects";
	}
	
    @RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
    public String createProjectForm(Model model) {
    	User loggedUser=sessionData.getLoggedUser();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("projectForm", new Project());
    	return "addProject";
    	
    	
    }
	
	

}
