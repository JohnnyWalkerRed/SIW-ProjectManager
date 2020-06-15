package siw.exam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Project;
import siw.exam.model.Tag;
import siw.exam.model.Task;
import siw.exam.repository.ProjectRepository;
import siw.exam.repository.TagRepository;

@Service
public class TagService {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TagRepository tagRepository;
	@Transactional
	public Tag getTag(long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Tag getTag(String name) {
		Optional<Tag> result = this.tagRepository.findByName(name);
		return result.orElse(null);
	}
	@Transactional 
	public void saveTag(Tag tag) {
		this.tagRepository.save(tag);
	}
	
	@Transactional
	public void deleteTag(Tag tag)
	{
		this.tagRepository.delete(tag);
	}
	@Transactional
	public void addTask(Tag tag, Task task) {
		Tag activeTag = this.tagRepository.findById(tag.getId()).orElse(null);
		activeTag.getTagTask().add(task);
		this.tagRepository.save(activeTag);
	}
	@Transactional
	public List<Tag> getTags(Project project){
		Project activeProject = this.projectService.getProject(project.getId());
		return activeProject.getTags();
	}
}
