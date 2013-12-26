package com.marlboro.util.QueuedLogger;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 로그를 순차적으로 나타낼 수 있도록 해주는 클래스이다.
 * 
 * @author delta829
 */
public class QueuedLogger {
	private static final Logger log4jLogger = Logger.getLogger(QueuedLogger.class);

	private static Thread thread = null;
	private static LinkedBlockingQueue<QueuedTransactionLogs> logQueue = new LinkedBlockingQueue<QueuedTransactionLogs>();

	/**
	 * {@link QueuedLogger} 생성자.
	 */
	private QueuedLogger() {
	}

	/**
	 * 로그 쓰레드를 끝낸다.
	 */
	public static void shutdown() {
		while (logQueue.isEmpty() == false) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		thread.interrupt();
		thread = null;
	}

	/**
	 * 로그 큐에 입력한다.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 */
	public static void push(Level level, Object message) {
		push(new QueuedTransactionLogs(new QueuedTransactionLog(level, message)));
	}

	/**
	 * 로그 큐에 입력한다.
	 * 
	 * @param level 레벨.
	 * @param message 메시지.
	 * @param throwable Throwable.
	 */
	public static void push(Level level, Object message, Throwable throwable) {
		push(new QueuedTransactionLogs(new QueuedTransactionLog(level, message, throwable)));
	}

	/**
	 * 로그 큐에 입력한다.
	 * 
	 * @param log 트랜잭션 로그.
	 */
	public static void push(QueuedTransactionLog log) {
		push(new QueuedTransactionLogs(log));
	}

	/**
	 * 로그 큐에 입력한다.
	 * 
	 * @param logs 트랙잭션 로그 리스트.
	 */
	public static synchronized void push(QueuedTransactionLogs logs) {
		if (thread == null) {
			thread = new Thread(new LoggingRunnale());
			thread.setName("DCP Queued Logger");
			thread.start();
		}

		logQueue.offer(logs);
	}

	/**
	 * 로그 쓰레드 수행 클래스이다.
	 * 
	 * @author delta829
	 */
	private static class LoggingRunnale implements Runnable {

		public void run() {
			try {
				while (true) {
					QueuedTransactionLogs logs = logQueue.take();
					logs.log(log4jLogger);
				}
			}
			catch (InterruptedException e) {
			}
		}
	}
}
