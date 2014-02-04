package com.expull.tfa.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("JavascriptInterface")
public class HomeActivity extends Activity{
	private WebView mWebView;
	private String gcm_key;

	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
                
        setContentView(R.layout.main);

        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setOnKeyListener(new OnKeyListener() {			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
        		toast("wb : ["+keyCode+"]");
        		return false;
			}
        	
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new Object() {
        	public String readFile(String path) {
        		String result = "";
    			try {
    				result = readAllFromReader(new FileReader(new File(path)));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			return result;
        	}
        	
        	public String get(String key) {
				String dirPath = Environment.getDataDirectory() + "/data/com.expull.ebpp.smartstb";
				String filename = "/map.txt";

    			HashMap<String,String> keyMap = new HashMap<String,String>();
    			String value="";

    			try {
    	            FileInputStream fis = new FileInputStream(dirPath + filename);
    				ObjectInputStream data = new ObjectInputStream(fis);
    				keyMap = (HashMap<String,String>)data.readObject();

    				value = keyMap.get(key);

    				data.close();
    				fis.close();

    			} catch (ClassNotFoundException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			return value;
        	}

        	public void put(String key, String value){
        		String mSdPath = Environment.getDataDirectory() + "/data/com.expull.ebpp.smartstb";
				String filename = "/map.txt";

				File file = new File(mSdPath);

				if( !file.exists() ) {
					file.mkdirs();
				}

				HashMap<String , String> keyMap = new HashMap<String , String>();
				keyMap.put(key, value);

				File savefile = new File(mSdPath + filename);

				try{
					FileOutputStream f = new FileOutputStream(savefile);
					ObjectOutputStream fos = new ObjectOutputStream(f);
					fos.writeObject(keyMap);
					fos.flush();
				}
				catch(IOException e){
					e.printStackTrace();
				}
        	}
        	
        	public void finish(){
        		appfinish();
        	}

        	public String getGcmKey(){
        		return gcm_key;
        	}
        }, "PLATFORM");
		
        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(getString(R.string.app_host)+getString(R.string.app_home));
        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final android.webkit.JsResult result) {
				new AlertDialog.Builder(HomeActivity.this)
						.setTitle("Alert")
						.setMessage(message)
						.setPositiveButton(android.R.string.ok,
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										result.confirm();
									}
								}).setCancelable(false).create().show();

				return true;
			};
		});

	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		toast("["+keyCode+"]");
        if(isColorKey(keyCode)) {
    		toast("evaluateColorKey");
        	evaluateColorKey(keyCode);
        	return false;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private void toast(String string) {
//		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	static Map<Integer, Integer> KEY_MAP = new HashMap<Integer, Integer>();
	static {
		KEY_MAP.put(89, 81); // Q
		KEY_MAP.put(86, 87); // W
		KEY_MAP.put(85, 69); // E
		KEY_MAP.put(90, 82); // R
		KEY_MAP.put(KeyEvent.KEYCODE_BACK, 89); // Y
		KEY_MAP.put(88, 188); // <
		KEY_MAP.put(87, 190); // >
		//KEY_MAP.put(17, 8); // *
	}
	
	private void evaluateColorKey(int keyCode) {
		int mappedKeyCode = KEY_MAP.get(keyCode);
		String script = "event={keyCode:"+mappedKeyCode+"};event.preventDefault=function(){}; try{keydownHandler(event);}catch(e) {}";
		mWebView.loadUrl("javascript:"+script);
	}

	private boolean isColorKey(int keyCode) {
		return KEY_MAP.containsKey(keyCode);
	}

	private void appfinish(){
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(HomeActivity.this);
    	alert_confirm.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
    			new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			System.exit(0);
    		}
    	}).setNegativeButton("취소",
    			new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			return;
    		}
    	});
    	AlertDialog alert = alert_confirm.create();
    	alert.show();
	}

	@Override
	public void onPause() {
		System.exit(0);
	}

	private static String readAllFromReader(Reader reader) throws IOException {
		String result = "";
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			result += line + "\n";
		}
		reader.close();
		return result;
	}
	
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
        	mWebView.setVisibility(View.VISIBLE);

            super.onPageFinished(view, url);
        }
    }
}