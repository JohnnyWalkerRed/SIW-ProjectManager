package siw.exam.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Credentials {
	
	public static final String DEFAULT_ROLE= "DEFAULT";
	public static final String ADMIN_ROLE= "ADMIN";
	
	//identificatore univoco
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//colonna userName univoca
	@Column(unique = true, nullable =false, length = 100)
	private String userName;
	
	//colonna contenente la password
	@Column(nullable = false, length = 100)
	private String password;
	
	//colonna contenente il ruolo (default ovvero admin)
	@Column(nullable = false, length = 100)
	private String role;
	
	
	/*
	* Ogni Credentials è associato a uno User
	* la relazione è denominata user
	* fetch EAGER (come di default per relazioni OneToOne)
	* cascade ALL: qualsiasi operazione venga svolta sul Credentials 
	* deve esser svolta anche sullo User
	  */
	@OneToOne(cascade=CascadeType.ALL)
	private User user;

}
