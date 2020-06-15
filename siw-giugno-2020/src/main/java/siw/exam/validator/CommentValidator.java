package siw.exam.validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import siw.exam.model.Comment;

import siw.exam.repository.UserRepository;
@Component
public class CommentValidator implements Validator{
	
	final Integer MAX_DESCRIPTION_LENGTH = 200;
	final Integer MIN_DESCRIPTION_LENGTH = 0;
	@Autowired
	UserRepository userRepository;
	@Override
	public boolean supports(Class<?> clazz) {
		return Comment.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Comment comment = (Comment)target;
		
		String description =comment.getDescription().trim();
		
		
		if(description.trim().isEmpty())
			errors.rejectValue("description", "required");
		else if (description.length()<MIN_DESCRIPTION_LENGTH || description.length()>MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");
	}

}
