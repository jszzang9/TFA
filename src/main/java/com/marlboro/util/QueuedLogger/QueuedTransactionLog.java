package com.marlboro.util.QueuedLogger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * QueuedLogger 에서 사용하는 로그 데이터 클래스이다.
 * 
 * @author delta829
 */
public class QueuedTransactionLog {	
	private Level level;
	private Object message;
	private Throwable throwable;
	
	/**
	 * {@link QueuedTransactionLog} 생성자.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 */
	public QueuedTransactionLog(Level level, Object message) {
		this.level = level;
		this.message = message;
		this.throwable = null;
	}

	/**
	 * {@link QueuedTransactionLog} 생성자.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 * @param throwable Throwable.
	 */
	public QueuedTransactionLog(Level level, Object message, Throwable throwable) {
		this.level = level;
		this.message = message;
		this.throwable = throwable;
	}
	
	/**
	 * 레벨을 가져온다.
	 * 
	 * @return 레벨.
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * 메시지를 가져온다.
	 * 
	 * @return 메시지.
	 */
	public Object getMessage() {
		return message;
	}
	
	/**
	 * Throwable을 가져온다.
	 *  
	 * @return Throwable.
	 */
	public Throwable getThrowable() {
		return throwable;
	}
	
	/**
	 * 로그를 출력한다.
	 * 
	 * @param logger 출력할 로거.
	 */
	public void log(Logger logger) {
		if (getLevel() == Level.INFO)
			logger.info(getMessage(), getThrowable());
		else if (getLevel() == Level.DEBUG)
			logger.debug(getMessage(), getThrowable());
		else if (getLevel() == Level.WARN)
			logger.warn(getMessage(), getThrowable());
		else if (getLevel() == Level.ERROR)
			logger.error(getMessage(), getThrowable());
		else if (getLevel() == Level.FATAL)
			logger.fatal(getMessage(), getThrowable());
		else if (getLevel() == Level.TRACE)
			logger.trace(getMessage(), getThrowable());
	}
}
