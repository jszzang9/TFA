package com.marlboro.util.QueuedLogger;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * QueuedLogger 클래스에서 사용하는 로그 데이터 클래스이다.
 * 
 * @author delta829
 */
public class QueuedTransactionLogs {
	private ArrayList<QueuedTransactionLog> logs = new ArrayList<QueuedTransactionLog>();
	
	/**
	 * {@link QueuedTransactionLogs} 생성자.
	 */
	public QueuedTransactionLogs() {
	}
	
	/**
	 * {@link QueuedTransactionLogs} 생서자.
	 * 
	 * @param log 트랜잭션 로그.
	 */
	public QueuedTransactionLogs(QueuedTransactionLog log) {
		logs.add(log);
	}
	
	/**
	 * 트랜잭션 로그 리스트를 가져온다.
	 * 
	 * @return 트랙잭션 로그 리스트.
	 */
	public ArrayList<QueuedTransactionLog> getLogs() {
		return logs;
	}
	
	/**
	 * 메시지를 추가한다.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 */
	public void add(Level level, Object message) {
		add(new QueuedTransactionLog(level, message));
	}
	
	/**
	 * 메시지를 추가한다.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 * @param throwable Throwable.
	 */
	public void add(Level level, Object message, Throwable throwable) {
		add(new QueuedTransactionLog(level, message, throwable));
	}
	
	/**
	 * 트랜잭션 로그를 추가한다.
	 * @param log 트랜잭션 로그.
	 */
	public void add(QueuedTransactionLog log) {
		logs.add(log);
	}
	
	/**
	 * 로그를 출력한다.
	 * @param logger 출력할 로그.
	 */
	public void log(Logger logger) {
		for (QueuedTransactionLog log : logs)
			log.log(logger);
	}
}
