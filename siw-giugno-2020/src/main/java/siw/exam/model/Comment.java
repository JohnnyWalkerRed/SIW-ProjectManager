package siw.exam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {
	//id univoco
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//attributo description string
	@Column
	private String description;
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


	public Long getId() {
		return id;
	}
	//stampa a schermo le informazioni di comment
	@Override
	public String toString() {
		return "Comment [id=" + id + ", description=" + description + "]";
	}
	
	
	
}
