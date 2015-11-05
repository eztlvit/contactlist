package com.zzt.springmvc.contactlist.service;

import java.util.List;

import com.zzt.springmvc.contactlist.entity.User;
import com.zzt.springmvc.contactlist.entity.UserMyBatis;


public interface UserService {

	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public User findUserById(int id);
	
	public void saveOrUpdate(User user);
	
	public void deleteUser(User user);
	
	public List<User> findAllList();
	
	public User findUserByNameAndPassword(String username, String password);
	
	public List<User> checkUserName(String username); 
	
	public void saveUserByMyBatis(UserMyBatis userMyBatis);
	
	public List<UserMyBatis> getList(); 
}
