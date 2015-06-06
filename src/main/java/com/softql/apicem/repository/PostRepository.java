package com.softql.apicem.repository;

import com.softql.apicem.domain.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long>,//
        JpaSpecificationExecutor<Post>{

}
