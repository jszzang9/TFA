package com;

import org.junit.Ignore;
import org.junit.Test;

import com.integration_test.protocol.ProtocolIT;
import com.marlboro.core.model.HibernateUtil;
import com.marlboro.core.model.manager.TestDataManager;

public class TestDataIT extends ProtocolIT{
	private TestDataManager dataManager;
	
	
	@Test @Ignore
	public void updata() {
		try {
			dataManager = new TestDataManager();
			HibernateUtil.beginTransaction();
			
			dataManager.deleteArticle("jszzang8", "fecfbdbfbaeebd");
			HibernateUtil.commit();
			HibernateUtil.closeSession();
		}
		catch(Throwable ex) {
			HibernateUtil.rollBack();
		}
	}
}
