package com.marlboro.core.model.manager;

import org.hibernate.Session;

import com.marlboro.core.model.HibernateUtil;
import com.marlboro.core.model.dto.UserData;

public class UserManager {
	public UserData getUserData(String uid) {  
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		return (UserData) session.get(UserData.class, uid);
	}  


	public UserData putUserData(UserData userData) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		UserData data = new UserData();
		data.setUid(userData.getUid());
		data.setPid(userData.getPid());
		session.save(data);

		HibernateUtil.commit();
		HibernateUtil.closeSession();

		return data;
	}

	public UserData updateUserData(String uid, String newPid) {
		HibernateUtil.beginTransaction();
		UserData userData = getUserData(uid);
		userData.setPid(newPid);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
		return userData;
	}
	
	public void deleteUserData(String uid) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		UserData userData = getUserData(uid);
		session.delete(userData);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}
}
