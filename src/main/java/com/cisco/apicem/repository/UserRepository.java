package com.cisco.apicem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cisco.apicem.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);
}
