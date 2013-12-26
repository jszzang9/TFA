package com.expull.tfa.core.agent_was.dcp;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.log4j.Level;

import com.expull.tfa.TFAConfiguration;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.expull.tfa.util.QueuedLogger.QueuedTransactionLogs;

/**
 * DCP 서버 대리 클래스이다.
 * 
 * @author delta829
 */
public class DCPProxy {

	/**
	 * DCP 서버에 메시지를 보낸다.
	 * 
	 * @param telegram 전문 번호.
	 * @param bodyData 바디 데이터.
	 * @return 응답 결과.
	 * @throws Exception
	 */
	public static String send(String telegram, String bodyData) throws Exception {
		Socket socket = new Socket(TFAConfiguration.getProperty("dcp.proxy.ip"), Integer.parseInt(TFAConfiguration.getProperty("dcp.proxy.port")));
		Writer writer = new OutputStreamWriter(socket.getOutputStream());
		Reader reader = new InputStreamReader(socket.getInputStream());
		
		int length = bodyData.getBytes().length;
		byte[] bytesLength = ByteBuffer.allocate(4).putInt(length).array();
		
		socket.getOutputStream().write(bytesLength);
		writer.write(telegram+bodyData);
		writer.flush();
		
		StringBuilder result = new StringBuilder();
		char[] buf = new char[1024];
		while (true) {
			int len = reader.read(buf);
			if (len < 0) 
				break;
			
			result.append(buf, 0, len);
			if (len < buf.length) 
				break;
		}
		
		writer.close();
		reader.close();
		socket.close();
		
		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, " ================== Send To DCP ================== ");
		logs.add(Level.INFO, "  > Telegram Number : " + telegram);
		logs.add(Level.INFO, "  > Body Data : " + bodyData);
		logs.add(Level.INFO, "  > Result : " + result.toString());
		logs.add(Level.INFO, "  > OK");
		logs.add(Level.INFO, " ================================================= ");
		QueuedLogger.push(logs);
		
		return result.toString();
	}
}
