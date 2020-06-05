package siw.exam.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.exam.model.Comment;
import siw.exam.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Transactional
	public Comment getComment(long id) {
		Optional<Comment> result = this.commentRepository.findById(id);
		return result.orElse(null);
	}
	@Transactional 
	public void deleteComment(Comment comment) {
		this.commentRepository.delete(comment);
	}
	@Transactional
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);
	}
}
