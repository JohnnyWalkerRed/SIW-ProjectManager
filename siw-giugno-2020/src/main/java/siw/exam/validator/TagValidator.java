package siw.exam.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.Project;
import siw.exam.model.Tag;
import siw.exam.services.TagService;
@Component
public class TagValidator implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 200;
	final Integer MIN_COLOR_LENGTH = 0;
	final Integer MAX_COLOR_LENGTH = 6;
	
	@Autowired 
	TagService tagService;
	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}
	/*metodo di valutazione intermedio. Serve ad assicurare che il tag sia unico all'interno del project. Si usa in creazione*/
	public void validateInProject(Tag tag, Project project, Errors errors) {
		List<Tag> tags = this.tagService.getTags(project);
		
		for (Tag t : tags)
			if(t.getName().equals(tag.getName()) && !t.getId().equals(tag.getId()))
				errors.rejectValue("name", "duplicated");
		
		this.validate(tag, errors);
	}
	/*metodo di validazione standard, usato in update
	 * (un tag con nome uguale ma descrizione diversa deve essere valutato corretto per le funzioni di update)*/
	@Override
	public void validate(Object target, Errors errors) {
		Tag tag = (Tag) target;
		String name = tag.getName();
		String color = tag.getColor();
		String description = tag.getDescription();
		
		if(name.trim().isEmpty())
			errors.rejectValue("name", "required");
		else if (name.length()<MIN_NAME_LENGTH || name.length()>MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		
		if(color.trim().isEmpty())
			errors.rejectValue("color", "required");
		else if (color.length()<MIN_COLOR_LENGTH || color.length()>MAX_COLOR_LENGTH)
			errors.rejectValue("color", "size");
		
		
		if (description.length()<MIN_NAME_LENGTH || description.length()>MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");
	}

}
