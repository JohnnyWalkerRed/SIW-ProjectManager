package siw.exam.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	@RequestMapping(value= { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		
		User loggedUser = sessionData.getLoggedUser();
				
		List <Project> projectsList =  projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectList", projectsList);
		return "myOwnedProjects";
	}
	
	@RequestMapping(value= { "/projects/{projectId}" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model,
									@PathVariable Long projectId) {
		
		/* se il progetto non è presente tra quelli in DB
		 * reindirizza ai miei progetti
		 */
		Project project = projectService.getProject(projectId);
		if (project==null)
			return "redirect:/myOwnedProjects";
		
		User loggedUser = sessionData.getLoggedUser();
		
		/*se lo user non ha visibilità o è proprietario del progetto, 
		 * rimanda alla lista dei progetti dello user
		 */
		List <User> members = userService.getMembers(project);
		if (!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/myOwnedProjects";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
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
    public String createProject(@Valid @ModelAttribute("projectForm")Project project, 
    							BindingResult projectBindingResult, 
    							Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	
    	projectValidator.validate(project, projectBindingResult);
    	if (!projectBindingResult.hasErrors()) {
    		project.setOwner(loggedUser);
    		this.projectService.saveProject(project);
    		return "redirect: /projects"; //+ project.getId();
    	}
    	model.addAttribute("loggedUser", loggedUser);
    	return "addProject";
    	
    }
	
	

}
