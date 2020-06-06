package siw.exam.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Task;
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
}
