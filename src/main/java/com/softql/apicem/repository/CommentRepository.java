package com.softql.apicem.repository;

import com.softql.apicem.domain.Comment;
import com.softql.apicem.domain.Post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	public List<Comment> findByPost(Post post);

    public Page<Comment> findByPostId(Long id, Pageable page);
	
}
