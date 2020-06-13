package siw.exam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Credentials;
import siw.exam.model.Project;
import siw.exam.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public Optional <User> findById(long id);
	public Optional <User> findByFirstName(String username);
	public Optional <User> findByOwnedProjects(Project project);
	public Optional <User> findByVisibleProjects(Project project);
	public Optional <User> findByCredentials(Credentials credentials);
}
