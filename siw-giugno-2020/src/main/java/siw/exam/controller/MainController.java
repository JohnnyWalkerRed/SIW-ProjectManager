package siw.exam.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/*Classe controller che si occupa di gestire tutte le richieste http che hanno a che fare con l'interfaccia principale
 * del sistema*/
@Controller
public class MainController {
	public MainController() {}
	@RequestMapping (value = {"/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
}
