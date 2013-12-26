package com.marlboro.core.model.manager;

import org.hibernate.Session;

import com.marlboro.core.model.HibernateUtil;
import com.marlboro.core.model.dto.TestData;
import com.marlboro.util.Generator;

public class TestDataManager {

	public TestData putTestData(TestData testData) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();

		TestData data = new TestData();
		data.setPrimaryKey(Generator.makeSynthesisKey(testData.getMarlboroId(), testData.getMarlboroNumber()));
		data.setMarlboroId(testData.getMarlboroId());
		data.setMarlboroPw(testData.getMarlboroPw());
		data.setMarlboroNumber(testData.getMarlboroNumber());
		session.save(data);

		HibernateUtil.commit();
		HibernateUtil.closeSession();
		return data;
	}

	public TestData updateTestData(String marlboroId, String marlboroNumber, String newPassWord) {
		HibernateUtil.beginTransaction();
		TestData testData = getTestData(marlboroId, marlboroNumber);
		testData.setMarlboroPw(newPassWord);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
		
		return testData;  
	}
	

	public void deleteArticle(String marlboroId, String marlboroNumber) {
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		TestData testData = getTestData(marlboroId, marlboroNumber);  
		session.delete(testData);
		
		HibernateUtil.commit();
		HibernateUtil.closeSession();
	}  

	public TestData getTestData(String marlboroId, String marlboroNumber) {  
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		String primaryKey = Generator.makeSynthesisKey(marlboroId, marlboroNumber);
		
		return (TestData) session.get(TestData.class, primaryKey);
	}  
}
