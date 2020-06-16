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
import siw.exam.model.Comment;
import siw.exam.model.Project;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.services.CommentService;
import siw.exam.services.ProjectService;
import siw.exam.services.TaskService;
import siw.exam.services.UserService;
import siw.exam.validator.CommentValidator;



@Controller
public class CommentController {
	
	@Autowired
	TaskService taskService;
	@Autowired
	SessionData sessionData;
	@Autowired
	UserService userService;
	@Autowired
	CommentValidator commentValidator;
	@Autowired
	CommentService commentService;
	@Autowired 
	ProjectService projectService;
	/*richiesta di accesso allla pagina dei commenti relativi ad un task*/
	@RequestMapping (value = {"/comments/{taskId}/seeComments/{projectId}"}, method = RequestMethod.GET)
	public String seeCommentTask(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
		Task activeTask = this.taskService.getTask(taskId);
		User loggedUser = this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		List<User> members = this.userService.getMembers(activeProject);
		/*autenticazione del richiedente. Può accedere a questa sezione solo chi ha visibilità sul project*/
		if(!activeProject.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/home";
		else {
			/*prepara i dati per la vista successiva*/
			this.sessionData.setActiveTask(activeTask);
			List <Comment> commentList= activeTask.getComments();
			model.addAttribute("activeTask", activeTask);
			model.addAttribute("commentList", commentList);
			return "comments";
		}
	}
	/*richiesta di accesso alla pagina del form per aggiungere un commento ad un tag*/
	@RequestMapping (value = {"/comments/{taskId}/addComment/{projectId}"}, method = RequestMethod.GET)
	public String addCommentTask(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project activeProject = this.projectService.getProject(projectId);
		Task activeTask = this.taskService.getTask(taskId);
		/*controllo autenticità: solo un owner del task o del project può aggiungere un comment*/
		if(!activeProject.getOwner().equals(loggedUser) && !activeTask.getOwner().equals(loggedUser))
			return "redirect:/home";
		else {
			/*prepara i dati per il form incluso un nuovo commento*/
			model.addAttribute("commentForm", new Comment());
			model.addAttribute("activeTask", activeTask);
			sessionData.setActiveTask(activeTask);
			return "addCommentForm";
		}
	}
	/*richiesta POST successiva al form*/
	@RequestMapping (value = {"/comment/add"}, method = RequestMethod.POST)
	public String addComment(@Validated @ModelAttribute("commentForm") Comment comment, 
						  BindingResult commentBindingResult, 
						  Model model){
		/*validazione del comment*/
		commentValidator.validate(comment, commentBindingResult);
		Task activeTask = sessionData.getActiveTask();
		if(!commentBindingResult.hasErrors()) {
			/*salvataggio del comment e aggiunta ai comment del task (strategia LAZY)*/
			commentService.saveComment(comment);
			taskService.addComment(comment, activeTask);
			/*get della lista dei comment (LAZY) per farne il display in seguito*/
			List <Comment> commentList = commentService.getComments(activeTask);			
			model.addAttribute("activeTask", activeTask);
			model.addAttribute("commentList",commentList);
			return "comments";
		}
		return "redirect:/comment/add";
	}
	

}
