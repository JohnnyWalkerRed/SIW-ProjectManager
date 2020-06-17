package siw.exam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import siw.exam.model.User;
import siw.exam.controller.session.SessionData;
import siw.exam.model.Credentials;
import siw.exam.services.CredentialsService;
import siw.exam.services.UserService;
import siw.exam.validator.CredentialsValidator;
import siw.exam.validator.UserValidator;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {
	@Autowired 
	CredentialsService credentialsService;
	
	@Autowired 
	CredentialsValidator credentialsValidator;
    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("user", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    @RequestMapping (value = {"/users/{userId}/update"}, method = RequestMethod.GET)
    public String updateUserForm(Model model, @PathVariable Long userId) {
    	User activeUser = this.userService.getUser(userId);
    	if(!this.sessionData.getLoggedUser().equals(activeUser))
    		return "redirect:/home";
    	else {
    		model.addAttribute("activeUser", activeUser);
    		return "updateUserForm";
    	}
    }
    @RequestMapping (value = "users/updateUser", method = RequestMethod.POST)
    public String updateUser (Model model,
    						  @Validated @ModelAttribute("activeUser") User activeUser,
    						  BindingResult userBindingResult) {
    	
    	User loggedUser= this.sessionData.getLoggedUser();
    	
    	
    	this.userValidator.validate(activeUser, userBindingResult);
    	
    	
    	if( !userBindingResult.hasErrors()) {
    		loggedUser.setFirstName(activeUser.getFirstName());
        	loggedUser.setLastName(activeUser.getLastName());
    		this.userService.saveUser(loggedUser);
    		
    		return "redirect:/users/me";
    	}
    	return "redirect:/users/"+loggedUser.getId()+"/update";
    }

}
