package com.dapaua.trmwidget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConfigureActivity extends Activity {

	

Button button1;
Button button2;
Button button3;
int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

private String getInfo(File file)
{
	String output="";
	BufferedReader reader = null;

	try {
	    reader = new BufferedReader(new FileReader(file));
	    String text = null;

	    	while ((text = reader.readLine()) != null) {
	    		String[] parts = text.split(" ");
	    		
	    		
	    		if (parts[0].equals("#INFO"))
	    			{
	    			output=text.substring(6);
	    			
	    			}
	    		
	    	}
		}
	 catch (Exception e)
	 {}
	return output;
	
}


@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);

	setResult(RESULT_CANCELED);

	String sdcard = Environment.getExternalStorageDirectory().getPath();
	String fullpath=sdcard+"/"+"NowWidgets/scripts/";
	
	
	File f=new File(fullpath);
	if (!f.exists()) 
		{
			f.mkdirs();
			
		}
	File [] files =f.listFiles();
	ArrayList<String> mylist = new ArrayList<String>();
	ArrayList<String> myinfo = new ArrayList<String>();
	ArrayList arraylistItems = new ArrayList<TwoLines>();
    arraylistItems= new ArrayList<TwoLines>();
	for (File myfile : files)
		{
		arraylistItems.add(new TwoLines(myfile.getName(),getInfo(myfile)));
		mylist.add(myfile.getName());
		myinfo.add(getInfo(myfile));
		}
	
     setContentView(R.layout.configure_activity);
     TextView tv= (TextView)findViewById(R.id.textView1);
     tv.setText("Getting scripts from:"+fullpath);
     
     MyAdapter adapter= new MyAdapter (this, arraylistItems);
     
        
     ListView lv = (ListView)findViewById(R.id.listView1);
     lv.setAdapter(adapter);
     lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    	 @Override
    	 public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    		 final Context context = ConfigureActivity.this;

    		 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    		 
    		 SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(context);
    		 SharedPreferences.Editor editor = sp.edit();
    		 ListView p =(ListView)parent;
    		 MyAdapter arrayAdapter =(MyAdapter)p.getAdapter();
    		 TwoLines script = (TwoLines)arrayAdapter.getItem(position);
    		 //System.out.println("TW:Script choosen:"+script.line1);
    		 editor.putString("script"+mAppWidgetId, script.line1);
    		 editor.commit();
    		 widget1.updateWidget(context, appWidgetManager, mAppWidgetId);
    		 Intent resultValue = new Intent();
    		 resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
    		 setResult(RESULT_OK, resultValue);
    		 finish();
    		 
    		 
    	             }
    	         });
  
     
     
     Intent intent = getIntent();
     Bundle extras = intent.getExtras();
     if (extras != null) {
         mAppWidgetId = extras.getInt(
                 AppWidgetManager.EXTRA_APPWIDGET_ID,
                 AppWidgetManager.INVALID_APPWIDGET_ID);
     }
  
     // If they gave us an intent without the widget id, just bail.
     if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
         finish();
     }
}




}
