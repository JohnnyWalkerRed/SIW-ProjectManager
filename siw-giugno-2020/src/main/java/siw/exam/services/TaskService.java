package siw.exam.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Comment;
import siw.exam.model.Tag;
import siw.exam.model.Task;
import siw.exam.model.User;
import siw.exam.repository.TaskRepository;

import java.util.Optional;

/**
 * The TaskRepository handles logic for Tasks.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Restituisce un Task dal DB sulla base del suo ID.
     * @param id del Task da restituire dal DB
     * @return il Task stesso o null se il Task non è presente nel DB
     */
    @Transactional
    public Task getTask(long id) {
        Optional<Task> result = this.taskRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * Salva un task nel DB
     * @param il Task da salvare nel DB
     * @return the saved Task
     */
    @Transactional
    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    }

    /**
     * setta un Task come completato.
     * @param il task da settare come completato
     * @return il task dopo aver settato il tag come completato
     */
    @Transactional
    public Task setCompleted(Task task) {
        task.setCompleted(true);
        return this.taskRepository.save(task);
    }


    /**
     * cancella un task dal DB
     * @param il Task da cancellare
     */
    @Transactional
    public void deleteTask(Task task) {
        this.taskRepository.delete(task);
    }
    @Transactional 
    public void shareTaskWithUser(User user, Task task) {
    	Task activeTask = this.taskRepository.findById(task.getId()).orElse(null);
    	activeTask.setOwner(user);
    	this.taskRepository.save(activeTask);
    }
    @Transactional
    public void addTag(Tag tag, Task task) {
    	Task activeTask = this.taskRepository.findById(task.getId()).orElse(null);
    	activeTask.getTaskTag().add(tag);
    	this.taskRepository.save(activeTask);
    }
    @Transactional
    public void removeTag(Tag tag, Task task) {
    	Task activeTask = this.taskRepository.findById(task.getId()).orElse(null);
    	activeTask.getTaskTag().remove(tag);
    	this.taskRepository.save(activeTask);
    }
    
    @Transactional
    public void addComment(Comment comment, Task task) {
    	Task activeTask = this.taskRepository.findById(task.getId()).orElse(null);
    	activeTask.getComments().add(comment);
    	this.taskRepository.save(activeTask);
    }
    @Transactional
    public void removeComment(Comment comment, Task task) {
    	Task activeTask = this.taskRepository.findById(task.getId()).orElse(null);
    	activeTask.getComments().remove(comment);
    	this.taskRepository.save(activeTask);
    }
    @Transactional
    public void removeOwner(Task task, User user) {
    	Task activeTask=this.taskRepository.findById(task.getId()).orElse(null);
    	if(activeTask.getOwner().equals(user))
    		activeTask.setOwner(null);
    	this.taskRepository.save(activeTask);
    }
}
