package siw.exam.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.Credentials;
import siw.exam.services.CredentialsService;
/*Classe validator che si occupa di controllare la validità dei dati inseriti e registrati per quaanto riguarda le 
 * credentials di un utente.*/
@Component
public class CredentialsValidator implements Validator{
	@Autowired
	CredentialsService credentialsService;
	/*Intervalli di lunghezza impostati 4-20 per username e 6-20 per password*/
	final Integer MAX_USERNAME_LENGTH = 20;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWORD_LENGTH = 20;
	final Integer MIN_PASSWORD_LENGTH = 6;
	/*Ovverride del metodo di Validator che serve a controllare 
	 * la compatibilità del validator con una classe clazz*/
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Credentials.class.equals(clazz);
	}
	/*Metodo effettivo di valutazione che stampa un messaggio a seconda dell'entità del problema 
	 * e rifiuta i dati immessi se non conformi*/
	@Override
	public void validate(Object target, Errors errors) {
		Credentials credentials = (Credentials) target;
		String userName = credentials.getUserName().trim();
		String password = credentials.getPassword().trim();
		
		if(userName.trim().isEmpty())
			errors.rejectValue("userName", "required");
		else if(userName.length()<MIN_USERNAME_LENGTH || userName.length()>MAX_USERNAME_LENGTH)
			errors.rejectValue("userName", "size");
		else if(this.credentialsService.getCredentials(userName)!=null)
			errors.rejectValue("userName", "duplicate");
		if(password.trim().isEmpty())
			errors.rejectValue("password", "required");
		else if(password.length()<MIN_PASSWORD_LENGTH || password.length()>MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size");
	}

}
