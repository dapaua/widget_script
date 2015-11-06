package com.dapaua.trmwidget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {

	String paypaldonatelink="https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=EMDEEZW7SAJZG&lc=ES&item_name=dapaua&item_number=NW&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted";
	String threadlink="http://forum.xda-developers.com/showthread.php?p=41139073";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebView wv = (WebView)this.findViewById(R.id.webView1);
		wv.loadUrl("file:///android_asset/manual.html");
		Button donate = (Button)findViewById(R.id.donate);
		Button thread = (Button)findViewById(R.id.thread);
		Button install = (Button)findViewById(R.id.install);
		
		donate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Uri uri = Uri.parse(paypaldonatelink);
            	 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            	 startActivity(intent);
                
            }
        });
		
		thread.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
             Uri uri = Uri.parse(threadlink);
           	 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
           	 startActivity(intent);
            }
        });
		
		install.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//1-Create folder
            	String sdcard = Environment.getExternalStorageDirectory().getPath();
            	String fullpath=sdcard+"/"+"NowWidgets/scripts/";
            	File f=new File(fullpath);
            	if (!f.exists()) 
            		{
            			f.mkdirs();
            			//Should also copy the example scripts
            		}
            	//Find all files
            	AssetManager assetManager = getAssets();
                String[] files = null;
                try {
                    files = assetManager.list("scripts");
                } catch (IOException e) {
                    
                }
                
                for(String filename : files) {
                	System.out.println("TW:script:"+filename);
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                      in = assetManager.open("scripts/"+filename);
                      out = new FileOutputStream(fullpath + filename);
                      copyFile(in, out);
                      in.close();
                      in = null;
                      out.flush();
                      out.close();
                      out = null;
                    } catch(IOException e) {
                        //Log.e("tag", "Failed to copy asset file: " + filename, e);
                    }       
                }
                
                
            }
        });
		
	}

	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
