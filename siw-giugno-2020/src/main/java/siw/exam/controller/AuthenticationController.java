package siw.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import siw.exam.controller.session.SessionData;
import siw.exam.model.Credentials;
import siw.exam.model.User;
import siw.exam.services.CredentialsService;
import siw.exam.validator.CredentialsValidator;
import siw.exam.validator.UserValidator;
/*Classe controller che si occupa di gestire tutte le richieste http che hanno a che fare con l'autenticazione di 
 * un utente*/
@Controller
public class AuthenticationController {
	@Autowired
	CredentialsService credentialsService;
	@Autowired
	CredentialsValidator credentialsValidator;
	@Autowired
	UserValidator userValidator;
	@Autowired
	SessionData sessionData;
	/*
	 * Ricevitore per le rischieste GET del tipo /users/register. 
	 * Prepara un user e le sue credentials entrambe vuote per l'inserimento tramite il form
	 * Reindirizza quindi alla pagina registerUser, ovvero il form*/
	@RequestMapping(value = {"/users/register"}, method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("userForm", new User());
		model.addAttribute("credentialsForm", new Credentials());
		return "registerUser";
	}
	/*
	 * Ricevitore per le richieste POST del tipo /users/register.
	 * Si occupa di controllare la validità dei dati immessi secondo quanto stabilito dalle funzioni Validator
	 * Qualora i dati inseriti fossero conformi reindirizza verso la pagina registrationSuccessful, 
	 * altrimenti verso registerUser*/
	@RequestMapping(value= {"/users/register"}, method=RequestMethod.POST)
	public String registerUser(@Validated @ModelAttribute("userForm")User user, 
								BindingResult userBindingResult, 
								@Validated @ModelAttribute("credentialsForm")Credentials credentials, 
								BindingResult credentialsBindingResult, 
								Model model) {
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!userBindingResult.hasErrors()&&!credentialsBindingResult.hasErrors()) {
			credentials.setUser(user);
			user.setCredentials(credentials);
			credentialsService.saveCredentials(credentials);
			return "registrationSuccessful";
		}
		return "registerUser";
	}
	@RequestMapping (value= {"/users/{credentialsId}/updateUserName"}, method = RequestMethod.GET)
	public String updateUserName(Model model, @PathVariable Long credentialsId) {
		Credentials credentials = this.credentialsService.getCredentials(credentialsId);
		sessionData.setLoggedCredentials(credentials);
		model.addAttribute("credentials", credentials);
		return "updateUserName";
	}
	@RequestMapping (value= {"/users/modifyUserName"}, method = RequestMethod.POST)
	public String modifyUserName(Model model, @Validated @ModelAttribute("credentials")Credentials credentials, 
								BindingResult userNameBindingResult){
		Credentials newCredentials = sessionData.getLoggedCredentials();
		credentialsValidator.validateUserName(credentials, userNameBindingResult);
		if (!userNameBindingResult.hasErrors()) {
		newCredentials.setUserName(credentials.getUserName());
		credentialsService.updateUsername(newCredentials);
		return "userNameModified";
		}
		return "aggiornamentoNonRiuscito";
	}
}
