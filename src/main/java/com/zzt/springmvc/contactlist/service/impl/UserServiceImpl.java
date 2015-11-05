package com.zzt.springmvc.contactlist.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzt.springmvc.contactlist.dao.BaseDAO;
import com.zzt.springmvc.contactlist.entity.User;
import com.zzt.springmvc.contactlist.entity.UserMyBatis;
import com.zzt.springmvc.contactlist.mapper.UserMyBatisMapper;
import com.zzt.springmvc.contactlist.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private BaseDAO<User> baseDAO;
	
	@Autowired(required = false)
	@Qualifier("userMyBatisMapper")
	private UserMyBatisMapper mapper;

	public void saveUser(User user) {
		baseDAO.save(user);
	}

	public void updateUser(User user) {
		baseDAO.update(user);
	}

	public User findUserById(int id) {
		return baseDAO.get(User.class, id);
	}

	public void deleteUser(User user) {
		baseDAO.delete(user);
	}

	public List<User> findAllList() {
//		return baseDAO.find(" from User u order by u.createTime");
		return baseDAO.find("select * from user u order by u.create_time");
	}
	
	public User findUserByNameAndPassword(String username, String password) {
		return baseDAO.get(
				" from User u where u.userName = ? and u.password = ? ",
				new Object[] { username, password });
	}

	public BaseDAO<User> getBaseDAO() {
		return baseDAO;
	}

	public void setBaseDAO(BaseDAO<User> baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void saveOrUpdate(User user) {
		baseDAO.saveOrUpdate(user);
	}

	@Override
	public List<User> checkUserName(String username) {
		// TODO Auto-generated method stub
		return baseDAO.find("select * from user u where u.user_name = '" + username + "' order by u.create_time");
	}
	
	@Override
	public void saveUserByMyBatis(UserMyBatis user) {
		mapper.insert(user);
	}
	
	@Override
	public List<UserMyBatis> getList(){
		return mapper.getList();
	}
}
