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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	
	
	
	
}
