package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	public Users findByEmail(String email);

}
