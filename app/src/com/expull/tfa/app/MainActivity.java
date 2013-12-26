package com.expull.tfa.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Socket socket = null;
	private String pid;
	private String mac;
	private final Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		assignPid();
		assignMac();
		if(socket != null) {
			registAuth(pid, mac);
		}
	}
	
	public void onClickRegist(View v) {
		registAuth(pid, mac);
	}
	
	private void assignPid() {
		pid = getMyPhoneNumber();
		if(pid.startsWith("+82")) pid = pid.replace("+82", "0");
		EditText.class.cast(findViewById(R.id.editPid)).setText(pid);
	}
	
	private void assignMac() {
		mac = getMacId().replace(":", "").replace("-", "");
		EditText.class.cast(findViewById(R.id.editMac)).setText(mac);		
	}
	
	private String getMacId() {
	    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	    return wifiInfo.getBSSID();
	}
	
	private String getMyPhoneNumber(){
	    TelephonyManager mTelephonyMgr;
	    mTelephonyMgr = (TelephonyManager)
	        getSystemService(Context.TELEPHONY_SERVICE); 
	    return mTelephonyMgr.getLine1Number();
	}

	public void registAuth(String pid, String mac) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainActivity.this.reconnectSocketToServer();
				String request = buildRequest(MainActivity.this.pid, MainActivity.this.mac);
				sendMessageToSocket(request);
			}
		}).start();
	}

	private void sendMessageToSocket(String request) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(request);
			writer.flush();
		} catch (IOException e) {
			postToast(R.string.msg_failed_to_send_request);
		}
		
	}

	private void postToast(final int stringId) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, stringId, Toast.LENGTH_SHORT);			
			}
		});
		
	}

	private void reconnectSocketToServer() {
		closeSocket();
		try {
			String host = getString(R.string.tfa_host);
			int port = Integer.parseInt(getString(R.string.tfa_port));
			Log.d("TFA", host+", "+port);
			socket = new Socket(host, port);
		} catch (Throwable t) {
			postToast(R.string.msg_failed_to_connect_tfa);
			socket = null;
		}
	}

	private void closeSocket() {
		if(socket  == null) return;
		try {socket.close(); } catch(Throwable t) {}
		socket = null;
	}

	private String buildRequest(String pid, String mac) {
		return "{\"pid\":\""+pid+"\", \"mac\":\""+mac+"\"}";
	}
}
