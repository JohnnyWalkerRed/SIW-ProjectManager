package siw.exam.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Project;
import siw.exam.model.User;

public interface ProjectRepository extends CrudRepository<Project, Long> {

public Optional <Project> findById(long id);
public Optional <Project> findByName(String name);
public Optional <Project> findByBeginDate(LocalDate date);
public List <Project> findByMembers(User member);
public List<Project> findByOwner(User owner);
}
