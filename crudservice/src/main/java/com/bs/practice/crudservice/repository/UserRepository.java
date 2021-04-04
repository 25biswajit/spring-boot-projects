package com.bs.practice.crudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bs.practice.crudservice.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
}
