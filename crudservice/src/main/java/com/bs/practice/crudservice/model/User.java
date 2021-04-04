package com.bs.practice.crudservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", address=" + address + ", salary=" + salary + ", age=" + age
				+ "]";
	}
	@Id
	@GeneratedValue
    private Integer id;
    private String name;
    private String address;
    private Integer salary;
    private Integer age;
    
    public User() {}
	
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public User copy(User user) {
    	this.id = user.id;
    	this.name = user.name;
    	this.address = user.address;
    	this.age = user.age;
    	this.salary = user.salary;
    	return this;
    }
}
