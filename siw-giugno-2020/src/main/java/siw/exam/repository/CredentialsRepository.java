package siw.exam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Credentials;
import siw.exam.model.User;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	
	public Optional <Credentials> findByUserName (String username);
	public Optional <Credentials> findByUser (User user);
}
