package com.bs.practice.crudservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.bs.practice.crudservice.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CrudServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class CrudControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
	
	@Test
	@Order(1)
    public void testCRUDOperationOnUser() {
		// New User
		User userNew = createDummyUser();
				
        // New User Created	
        Integer newUserId = createNewUser(userNew);
        
        // Fetch User By User Id
        User userFetched = getUserById(newUserId);
        
        assertEquals(userFetched.getName(), userNew.getName());
        assertEquals(userFetched.getAddress(), userNew.getAddress());
        assertEquals(userFetched.getAge(), userNew.getAge());
        assertEquals(userFetched.getSalary(), userNew.getSalary());
        
        // Update User
        updateUser(userFetched);
        
        // Fetch All User
        testGetAllUsers();
        
        // Delete User
        deleteUser(newUserId, HttpStatus.OK);
    }
	
	private User createDummyUser() {
		User userNew = new User();
        userNew.setName("Dummy");
        userNew.setAge(25);
        userNew.setSalary(20000);
        userNew.setAddress("DuumyLocation");
		return userNew;
	}

	private Integer createNewUser(User userNew) {
		HttpEntity<User> requestInsert = new HttpEntity<User>(userNew, getHeaders());
        ResponseEntity<Integer> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", 
        		requestInsert,
        		Integer.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        System.out.println("USER Successfully CREATED:" + userNew);
        
        return postResponse.getBody();		
	}

	public void updateUser(User userToBeUpdated) {
		userToBeUpdated.setSalary(userToBeUpdated.getSalary() + 500);
    	
    	HttpEntity<User> requestUpdate = new HttpEntity<User>(userToBeUpdated, getHeaders());
    	UriComponents uri =UriComponentsBuilder.fromHttpUrl(getRootUrl() + "/users").build();
    	
    	ResponseEntity<String> putResponse = restTemplate.exchange(uri.toString(), 
    			HttpMethod.PUT, 
    			requestUpdate,
				String.class);
    	
    	assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(putResponse.getBody());
        assertEquals("User with id : "+userToBeUpdated.getId()+" updated successfully", putResponse.getBody());
        
        System.out.println("User with id : "+userToBeUpdated.getId()+" updated successfully");
	}
	
    public User getUserById(Integer id) {
    	assertNotNull(id);
    	
    	UriComponents uri = UriComponentsBuilder.fromUriString(getRootUrl() + "/users")
                .queryParam("id", id)
                .build();
    	
    	ResponseEntity<User> userResponse = restTemplate.exchange(uri.toString(), 
    			HttpMethod.GET,
    			HttpEntity.EMPTY,
    			User.class);
    	
    	assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    	
    	User fetchedUser = userResponse.getBody();
    	
        assertNotNull(fetchedUser);
        
        System.out.println("USER Successfully FETCHED:" + fetchedUser);
        
        return fetchedUser;
    }
    
    @Test
    @Order(2)
    public void testDeleteUserById() {
    	deleteUser(14, HttpStatus.NOT_FOUND);
    }
    
    public void deleteUser(Integer id, HttpStatus expected) {
    	Map<String, Integer> parameters = new HashMap<>();
    	parameters.put("id", id);
    	
    	UriComponents uri = UriComponentsBuilder.fromHttpUrl(getRootUrl() + "/users/{id}").build();
    	
    	ResponseEntity<String> deleteResponse = restTemplate.exchange(uri.toString(), 
    			HttpMethod.DELETE, 
    			HttpEntity.EMPTY,
				String.class,
				parameters);
    	
    	assertEquals(expected, deleteResponse.getStatusCode());
    	assertNotNull(deleteResponse.getBody());
    	
    	if(expected == HttpStatus.NOT_FOUND) {
    		assertTrue(deleteResponse.getBody().toString().contains("No such USER present with id : 14"));
    		System.out.println("No such USER present with id : 14 ( EXPECTED )");
    	}else {
    		assertEquals("User with id : "+id+" deleted successfully", deleteResponse.getBody());
    		System.out.println("User with id : "+id+" deleted successfully");
    	}
    }
    
  	public void testGetAllUsers() {
  		ResponseEntity<List<User>> response = restTemplate.exchange(getRootUrl() + "/users", 
  				HttpMethod.GET, 
  				HttpEntity.EMPTY,
  				new ParameterizedTypeReference<List<User>>() {});
  		assertEquals(HttpStatus.OK, response.getStatusCode());
  		List<User> list = response.getBody();
  		System.out.println("ALL USER Successfully FETCHED");
  		list.forEach(System.out::println);
  		assertNotNull(list);
  		assertTrue(!list.isEmpty());
  	}
}
