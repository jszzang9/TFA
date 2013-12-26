package com.marlboro.core.model;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.marlboro.util.QueuedLogger.QueuedTransactionLogs;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable ex) {
			QueuedTransactionLogs logs = new QueuedTransactionLogs();
			logs.add(Level.INFO, "======================================");
			logs.add(Level.FATAL, "fail to initialize Hibernate SessionFactory" + ex);
			logs.add(Level.INFO, "======================================");
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public static Transaction beginTransaction() {
		return getCurrentSession().beginTransaction();
	}
	
	public static void  commit() {
		getCurrentSession().getTransaction().commit();
	}
	
	public static void closeSession() {
		getCurrentSession().close();
	}
	
	public static void rollBack() {
		if (getCurrentSession().isOpen()) {
			Transaction tx = getCurrentSession().getTransaction();
			if (tx != null && tx.isActive())
				tx.rollback();
		}
	}
}
