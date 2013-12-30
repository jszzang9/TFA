package com.expull.tfa.core.protocol.model.manager;

import org.hibernate.Session;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.UserPcidData;
import com.expull.tfa.util.Generator;

public class UserPcidManager {
	public UserPcidData getUserPcIDData(String uid_pid) {  
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		return (UserPcidData) session.get(UserPcidData.class, uid_pid);
	}  


	public UserPcidData putUserPcidData(String uid, String pid) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		String uid_pid = Generator.makeSynthesisKey(uid,pid);
		
		UserPcidData userPcidData = new UserPcidData();
		userPcidData.setUserId_pcId(uid_pid);
		session.save(userPcidData);

		HibernateUtil.commit();
		HibernateUtil.closeSession();

		return userPcidData;
	}

//	public UserData updateUserData(String uid, String newPid) {
//		HibernateUtil.beginTransaction();
//		UserData userData = getUserData(uid);
//		userData.setPid(newPid);
//		
//		HibernateUtil.commit();
//		HibernateUtil.closeSession();
//		return userData;
//	}
	
	public void deleteUserPcidData(String uid, String pid) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		String uid_pid = Generator.makeSynthesisKey(uid,pid);
		
		UserPcidData userPcidData = getUserPcIDData(uid_pid);
		session.delete(userPcidData);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}
}
