package siw.exam.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Task;
import siw.exam.model.User;

public interface TaskRepository extends CrudRepository<Task, Long> {
	public Optional <Task> findById(long id);
	public Optional <Task> findByCreationDate(LocalDate date);
	public Optional <Task> findByOwner (User user);

}
