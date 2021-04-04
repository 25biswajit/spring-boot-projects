package com.bs.practice.crudservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bs.practice.crudservice.model.User;
import com.bs.practice.crudservice.service.CrudUserService;

@RestController
public class CrudServiceController {
	
	@Autowired
    private CrudUserService userService;
	
	@GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "id",required=false) Optional<Integer> id) {
		if(id.isPresent()) {
			return new ResponseEntity<User>(userService.getUserById(id.get()), HttpStatus.OK);
		}else {
			return new ResponseEntity<List<User>>(userService.getListOfAllUsers(), HttpStatus.OK);
		}
    }
	
	@PostMapping("/users")
	public ResponseEntity<Integer> insert(@RequestBody User user) {
		Integer id = userService.createUser(user);
		return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
	}
	
	@PutMapping("/users")
	public ResponseEntity<String> update(@RequestBody User user) {
		userService.updateUser(user);
		return new ResponseEntity<String>("User with id : " + user.getId() + " updated successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id) {
		userService.removeUserById(id);
		return new ResponseEntity<String>("User with id : " + id + " deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/users")
	public ResponseEntity<String> delete() {
		userService.removeAllUsers();
		return new ResponseEntity<String>("All Users has been deleted successfully", HttpStatus.OK);
	}
}
