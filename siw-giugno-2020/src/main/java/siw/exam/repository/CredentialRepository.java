package siw.exam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Credentials;

public interface CredentialRepository extends CrudRepository<Credentials, Long> {
	
	
	public Optional <Credentials> findByUserName (String username);

}
