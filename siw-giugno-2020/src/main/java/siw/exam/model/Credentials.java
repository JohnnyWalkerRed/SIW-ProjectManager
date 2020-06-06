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
	
	public Credentials() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static String getDefaultRole() {
		return DEFAULT_ROLE;
	}

	public static String getAdminRole() {
		return ADMIN_ROLE;
	}

	
}
