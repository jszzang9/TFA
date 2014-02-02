package com.expull.tfa.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private final int REQUEST_ENABLE_BT = 3;
	private BluetoothAdapter mBluetoothAdapter;
	private final ArrayList<String> mArrayAdapter = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initBTReceiver();
	}

	private void initBTReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
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
//			registAuth(pid, mac);
		}
	}
	
	public void onClickRegist(View v) {
		registAuth(pid, mac);
	}
	
	public void onClickBluetooth(View v) {
		pairBluetooth();
	}
	
	private void pairBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			postToast("failed to initialize BT");
		}
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT ) ;
		    return;
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
		    for (BluetoothDevice device : pairedDevices) {
		        mArrayAdapter .add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		mBluetoothAdapter.startDiscovery(); 
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        }
	    }
	};

	public void onClickExpire(View v) {
		expireSession();
	}

	private void expireSession() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					readAllFromURLWithPOST("http://ec000.expull.com:10481/expireSession", "pid="+MainActivity.this.pid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void assignPid() {
		pid = getMyPhoneNumber();
		if(pid.startsWith("+82")) pid = pid.replace("+82", "0");
		EditText.class.cast(findViewById(R.id.editPid)).setText(pid);
	}
	
	private void assignMac() {
		if(getMacId() == null) return;
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

	private void postToast(final String string) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT);			
			}
		});
		
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
	
	public static String readAllFromURLWithPOST(String url, String post) throws Exception {
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		wr.write(post);
		wr.flush();
		return readAllFromReader(new InputStreamReader(connection.getInputStream())).trim();
	}

	private static String readAllFromReader(Reader reader) throws IOException {
		return readAllFromReader(reader, true);
	}

	public static String readAllFromReader(Reader reader, boolean close) throws IOException {
		StringBuilder result = new StringBuilder();
		char[] buf = new char[1024];
		while(true) {
			int len = reader.read(buf);
			if(len < 0) break;
			result.append(buf, 0, len);
			if(len < buf.length) break;
		}
		return result.toString();
	}
}
