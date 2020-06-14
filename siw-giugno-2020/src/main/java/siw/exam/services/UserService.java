package siw.exam.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Credentials;
import siw.exam.model.Project;
import siw.exam.model.User;
import siw.exam.repository.ProjectRepository;
import siw.exam.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserService handles logic for Users.
 */
@Service
public class UserService {

	@Autowired
	private ProjectRepository projectRepository;
	
    @Autowired
    private UserRepository userRepository;

    /**Metodo che restituisce uno User ricercato tramite id
     * return: User
     * parametro: long id
     */
    @Transactional
    public User getUser(long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }
    @Transactional 
    public User getUserByCredentials(Credentials credentials) {
    	Optional<User> result = this.userRepository.findByCredentials(credentials);
    	return result.orElse(null);
    }
    /**
     * Metodo che salva User nel DB e restituisce lo stesso User
     * return: User
     * parametro: User
      */
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * restituisce la lista di tutti gli User salvati 
     * nel DB
     */
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
   
    @Transactional
    public void sharedProject(Project project, User user) {
    	User activeUser = this.userRepository.findById(user.getId()).orElse(null);
    	activeUser.addVisibleProject(project);
    	this.userRepository.save(activeUser);
    }
    
    @Transactional
    public List<User> getUsersByVisibleProject(Project project){
    	
    	List<User> result = new ArrayList<>();
    	Project activeProject = this.projectRepository.findById(project.getId()).orElse(null);
    	result = activeProject.getMembers();
    	return result;
    }
}
