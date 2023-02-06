package com.grpcflix.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grpcflix.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {}
