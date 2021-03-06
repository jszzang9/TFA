package com.expull.tfa.core.protocol.model.manager;

import org.hibernate.Session;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.LidData;


public class LidManager {
	public LidData getLidData(String mac) {  
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		return (LidData) session.get(LidData.class, mac);
	}  


	public LidData putLidData(LidData lidData) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		LidData data = new LidData();
		data.setLid(lidData.getLid());
		data.setMac(lidData.getMac());
		session.save(data);

		HibernateUtil.commit();
		HibernateUtil.closeSession();

		return data;
	}

	public LidData updateLidData(String lid, String newMac) {
		HibernateUtil.beginTransaction();
		LidData lidData = getLidData(lid);
		lidData.setMac(newMac);;
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
		return lidData;
	}
	
	public void deleteLidData(String lid) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		LidData lidData = getLidData(lid);
		session.delete(lidData);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}
}
