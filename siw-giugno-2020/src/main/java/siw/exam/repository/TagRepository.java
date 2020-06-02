package siw.exam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Tag;
import siw.exam.model.Task;

public interface TagRepository extends CrudRepository<Tag, Long>{
	public Optional<Tag> findByName(String name);
	public Optional<Tag> findById(Long id);
	public Optional<Tag> findByColor(String color);
	public List<Tag> findByTagTask(Task task);
}
