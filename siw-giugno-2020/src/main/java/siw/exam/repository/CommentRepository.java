package siw.exam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.exam.model.Comment;

public interface CommentRepository extends CrudRepository <Comment, Long>{
	public Optional<Comment> findById(Long id);
}
