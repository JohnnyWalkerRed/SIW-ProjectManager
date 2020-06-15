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
	@RequestMapping (value = {"/tags/tasks/{task}/addTag"}, method=RequestMethod.GET)
	public String addTag(Model model, @PathVariable Long task) {
		Task activeTask = this.taskService.getTask(task);
		model.addAttribute("activeTask", activeTask);
		model.addAttribute("activeProject", this.sessionData.getActiveProject());
		List<Tag> tags = this.tagService.getTags(this.sessionData.getActiveProject());//questo
		model.addAttribute("tags", tags);
		return "addTagToTaskForm";
	}
	@RequestMapping (value = {"/tags/projects/{projectId}/addTag"}, method=RequestMethod.GET)
	public String  addTagToProjectForm(Model model, @PathVariable Long projectId) {
		Project activeProject = this.projectService.getProject(projectId);
		model.addAttribute("activeProject", activeProject);
		this.sessionData.setActiveProject(activeProject);
		model.addAttribute("tagForm", new Tag());
		return "addTagToProjectForm";
	}
	@RequestMapping (value = {"/tags/projects/add"}, method = RequestMethod.POST)
	public String addTagToProjectForm(Model model, @Validated @ModelAttribute Tag tagForm, BindingResult tagBindingResult) {
		this.tagValidator.validate(tagForm, tagBindingResult);
		Project project = this.sessionData.getActiveProject();
		if(!tagBindingResult.hasErrors()) {
			this.tagService.saveTag(tagForm);
			this.projectService.addTag(project, tagForm);
		}
		return "redirect:/projects";
	}
	@RequestMapping (value = {"/tags/{taskId}/{tagId}/addTag"}, method=RequestMethod.GET)
	public String addTagToProject(Model model, @PathVariable Long taskId, @PathVariable Long tagId) {
		Tag activeTag = this.tagService.getTag(tagId);
		Task activeTask = this.taskService.getTask(taskId);
		this.tagService.addTask(activeTag, activeTask);
		this.taskService.addTag(activeTag, activeTask);
		return "redirect:/projects/"+this.sessionData.getActiveProject().getId();
	}
	@RequestMapping (value = {"/tags/{tagId}/{projectId}/deleteTagFromProject"}, method = RequestMethod.GET)
	public String deleteTagFromProject(Model model, @PathVariable Long tagId, @PathVariable Long projectId)
	{
		Tag activeTag = this.tagService.getTag(tagId);
		Project activeProject = this.projectService.getProject(projectId);
		this.projectService.removeTag(activeProject, activeTag);
		for(Task t : activeProject.getTasks())
		{
			this.taskService.removeTag(activeTag, t);
		}
		this.tagService.deleteTag(activeTag);
		return "redirect:/projects/"+projectId;
	}
}
