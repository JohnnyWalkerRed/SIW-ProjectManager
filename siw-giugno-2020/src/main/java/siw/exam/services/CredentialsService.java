package siw.exam.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import siw.exam.model.Credentials;
import siw.exam.model.User;
import siw.exam.repository.CredentialsRepository;

@Service
public class CredentialsService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Transactional
	public Credentials getCredentials(long id) {
		Optional <Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
		
	}
	
	public Credentials getCredentials(String username) {
		Optional <Credentials> result = this.credentialsRepository.findByUserName(username);
		return result.orElse(null);
	}
	
	public Credentials getCredentialsByUser(User user) {
		Optional<Credentials> result = this.credentialsRepository.findByUser(user);
		return result.orElse(null);
	}
	
	public Credentials saveCredentials (Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
}
