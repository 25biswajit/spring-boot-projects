package com.bs.practice.crudservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bs.practice.crudservice.exception.ResourceNotFoundException;
import com.bs.practice.crudservice.model.User;
import com.bs.practice.crudservice.repository.UserRepository;

@Service
@Transactional
public class CrudUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> getListOfAllUsers(){
		return userRepository.findAll();
	}
	
	public User getUserById(Integer id) {
		Optional<User> userOptional = userRepository.findById(id);
		return userOptional.orElseThrow(() -> new ResourceNotFoundException("No such USER present with id : " + id));
	}
	
	public void updateUser(User newUser) {
		Optional<User> userOptional = userRepository.findById(newUser.getId());
		User dbUser = userOptional.orElseThrow(() -> new ResourceNotFoundException("No such USER present with id : " + newUser.getId()));
		dbUser = dbUser.copy(newUser);
		userRepository.save(dbUser);
	}
	
	public void removeUserById(Integer id) {
		try {
			userRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("No such USER present with id : " + id);
		}
	}

	public Integer createUser(User newUser) {
		User user = userRepository.save(newUser);
		return user.getId();
	}

	public void removeAllUsers() {
		userRepository.deleteAll();
	}
}