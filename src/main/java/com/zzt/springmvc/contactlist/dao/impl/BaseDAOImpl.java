   package com.zzt.springmvc.contactlist.dao.impl;

import java.io.Serializable;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.zzt.springmvc.contactlist.common.MD5Util;
import com.zzt.springmvc.contactlist.dao.BaseDAO;
import com.zzt.springmvc.contactlist.entity.User;
import com.zzt.springmvc.contactlist.redis.RedisClientTemplate;


@Repository("baseDAO")
@SuppressWarnings("all")
public class BaseDAOImpl<T> implements BaseDAO<T> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
//	@Resource(name="redisClientTemplate")
//    protected RedisTemplate<Serializable, Serializable> redisTemplate;
	
	@Resource(name="redisClientTemplate")
	private RedisClientTemplate redisClientTemplate;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public Serializable save(final T o) {
		final String addSql = "insert into user(create_time,user_name,password,phone_number,address) values(?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator(){  
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {  
                PreparedStatement ps =  
                    conn.prepareStatement(addSql, new String[]{"id"});//返回id
                final User user = (User)o;
                java.sql.Date date = new java.sql.Date(new Date().getTime());
                ps.setDate(1, date);  
                ps.setString(2, user.getUserName());  
                ps.setString(3, MD5Util.string2MD5(user.getPassword()));  
                ps.setString(4, user.getPhoneNumber());  
                ps.setString(5, user.getAddress());
                System.out.println(user.getAddress());
                
                redisClientTemplate.set("user.id." + user.getId(), user.getId().toString());
                redisClientTemplate.set("user.pwd." + user.getId(), user.getPassword().toString());
                redisClientTemplate.set("user.phoneNumber." + user.getId(), user.getPhoneNumber().toString());
                redisClientTemplate.set("user.address." + user.getId(), user.getAddress().toString());
                redisClientTemplate.set("user.userName", user.getUserName().toString());
//                redisTemplate.execute(new RedisCallback<Object>() {
//
//                    @Override
//                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                        connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
//                                       redisTemplate.getStringSerializer().serialize(user.getUserName()));
//                        return null;
//                    }
//                });
                return ps;  
            }  
              
        });
		System.out.println(keyHolder.getKey());  
		return keyHolder.getKey();
//		this.getCurrentSession().save(o);
	}

	public void delete(T o) {
		User user = (User) o;
		String[] keys = { "user.id." + user.getId(),
				"user.pwd." + user.getId(),
				"user.phoneNumber." + user.getId(),
				"user.address." + user.getId(),
				"user.createDateTime." + user.getId(),
				"user.userName." + user.getId(),
				"user.picUrl." + user.getId() };
		redisClientTemplate.del("user.id."+user.getId());
		for (int i = 0; i < keys.length; i++) {
			redisClientTemplate.del(keys[i]);
		}
		this.getCurrentSession().delete(o);
	}

	public void update(T o) {
		this.getCurrentSession().update(o);
	}

	public void saveOrUpdate(T o) {
		redisClientTemplate.del("user.id."+((User)o).getId());
		this.getCurrentSession().saveOrUpdate(o);
	}

	public List<T> find(String hql) {
//		return this.getCurrentSession().createQuery(hql).list();
		List<User> userLst = new ArrayList<User>();
		List<Map<String, Object>> userMaps = jdbcTemplate.queryForList(hql);
		Iterator iterator = userMaps.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> tempMap = (Map<String, Object>) iterator.next();
			User tempUser = new User();
			tempUser.setAddress(tempMap.get("address") == null?"":tempMap.get("address").toString());
			tempUser.setCreateTime((Date)tempMap.get("create_time"));
			tempUser.setId((Integer)tempMap.get("id"));
			tempUser.setPhoneNumber((String)tempMap.get("phone_number"));
			tempUser.setUserName((String)tempMap.get("user_name"));
			tempUser.setPicUrl((String)tempMap.get("pic_url"));
			userLst.add(tempUser);
			
			Set<String> keys = tempMap.keySet();
			for (String string : keys) {
				System.out.println("key:"+string + "----------------" + "value:" + tempMap.get(string));
			}
		}
		return (List<T>) userLst;
	}

	public List<T> find(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}

	public List<T> find(String hql, List<Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.list();
	}

	public List<T> find(String hql, Object[] param, Integer page, Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	public List<T> find(String hql, List<Object> param, Integer page, Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	public T get(Class<T> c, final Serializable id) {
		
//		Object object = redisTemplate.execute(new RedisCallback<T>() {
//
//			@Override
//			public T doInRedis(RedisConnection connection) throws DataAccessException {
//				byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);
//                if (connection.exists(key)) {
//                    byte[] value = connection.get(key);
//                    String name = redisTemplate.getStringSerializer().deserialize(value);
//                    User user = new User();
//                    user.setUserName(name);
//                    return (T) user;
//                }
//				return null;
//			}
//		
//		});
		
		Integer uid = redisClientTemplate.get("user.id."+id) != null ? Integer.valueOf(redisClientTemplate.get("user.id."+id)):null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		if (null!=uid) {
			User user = new User();
			user.setId(uid);
			user.setAddress(redisClientTemplate.get("user.address."+id));
			user.setPassword(redisClientTemplate.get("user.pwd."+id));
			user.setPhoneNumber(redisClientTemplate.get("user.phoneNumber."+id));
			user.setUserName(redisClientTemplate.get("user.userName."+id));
			user.setPicUrl(redisClientTemplate.get("user.picUrl."+id));
			try {
				user.setCreateTime(sdf.parse(redisClientTemplate.get("user.createDateTime."+id)));
				user.setUpdateTime(sdf.parse(redisClientTemplate.get("user.updateDateTime."+id)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return (T) user;
		}
		
//		if (null!=object) {
//			return (T) object;
//		}else {
			User user = (User) this.getCurrentSession().get(c, id);
//			redisTemplate.execute(new RedisCallback<Object>() {
//
//                @Override
//                public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                    connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
//                                   redisTemplate.getStringSerializer().serialize(user.getUserName()));
//                    return null;
//                }
//            });
			 redisClientTemplate.set("user.id." + user.getId().toString(), user.getId().toString());
             redisClientTemplate.set("user.pwd." + user.getId().toString(), user.getPassword().toString());
             redisClientTemplate.set("user.phoneNumber." + user.getId().toString(), user.getPhoneNumber().toString());
             redisClientTemplate.set("user.address." + user.getId().toString(), user.getAddress().toString());
             redisClientTemplate.set("user.createDateTime." + user.getId().toString(), sdf.format(user.getCreateTime()));
             redisClientTemplate.set("user.updateDateTime." + user.getId().toString(), sdf.format(user.getUpdateTime()));
             redisClientTemplate.set("user.userName." + user.getId().toString(), user.getUserName().toString());
             redisClientTemplate.set("user.picUrl." + user.getId().toString(), user.getPicUrl().toString());
			return (T) user;
//		}
	}

	public T get(String hql, Object[] param) {
		List<T> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		} else {
			return null;
		}
	}

	public T get(String hql, List<Object> param) {
		List<T> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		} else {
			return null;
		}
	}

	public Long count(String hql) {
		return (Long) this.getCurrentSession().createQuery(hql).uniqueResult();
	}

	public Long count(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return (Long) q.uniqueResult();
	}

	public Long count(String hql, List<Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return (Long) q.uniqueResult();
	}

	public Integer executeHql(String hql) {
		return this.getCurrentSession().createQuery(hql).executeUpdate();
	}

	public Integer executeHql(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}

	public Integer executeHql(String hql, List<Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.executeUpdate();
	}

}
