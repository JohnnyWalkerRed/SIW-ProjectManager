package siw.exam.validator;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.User;
/*Classe validator che si occupa di controllare la validità dei dati immessi 
 * per quanto riguarda un user*/
@Component
public class UserValidator implements Validator{
	/*Intervallo unico impostato per entrambi i nomi 2-100*/
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	/*Override del metodo di Validator per controllare la compatibilità 
	 * del validator con una classe clazz*/
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
	/*Metodo principale di valutazione che stampa un messaggio a seconda dell'entità del problema
	 * e rifiuta i dati immessi se non conformi*/
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		String firstName = user.getFirstName().trim();
		String lastName = user.getLastName().trim();
		
		if(firstName.trim().isEmpty())
			errors.rejectValue("firstName", "required");
		else if (firstName.length()<MIN_NAME_LENGTH || firstName.length()>MAX_NAME_LENGTH)
			errors.rejectValue("firstName", "size");
		
		if(lastName.trim().isEmpty())
			errors.rejectValue("lastName", "required");
		else if (lastName.length()<MIN_NAME_LENGTH || lastName.length()>MAX_NAME_LENGTH)
			errors.rejectValue("lastName", "size");
	}

}
