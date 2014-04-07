package com.expull.tfa.core.protocol.model.manager;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.MasterData;

public class MasterManager {
	public MasterData[] getMasters() {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		Criteria cr = session.createCriteria(MasterData.class);
		List list = cr.list();

		HibernateUtil.commit();
		HibernateUtil.closeSession();
		
		MasterData[] result = new MasterData[list.size()];
		for(int i=0;i<result.length;i++) {
			result[i] = ((MasterData)list.get(i));
		}

		return result;
	}

	public MasterData[] getMastersForUserid(String userid) {
		Session session = HibernateUtil.getCurrentSession();

		Criteria cr = session.createCriteria(MasterData.class);
		cr.add(Restrictions.eq("userid", userid));
		List list = cr.list();
		
		MasterData[] result = new MasterData[list.size()];
		for(int i=0;i<result.length;i++) {
			result[i] = (MasterData)list.get(i);
		}

		return result;
	}

	public void put(MasterData master) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		session.save(master);

		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}
}
