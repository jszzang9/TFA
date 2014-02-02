package com;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.expull.tfa.core.protocol.HibernateUtil;
import com.expull.tfa.core.protocol.model.dto.LidData;
import com.expull.tfa.core.protocol.model.dto.UserData;
import com.expull.tfa.core.protocol.model.manager.LidManager;
import com.expull.tfa.core.protocol.model.manager.PcidManager;
import com.expull.tfa.core.protocol.model.manager.UserManager;

public class TestDataIT{
	private UserManager dataManager;
	private PcidManager daPcidManager;
	private LidManager lidManager;
	
	@Test
	public void updata() {
		try {
			dataManager = new UserManager();
			HibernateUtil.beginTransaction();
			UserData userData = new UserData("jszzang8", "8080");
			dataManager.putUserData(userData);
			
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
	}
	
	@Test @Ignore
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
	
	@Test @Ignore
	public void lidGet() {
		try {
			lidManager = new LidManager();
			HibernateUtil.beginTransaction();
			
			LidData d = lidManager.getLidData("0026662E777D");
			HibernateUtil.commit();
			HibernateUtil.closeSession();
			
			Assert.assertEquals("expull", d.getLid());
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
			ex.printStackTrace();
		}
	}
}
