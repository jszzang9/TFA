package com.expull.tfa.core.protocol.model;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.LidData;
import com.expull.tfa.core.protocol.model.dto.MasterData;
import com.expull.tfa.core.protocol.model.dto.PcidData;
import com.expull.tfa.core.protocol.model.dto.UserData;
import com.expull.tfa.core.protocol.model.manager.LidManager;
import com.expull.tfa.core.protocol.model.manager.MasterManager;
import com.expull.tfa.core.protocol.model.manager.PcidManager;
import com.expull.tfa.core.protocol.model.manager.UserManager;


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
			lidData = lidManager.getLidData(mac);
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			ex.printStackTrace();
		}
		
		return lidData != null ? lidData.getLid() : "";
	}

	public String[] getPcidsFor(String lid) {
		PcidManager pcidManager;
		String[] pcids = null;
		try {
			pcidManager = new PcidManager();
			HibernateUtil.beginTransaction();
			
			pcids= pcidManager.getPcidsFor(lid);
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
		
		return pcids;
	}
	
	public MasterData[] getMasters() {
		MasterManager manager;
		MasterData[] masters = null;
		try {
			manager = new MasterManager();
			masters= manager.getMasters();
		}
		catch(Throwable ex) {
			ex.printStackTrace();
		}
		
		return masters;
	}

	public MasterData getMasterForUid(String uid) {
		MasterManager manager;
		MasterData[] masters = null;
		try {
			manager = new MasterManager();
			HibernateUtil.beginTransaction();
			
			masters= manager.getMastersForUserid(uid);
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			HibernateUtil.rollBack();
		}
		
		return masters.length>0?masters[0]:null;
	}

	public void createMasterWitTokenAndContact(String token, String contact) {
		MasterManager manager = new MasterManager();
		MasterData master = new MasterData();
		master.setToken(token);
		master.setContact(contact);
		manager.put(master);
	}
}
