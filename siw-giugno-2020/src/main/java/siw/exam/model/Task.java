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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column (nullable=false)
	private String name;
	
	private String description;
	
	private LocalDate creationDate;
	
	/*
	* Ogni task può essere associato ad uno o più tag e ogni tag può essere
	* associato ad uno o più task
	* la relazione è denominata taskTag
	* fetch LAZY: qualora necessario visualizzare i tag verranno caricati 
	* cascade: ogni tag potrebbe essere associato a più task, eventuali 
	* tag non associati ad alcun task verranno eliminati all'eliminazione
	* del progetto
	  */
	@ManyToMany (fetch=FetchType.LAZY)
	private List <Tag> taskTag;
	
	/*
	* Ogni task è associato ad un solo User
	* la relazione è denominata ownerTask
	* fetch EAGER: è opportuno caricare lo user al quale è associato quel task 
	*/
	@OneToOne (fetch=FetchType.EAGER)
	private User ownerTask;
	
	/*Un utente può scrivere uno o più commenti sotto un task
	 * la relazione è denominata comments
	 * fetch LAZY: se necessario i commenti vengono caricati
	 * cascade REMOVE: non ha senso mantenere dei commenti su un task 
	 * se questo è eliminato*/
	@OneToMany (fetch = FetchType.LAZY, cascade= {CascadeType.REMOVE})
	private List<Comment> comments;
	
	//costruttore no args
	public Task() {
	}
	
	//inizializza con la data odierna prima di un Persist
	@PrePersist
	protected void onPersist() {
		creationDate= LocalDate.now();
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

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
			
	public List<Tag> getTaskTag() {
		return taskTag;
	}

	public void setTaskTag(List<Tag> taskTag) {
		this.taskTag = taskTag;
	}

	public User getOwnerTask() {
		return ownerTask;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setOwnerTask(User ownerTask) {
		this.ownerTask = ownerTask;
	}

	//metodo toString: stampa a video le info di Task
	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", creationDate=" + creationDate
				+ "]";
	}
	
	
	
	

}
