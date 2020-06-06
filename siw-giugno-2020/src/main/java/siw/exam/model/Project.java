package siw.exam.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;



@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column (nullable=false)
	private String name;
	
	private String description;
	
	private LocalDate beginDate;
	
	
	/*Il progetto può essere visibile da uno o più utenti:
	 *la relazione è denominata members; 
	 *fetch Lazy: se vogliamo caricare un progetto, non necessariamente ci interessano gli utenti che ne hanno visibilità. 
	 */
	@ManyToMany(fetch=FetchType.LAZY)	
	private List <User> members;
	
	/*Ogni progetto ha un proprietario:
	 *la relazione è denominata owner; 
	 *fetch EAGER: è di interesse avere accesso al proprietario di ogni progetto (non esiste progetto senza proprietario)
	 */
	@ManyToOne (fetch=FetchType.EAGER)
	private User owner;
	
	/*
	* Ogni progetto si compone di uno o più Task (attività)
	* la relazione è denominata tasks
	* fetch EAGER: è di interesse avere accesso alle 
	* attività quando si accede ad un progetto
	* cascade REMOVE: i Task esistono solo se associati ad un progetto 
	  */
	@OneToMany (fetch=FetchType.EAGER, cascade = {CascadeType.REMOVE})
	private List <Task> projectTasks;
	
	/*
	* Ogni progetto può essere associato ad uno o più Tag
	* la relazione è denominata tags
	* fetch LAZY: qualora necessario visualizzare i tag verranno caricati 
	* cascade REMOVE: non ha senso avere dei tag non associati ad alcun progetto
	  */
	@OneToMany (fetch=FetchType.LAZY, cascade = {CascadeType.REMOVE})
	private List <Tag> projectTags;
	
	//inizializza con la data odierna prima di un Persist
	@PrePersist
	protected void onPersist() {
		beginDate= LocalDate.now();
		}
	
	//costruttore no args
	public Project() {
		
	}
	
	/* Si occupa di aggiungere uno user che ha 
	 * visibilità su un progetto
	 * controllando prima che non sia già presente
	 * tra gli user con visibilità
	 */
	public void addMember(User user) {
        if (!this.members.contains(user))
            this.members.add(user);
    }

	//metodi getters and setters
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public LocalDate getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}


	public List<User> getMembers() {
		return members;
	}


	public void setMembers(List<User> members) {
		this.members = members;
	}


	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
		
	public List<Task> getTasks() {
		return projectTasks;
	}

	public void setTasks(List<Task> tasks) {
		this.projectTasks = tasks;
	}

	public List<Tag> getTags() {
		return projectTags;
	}

	public void setTags(List<Tag> tags) {
		this.projectTags = tags;
	}
	
	//metodo toString: stampa a video le info di progetto

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description=" + description + ", beginDate=" + beginDate
				+ ", members=" + members + ", owner=" + owner + "]";
	}
	
	
	
	

}
