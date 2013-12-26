package com;

import org.junit.Test;

import com.integration_test.protocol.ProtocolIT;
import com.marlboro.core.model.HibernateUtil;
import com.marlboro.core.model.manager.LidManager;
import com.marlboro.core.model.manager.PcidManager;
import com.marlboro.core.model.manager.UserManager;

public class TestDataIT extends ProtocolIT{
	private UserManager dataManager;
	private PcidManager daPcidManager;
	private LidManager lidManager;
	
	@Test
	public void updata() {
		try {
			dataManager = new UserManager();
			HibernateUtil.beginTransaction();
			
			dataManager.getUserData("jszzang9");
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
	}
	
	@Test
	public void pcidGet() {
		try {
			daPcidManager = new PcidManager();
			HibernateUtil.beginTransaction();
			
			daPcidManager.getPcidData("pc01");
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
	}
	
	@Test
	public void lidGet() {
		try {
			lidManager = new LidManager();
			HibernateUtil.beginTransaction();
			
			lidManager.getLidData("expull");
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
	}
}
