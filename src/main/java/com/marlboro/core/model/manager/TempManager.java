package com.marlboro.core.model.manager;

import com.marlboro.core.model.HibernateUtil;
import com.marlboro.core.model.dto.LidData;
import com.marlboro.core.model.dto.PcidData;
import com.marlboro.core.model.dto.UserData;

public class TempManager {
	private static TempManager singleton = new TempManager();
	public static TempManager getInstance() { return singleton ; }

	public String getLidByPcid(String pcid) {
		PcidManager pcidManager;
		PcidData data = null;
		try {
			pcidManager = new PcidManager();
			HibernateUtil.beginTransaction();

			data = pcidManager.getPcidData(pcid);
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}

		return data.getLid();
	}

	public String getPidOf(String uid) {
		UserManager userManager;
		UserData userData = null;
		
		try {
			userManager = new UserManager();
			HibernateUtil.beginTransaction();

			userData = userManager.getUserData(uid);
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();

		}
		return userData.getPid();
	}
	
	public String getLidByMac(String mac) {
		LidManager lidManager;
		LidData lidData = new LidData();
		
		try {
			lidManager = new LidManager();
			HibernateUtil.beginTransaction();
			
			lidData = lidManager.getLidData(mac);
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
		
		return lidData.getLid();
	}
}
