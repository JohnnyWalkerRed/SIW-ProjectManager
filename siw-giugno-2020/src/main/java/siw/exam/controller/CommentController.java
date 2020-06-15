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

import siw.exam.model.Task;

import siw.exam.services.CommentService;
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
	
	@RequestMapping (value = {"/tasks/{taskId}/seeComments"}, method = RequestMethod.GET)
	public String seeCommentTask(Model model, @PathVariable Long taskId) {
		Task activeTask = this.taskService.getTask(taskId);
		this.sessionData.setActiveTask(activeTask);
		List <Comment> commentList= activeTask.getComments();
		model.addAttribute("activeTask", activeTask);
		model.addAttribute("commentList", commentList);
		return "comments";
	}
	
	@RequestMapping (value = {"/tasks/{taskId}/addComment"}, method = RequestMethod.GET)
	public String addCommentTask(Model model, @PathVariable Long taskId) {
		model.addAttribute("commentForm", new Comment());
		Task activeTask = this.taskService.getTask(taskId);
		model.addAttribute("activeTask", activeTask);
		sessionData.setActiveTask(activeTask);
		return "addCommentForm";
	}
	@RequestMapping (value = {"/comment/add"}, method = RequestMethod.POST)
	public String addComment(@Validated @ModelAttribute("commentForm") Comment comment, 
						  BindingResult commentBindingResult, 
						  Model model){
		commentValidator.validate(comment, commentBindingResult);
		Task activeTask = sessionData.getActiveTask();
		if(!commentBindingResult.hasErrors()) {
			commentService.saveComment(comment);
			taskService.addComment(comment, activeTask);
			List <Comment> commentList = commentService.getComments(activeTask);			
			model.addAttribute("activeTask", activeTask);
			model.addAttribute("commentList",commentList);
						
			return "comments";
		}
				
		
		return "redirect:/comment/add";
	}
	

}
