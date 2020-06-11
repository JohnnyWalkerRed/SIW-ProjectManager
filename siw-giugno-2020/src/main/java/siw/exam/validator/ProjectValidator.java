package siw.exam.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.Project;

@Component
public class ProjectValidator implements Validator{
	
	/*Override del metodo di Validator per controllare la compatibilità 
	 * del validator con una classe clazz*/
	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Project project = (Project) target;
		String name = project.getName().trim();
		
		
		if(name.trim().isEmpty())
			errors.rejectValue("name", "required");
		
	}


}
