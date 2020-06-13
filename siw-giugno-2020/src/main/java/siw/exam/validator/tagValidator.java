package siw.exam.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.Tag;
@Component
public class tagValidator implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 200;
	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Tag tag = (Tag) target;
		String name = tag.getName();
		String color = tag.getColor();
		String description = tag.getDescription();
		
	}

}
