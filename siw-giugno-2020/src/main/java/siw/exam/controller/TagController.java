package siw.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import siw.exam.controller.session.SessionData;
import siw.exam.model.Tag;
import siw.exam.model.Task;
import siw.exam.services.TaskService;
@Controller
public class TagController {
	@Autowired
	TaskService taskService;
	@Autowired
	SessionData sessionData;
	@RequestMapping (value = {"/tasks/{task}/addTag"}, method=RequestMethod.GET)
	public String addTag(Model model, @PathVariable Long task) {
		Task activeTask = this.taskService.getTask(task);
		model.addAttribute("activeTask", activeTask);
		model.addAttribute("tagForm", new Tag());
		this.sessionData.setActiveTask(activeTask);
		return "/tags/addTagForm";
	}
}
