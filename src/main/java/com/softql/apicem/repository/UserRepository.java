package com.softql.apicem.repository;

import com.softql.apicem.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Long> ,
        JpaSpecificationExecutor<User>
{

	public User findByUsername(String username);
}
