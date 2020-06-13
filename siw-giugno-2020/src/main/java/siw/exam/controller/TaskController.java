package siw.exam.controller;

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
import siw.exam.repository.ProjectRepository;
import siw.exam.repository.TaskRepository;
import siw.exam.services.TaskService;
import siw.exam.validator.TaskValidator;
import siw.exam.model.Project;
import siw.exam.model.Tag;


@Controller
public class TaskController {
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	TaskRepository taskRepository;
	@Autowired 
	TaskService taskService;
	@Autowired
	TaskValidator taskValidator;
	@RequestMapping (value = {"/tasks/add"}, method = RequestMethod.POST)
	public String addTask(@Validated @ModelAttribute("taskForm") Task task, BindingResult taskBindingResult, Model model){
		taskValidator.validate(task, taskBindingResult);
		Project activeProject = (Project) model.getAttribute("project");
		if(!taskBindingResult.hasErrors()) {
			/*da aggiungere la parte dell'aggiunta dell'user a cui si riferisce*/
			activeProject.getTasks().add(task);
			this.taskRepository.save(task);
			this.projectRepository.save(activeProject);
			return "redirect:/tasks";
		}
		model.addAttribute("project", activeProject);
		return "addTask";
	}
	@RequestMapping (value = {"/tasks/{task}/addTag"}, method=RequestMethod.GET)
	public String addTag(Model model, @PathVariable Long task) {
		Task activeTask = this.taskService.getTask(task);
		model.addAttribute("activeTask", activeTask);
		model.addAttribute("tagForm", new Tag());
		return "/tags/addTagForum";
	}
}
