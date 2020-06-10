package siw.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import siw.exam.controller.session.SessionData;
import siw.exam.model.Credentials;
import siw.exam.model.User;
/*Classe controller che si occupa di gestire tutte le richieste http che hanno a che fare con l'interfaccia principale
 * del sistema*/
@Controller
public class MainController {
	@Autowired 
	SessionData sessionData;
	public MainController() {}
	@RequestMapping (value = {"/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
	@RequestMapping (value= {"/home"}, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser=this.sessionData.getLoggedUser();
		Credentials loggedCredentials=this.sessionData.getLoggedCredentials();
		model.addAttribute("user", loggedUser);
		if(loggedCredentials.getRole().equals("ADMIN"))
		{
			return "admin";
		}
		else
			return "home";
	}
}
