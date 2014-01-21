package com.expull.tfa.core.protocol.model.manager;

import org.hibernate.Session;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.PcidData;


public class PcidManager {
	public PcidData getPcidData(String pcid) {  
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		return (PcidData) session.get(PcidData.class, pcid);
	}  


	public PcidData putPcidData(PcidData pcidData) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		PcidData data = new PcidData();
		data.setPcid(pcidData.getPcid());
		data.setLid(pcidData.getLid());
		session.save(data);

		HibernateUtil.commit();
		HibernateUtil.closeSession();

		return data;
	}

	public PcidData updatePcidData(String pcid, String newLid) {
		HibernateUtil.beginTransaction();
		PcidData pcidData = getPcidData(pcid);
		pcidData.setLid(newLid);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
		return pcidData;
	}
	
	public void deletePcidData(String pcid) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		PcidData pcidData = getPcidData(pcid);
		session.delete(pcidData);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}
}
