package siw.exam.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column 
	private String name;
	@Column
	private String color;
	@Column
	private String description;
	/*Ogni task può essere associato ad uno o più tag del progetto a cui appartiene, 
	 * ed ogni tag può essere associato ad uno o più task
	 * La relazione è chiamata tagTask ed è mappata dal lato task dalla colonna taskTag
	 * fetch LAZY: se si volesse ricercare un tag non è detto che si vogliano anche i task 
	 * ad esso associati*/
	@ManyToMany (mappedBy = "taskTag", fetch=FetchType.LAZY)
	private List<Task> tagTask;
	
	
	//metodi setter getter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Task> getTagTask() {
		return tagTask;
	}
	public void setTagTask(List<Task> tagTask) {
		this.tagTask = tagTask;
	}
	public Long getId() {
		return id;
	}
	//metodo tostring per visualizzare a schermo le informazioni del tag
	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", color=" + color + ", description=" + description + "]";
	}
	
	
	
	
}
