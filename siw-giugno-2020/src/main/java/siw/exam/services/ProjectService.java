package siw.exam.services;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Project;
import siw.exam.model.User;
import siw.exam.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ProjectService handles logic for Projects.
 * This mainly consists in CRUD operations, as well as sharing projects with other users.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Restituisce un Project dal DB tramite parametro id.
     * @param id del project
     * @return il project individuato nel db ovvero null se il progetto non è presente
     */
    @Transactional
    public Project getProject(long id) {
        Optional<Project> result = this.projectRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * Salva un progetto nel DB.
     * @param un Project (il project da salvare)
     * @return il progetto salvato
     */
    @Transactional
    public Project saveProject(Project project) {
        return this.projectRepository.save(project);
    }

    /**
     * Cancella un Project dal DB.
     * @param il project da cancellare dal DB
     */
    @Transactional
    public void deleteProject(Project project) {
        this.projectRepository.delete(project);
    }

    /**
     * Condivide un project con uno User.
     * @param il Project da condividere con uno User
     * @param l'user con il quale condividere il project
     * @return il progetto condiviso
     */
    @Transactional
    public Project shareProjectWithUser(Project project, User user) {
        Project activeProject = this.projectRepository.findById(project.getId()).orElse(null);
    	activeProject.addMember(user);
        return this.projectRepository.save(activeProject);
    }
    @Transactional
    public List<Project> retrieveProjectOwnedBy(User user){
    	ArrayList<Project> lista = new ArrayList<>();
    	Iterable<Project> i = this.projectRepository.findByOwner(user);
    	for(Project p : i) {
    		lista.add(p);
    	}
    	return lista;
    }
    @Transactional
    public Project getProject(String projectName) {
    	Optional<Project> result = this.projectRepository.findByName(projectName);
    	return result.orElse(null);
    }

    public List <Project> retrieveProjectsOwnedBy(User user){
    	return this.projectRepository.findByOwner(user);

    }
}
