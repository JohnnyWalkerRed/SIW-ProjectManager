package siw.exam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Comment {
	//id univoco
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//attributo description string
	@Column
	private String description;
	//relazione con il task a cui il commento si riferisce
	@OneToOne
	private Task commentedTask;
	//costruttore no args
	public Comment () {}
	//costruttore con set della descrizione
	public Comment (String description) {
		super();
		this.description=description;
	}
	//metodi getter e setter
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Task getCommentedTask() {
		return commentedTask;
	}

	public void setCommentedTask(Task commentedTask) {
		this.commentedTask = commentedTask;
	}

	public Long getId() {
		return id;
	}
	//stampa a schermo le informazioni di comment
	@Override
	public String toString() {
		return "Comment [id=" + id + ", description=" + description + "]";
	}
	
	
	
}
