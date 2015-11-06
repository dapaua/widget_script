package com.dapaua.trmwidget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	int timeout = 15; // Timeout in seconds;
	public RemoteViews remoteViews;
	public Context scontext;
	public AppWidgetManager sappWidgetManager;
	public ComponentName myWidget;
	public String text1;
	public boolean do_refresh=false;
	List<String> al_env;
	
	public static final int CODE_REFRESH=0;
	public static final int CODE_TEXT0=100;
	public static final int CODE_TEXT1=101;
	public static final int CODE_TEXT2=102;
	
	public static final int CODE_ICON1=1;
	public static final int CODE_ICON2=2;
	public static final int CODE_ICON3=3;
	public static final int CODE_ICON4=4;
	public static final int CODE_ICON5=5;
	public static final int CODE_ICON6=6;
	public static final int CODE_ICON7=7;
	public static final int CODE_ICON8=8;
	public static final int CODE_ICON9=9;
	public static final int CODE_ICON10=10;
	public static final int CODE_ICON11=11;
	public static final int CODE_ICON12=12;
	
	
	 AlarmManager m;
	 boolean debug = false;
	
	 @Override  
	    public void onCreate()  
	    {  
	        super.onCreate();  
	    }  
	  
	    @Override  
	    public int onStartCommand(Intent intent, int flags, int startId)  
	    {  
	    	if (intent==null) {return 0;}
	    	int widget = intent.getIntExtra("widget", 0);
	    	int code = intent.getIntExtra("code", CODE_REFRESH);
	    	//SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(this);
	    	//SharedPreferences.Editor editor = sp.edit();
	    	//editor.putInt("code"+widget, code);
	    	
	    	
	    			
	        buildUpdate(widget,code);  
	  
	        return super.onStartCommand(intent, flags, startId);  
	    }  
	  
	    private void buildUpdate(int widget, int code)  
	    {  
	    	if (debug) System.out.println("TW:Update called for widget"+widget);
	        AppWidgetManager awm =AppWidgetManager.getInstance(getBaseContext());
	        
	        RemoteViews myview = getView(getBaseContext(),awm,widget, code);
	        
	        //If there was a refresh command, do the refresh.
	        //This is to allow response from scripts without refreshing.
	        
	    }  
	  
	    @Override  
	    public IBinder onBind(Intent intent)  
	    {  
	        return null;  
	    }  
	    
	    private void preParse(Context context,int wid, String path)
	    {
	    	/*
	    	This function reads the script and search for the variables that should be predefined.
	    	
	    	Commands:
	    	#GETVAR VAR DEFAULT
	    	#Reads from SP:
	    	# var_wid_var and sets it to VAR. If not found, sets the default value.
	    	#-----
	    	#REQUIRES ITEM ITEM ITEM
	    	#Presets variables like NW_BATTERY
	    	#
	    	*/
	    	SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(context);
	    	File file = new File(path);
	    	BufferedReader reader = null;

	    	try {
	    	    reader = new BufferedReader(new FileReader(file));
	    	    String text = null;

	    	    	while ((text = reader.readLine()) != null) {
	    	    		String[] parts = text.split(" ");
	    	    		
	    	    		
	    	    		if (parts[0].equals("#GETVAR"))
	    	    			{
	    	    			if (parts.length>1)	
	    	    				{
	    	    				String defaultv ="";
	    	    				if (parts.length>2)
	    	    					{defaultv=parts[2];}
	    	    				String pref_name="var_"+wid+"_"+parts[1];
	    	    				String v=sp.getString(pref_name, defaultv);
	    	    				al_env.add(parts[1]+"="+v);
	    	    				}
	    	    			
	    	    			}
	    	    		if (parts[0].equals("#REQUIRES"))
    	    			{
    	    			}
	    	    	}
	    		}
	    	 catch (Exception e)
	    	 {}
	    	
	    }


	    private void runScript(int appWidgetId, Context context, int code)
	    	{
	    	

			m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			//myWidget = new ComponentName(context, widget1.class);
			
			int wid;

			if (debug) System.out.println("TW:Widget id:"+appWidgetId);
				wid = appWidgetId;
				
			
			ArrayList<String> cmds = new ArrayList<String>();
			String script ="time";
			SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(context);
			script = sp.getString("script"+appWidgetId, "time");
			
			String stcode = "REFRESH";
			if (code==CODE_REFRESH){stcode="REFRESH";}
			if (code==CODE_ICON1){stcode="ICON1";}
			if (code==CODE_ICON2){stcode="ICON2";}
			if (code==CODE_ICON3){stcode="ICON3";}
			if (code==CODE_ICON4){stcode="ICON4";}
			if (code==CODE_ICON5){stcode="ICON5";}
			if (code==CODE_ICON6){stcode="ICON6";}
			if (code==CODE_ICON7){stcode="ICON7";}
			if (code==CODE_ICON8){stcode="ICON8";}
			if (code==CODE_ICON9){stcode="ICON9";}
			if (code==CODE_ICON10){stcode="ICON10";}
			if (code==CODE_ICON11){stcode="ICON11";}
			if (code==CODE_ICON12){stcode="ICON12";}
			
			
			if (code==CODE_TEXT1){stcode="TEXT";}
			
			//TEST
			//if (wid==36) {script="issunday";}
			//if (wid==38) {script="pingtest";}
			//if (wid==39) {script="time";}
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			String fullpath=sdcard+"/"+"NowWidgets/scripts/";
			String datapath=sdcard+"/"+"NowWidgets/data/"+wid+"/";
			File f = new File(datapath);
			f.mkdirs();
			if (debug) System.out.println("TW:datapath:"+f.getAbsolutePath());
			
			cmds.add("sh "+fullpath+script+ " "+stcode);
			String all_output = "";
			String bat = String.valueOf((int)getBatteryPercent());
			al_env= new ArrayList<String>();
			Map<String, String> variables = System.getenv();  
			for (Map.Entry<String, String> entry : variables.entrySet())  
			{  
			   String name = entry.getKey();  
			   String value = entry.getValue();  
			   //if (debug) System.out.println("TW:"+name + "=" + value);
			   al_env.add(name+"="+value);
			}  
			al_env.add("NW_BATTERY="+bat);
			al_env.add("NW_DATA_PATH="+datapath);
			
			preParse(context,wid,fullpath+script);
			
			String[] env = al_env.toArray(new String[al_env.size()]); 
			for (String cmd : cmds) {
				try {

					if (debug)  System.out.println("TW:Executing:" + cmd);

					Process process = Runtime.getRuntime().exec(cmd,env);

					long now = System.currentTimeMillis();
					long timeoutInMillis = 1000L * timeout;
					long finish = now + timeoutInMillis;
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream()));
					String inputLine;
					while (isAlive(process)) {

						
						if (debug) System.out.println("TW:Checking output");
						while ((inputLine = bufferedReader.readLine()) != null) {
							
							all_output = all_output + inputLine + "\n";
							parse(inputLine,wid);
						}

						if (System.currentTimeMillis() > finish) {

							process.destroy();

						}
						Thread.sleep(100);

					}
					while ((inputLine = bufferedReader.readLine()) != null) {
						
						all_output = all_output + inputLine + "\n";
						parse(inputLine,wid);
					}
					if (debug) System.out.println("TW:Executed:" + cmd);

				}

				catch (Exception err) {
					// err.printStackTrace();

				}

			}
			
				
	    	
	    	}
		 
		
		public RemoteViews getView(Context context, AppWidgetManager appWidgetManager,
				int appWidgetId, int code) {
			scontext = context;
			sappWidgetManager = appWidgetManager;
			runScript(appWidgetId,context,code);
			
			
			return remoteViews;
			
		}

		public void setAppIcon(String iconlabel, int icon_position) {
			int icon_id=R.id.icon1;
			
			if (icon_position==0)	
				{icon_id=R.id.icon1;}
			if (icon_position==1)	
				{icon_id=R.id.icon1;}
			if (icon_position==2)	
				{icon_id=R.id.icon2;}
			if (icon_position==3)	
				{icon_id=R.id.icon3;}
			if (icon_position==4)	
				{icon_id=R.id.icon4;}
			if (icon_position==5)	
				{icon_id=R.id.icon5;}
			if (icon_position==6)	
			{icon_id=R.id.icon6;}
		if (icon_position==7)	
			{icon_id=R.id.icon7;}
		if (icon_position==8)	
			{icon_id=R.id.icon8;}
		if (icon_position==9)	
			{icon_id=R.id.icon9;}
		if (icon_position==10)	
			{icon_id=R.id.icon10;}
		
		if (icon_position==11)	
		{icon_id=R.id.icon11;}
	if (icon_position==12)	
		{icon_id=R.id.icon12;}
		
			
			
		
			PackageManager pm = getPackageManager();
			try
			{
				if (debug) System.out.println("TW:Appicon:" + iconlabel+","+icon_position);
			ApplicationInfo appInfo= getPackageManager().getApplicationInfo(iconlabel, 0);
			Drawable icon = appInfo.loadIcon(pm);
			remoteViews.setImageViewBitmap(icon_id, ((BitmapDrawable)icon).getBitmap());
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
			
			
			
			
		}
		
		public void setIcon(String iconlabel, int icon_position) {
			int icon_id=R.id.icon1;
			int icon_drawable=R.drawable.ic_launcher;
			if (icon_position==0)	
				{icon_id=R.id.icon1;}
			if (icon_position==1)	
				{icon_id=R.id.icon1;}
			if (icon_position==2)	
				{icon_id=R.id.icon2;}
			if (icon_position==3)	
				{icon_id=R.id.icon3;}
			if (icon_position==4)	
				{icon_id=R.id.icon4;}
			if (icon_position==5)	
				{icon_id=R.id.icon5;}
		if (icon_position==6)	
			{icon_id=R.id.icon6;}
		if (icon_position==7)	
			{icon_id=R.id.icon7;}
		if (icon_position==8)	
			{icon_id=R.id.icon8;}
		if (icon_position==9)	
			{icon_id=R.id.icon9;}
		if (icon_position==10)	
			{icon_id=R.id.icon10;}
		
		if (icon_position==11)	
		{icon_id=R.id.icon11;}
	if (icon_position==12)	
		{icon_id=R.id.icon12;}
		
			
			
			if (iconlabel.equals("OK"))
				{
				icon_drawable=R.drawable.ic_icon_ok;
				}
			
			if (iconlabel.equals("CROSS"))
			{
			icon_drawable=R.drawable.ic_icon_cross;
			}
			
			if (iconlabel.equals("LEFT"))
			{
			icon_drawable=R.drawable.ic_icon_left;
			}
			
			if (iconlabel.equals("RIGHT"))
			{
			icon_drawable=R.drawable.ic_icon_right;
			}
			
			if (iconlabel.equals("BLANK"))
			{
			icon_drawable=R.drawable.ic_icon_blank;
			}
			
			if (iconlabel.equals("REFRESH"))
			{
			icon_drawable=R.drawable.ic_icon_refresh;
			}
			
			remoteViews.setImageViewResource(icon_id, icon_drawable);
			
		}

		public void updateText()
		{
			remoteViews.setTextViewText(R.id.main_text, text1);	
			
		}
		public void addText(String text)
		{
			text1=text1+"\n"+text;
			updateText();
			
		}
		public void setText(String text) {
			text1=text;
			updateText();
			
		}
		
		public void alignText(String alignment)
			{
			int gravity=Gravity.LEFT | Gravity.CENTER_VERTICAL;
			if (alignment.equals("LEFT"))
			{gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;}
			if (alignment.equals("RIGHT"))
			{gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;}
			if (alignment.equals("CENTER"))
			{gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;}
			
			remoteViews.setInt(R.id.main_text, "setGravity", gravity);
			}
		
		public void parse(String input, int id) {
			if (debug) System.out.println("TW:" + input);
			String[] parts = input.split(" ");
			if (parts.length >= 2) 
			{
				if (parts[0].equals("SETLAYOUT")) 
				{
					setlayout(parts[1],id);
				}
				if (parts[0].equals("SETICON"))
				{
					int icon_code=0;
					if (parts.length==3)
						{
						icon_code=Integer.parseInt(parts[2]);
						}
					setIcon(parts[1],icon_code);
					
				}
				
				if (parts[0].equals("SETAPPICON"))
				{
					int icon_code=0;
					if (parts.length==3)
						{
						icon_code=Integer.parseInt(parts[2]);
						}
					setAppIcon(parts[1],icon_code);
					
				}
				
				if (parts[0].equals("RUNAPP"))
				{
					runApp(parts[1]);
					
				}
				
				if (parts[0].equals("STOREVAR"))
				{
					if (parts.length>=3)
						{
						storeVar(id,parts[1],parts[2]);
						}
					
				}
				
				if (parts[0].equals("SETTEXT"))
				{
					
					setText(input.substring(8));
					
				}
				
				if (parts[0].equals("SETTEXTALIGNMENT"))
					{
					alignText(parts[0]);
					
					}
				if (parts[0].equals("ADDTEXT"))
				{
					
					addText(input.substring(8));
					
				}
				
				if (parts[0].equals("REFRESH"))
				{
					
					program_refresh(parts[1],id);
					
				}
				

			}
			if (parts.length == 1) 
			{
				if (parts[0].equals("UPDATE"))
				{
					
					updatewidget(id);
				}
			}
		}

		public void storeVar(int wid,String var, String value)
		{
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(scontext);
		String pref_name="var_"+wid+"_"+var;
		SharedPreferences.Editor editor = sp.edit();
    	editor.putString(pref_name,value);
    	editor.commit();
		
		}
		public void runApp(String app)
		{
			try
			{
				  if (debug)  System.out.println("TW:Running:"+app);

				Intent intent = getPackageManager().getLaunchIntentForPackage(app);
				scontext.startActivity(intent);
				
			}
			catch (Exception e)
			{}
			
		}
		
		public Boolean isCharging()
		
		{	
			Context ctxt = this;
			
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			  Intent batteryStatus = ctxt.registerReceiver(null, ifilter);
			  int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			  boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ;
			  
			return isCharging;
		}
		
		public float getBatteryPercent()
		
		{	
			Context ctxt = this;
			
			
			
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			  Intent batteryStatus = ctxt.registerReceiver(null, ifilter);
			  int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			 
			  int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			  int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

			  float batteryPct = 100*(level / (float)scale);
			  if (debug)  System.out.println("TW:Battery:"+batteryPct+"%");
			return batteryPct;
		}
		
		public void program_refresh (String refresh_time, int id)
		{
			
			int refreshm = Integer.parseInt(refresh_time)*1000;
			if (debug) System.out.println("TW:Refresh set to "+ refreshm +" miliseconds"); 
			final Calendar TIME = Calendar.getInstance();  
	        TIME.set(Calendar.MINUTE, 0);  
	        TIME.set(Calendar.SECOND, 0);  
	        TIME.set(Calendar.MILLISECOND, 0);  
	  
	        Intent i = new Intent(scontext, UpdateService.class);  
	        i.putExtra("widget", id);
	        i.putExtra("code", 0);
	        PendingIntent service = null;  
	         
	            service = PendingIntent.getService(scontext, 1000+id, i, PendingIntent.FLAG_CANCEL_CURRENT);  
	         
	             
	        m.set(AlarmManager.RTC,  System.currentTimeMillis()+refreshm, service);  
			
		}
		
		public void updatewidget(int id) {
			if (debug) System.out.println("TW:Updating widget");
			sappWidgetManager.updateAppWidget(id, remoteViews);

		}

		public void setlayout(String layouts, int widgetid) {
			int layout=R.layout.widget_icon_text;
			if (layouts.equals("ICON_TEXT"))
			{layout=R.layout.widget_icon_text;}
			
			if (layouts.equals("2ICON_TEXT"))
			{layout=R.layout.widget_2icon_text;}
			
			if (layouts.equals("TEXT"))
			{layout=R.layout.widget_text;}
			
			if (layouts.equals("ICON"))
			{layout=R.layout.widget_icon;}
			
			if (layouts.equals("2ICON"))
			{layout=R.layout.widget_2icon;}
			
			if (layouts.equals("3ICON"))
			{layout=R.layout.widget_3icon;}
			
			if (layouts.equals("4ICON"))
			{layout=R.layout.widget_4icon;}
			
			if (layouts.equals("5ICON"))
			{layout=R.layout.widget_5icon;}
			
			if (layouts.equals("10ICON"))
			{layout=R.layout.widget_10icon;}
			
			if (layouts.equals("12ICON"))
			{layout=R.layout.widget_12icon;}
			
			
			if (layouts.equals("ICON_TEXT_ICON"))
			{layout=R.layout.widget_icon_text_icon;}
			
            Intent myIntent_icon1=new Intent(scontext,UpdateService.class);
			Bundle extras=new Bundle();
            extras.putInt("code", UpdateService.CODE_ICON1);
            extras.putInt("widget", widgetid);
            myIntent_icon1.putExtras(extras);
            //This 1 has to change for each PI.
            PendingIntent i1PI=PendingIntent.getService(scontext, 1+(widgetid)*10400, myIntent_icon1, PendingIntent.FLAG_UPDATE_CURRENT);
        
            Intent myIntent_icon2=new Intent(scontext,UpdateService.class);
    			Bundle extras2=new Bundle();
                extras2.putInt("code", UpdateService.CODE_ICON2);
                extras2.putInt("widget", widgetid);
                myIntent_icon2.putExtras(extras2);
                //This 1 has to change for each PI.
                PendingIntent i2PI=PendingIntent.getService(scontext, 2+(widgetid)*10400, myIntent_icon2, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent myIntent_icon3=new Intent(scontext,UpdateService.class);
    			Bundle extras3=new Bundle();
                extras3.putInt("code", UpdateService.CODE_ICON3);
                extras3.putInt("widget", widgetid);
                myIntent_icon3.putExtras(extras3);
                //This 1 has to change for each PI.
                PendingIntent i3PI=PendingIntent.getService(scontext, 3+(widgetid)*10400, myIntent_icon3, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent myIntent_icon4=new Intent(scontext,UpdateService.class);
    			Bundle extras4=new Bundle();
                extras4.putInt("code", UpdateService.CODE_ICON4);
                extras4.putInt("widget", widgetid);
                myIntent_icon4.putExtras(extras4);
                //This 1 has to change for each PI.
                PendingIntent i4PI=PendingIntent.getService(scontext, 4+(widgetid)*10400, myIntent_icon4, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent myIntent_icon5=new Intent(scontext,UpdateService.class);
    			Bundle extras5=new Bundle();
                extras5.putInt("code", UpdateService.CODE_ICON5);
                extras5.putInt("widget", widgetid);
                myIntent_icon5.putExtras(extras5);
                //This 1 has to change for each PI.
                PendingIntent i5PI=PendingIntent.getService(scontext, 5+(widgetid)*10400, myIntent_icon5, PendingIntent.FLAG_UPDATE_CURRENT);

                
                Intent myIntent_icon6=new Intent(scontext,UpdateService.class);
    			Bundle extras6=new Bundle();
                extras6.putInt("code", UpdateService.CODE_ICON6);
                extras6.putInt("widget", widgetid);
                myIntent_icon6.putExtras(extras6);
                //This 1 has to change for each PI.
                PendingIntent i6PI=PendingIntent.getService(scontext, 6+(widgetid)*10400, myIntent_icon6, PendingIntent.FLAG_UPDATE_CURRENT);
            
                Intent myIntent_icon7=new Intent(scontext,UpdateService.class);
        			Bundle extras7=new Bundle();
                    extras7.putInt("code", UpdateService.CODE_ICON7);
                    extras7.putInt("widget", widgetid);
                    myIntent_icon7.putExtras(extras7);
                    //This 1 has to change for each PI.
                    PendingIntent i7PI=PendingIntent.getService(scontext, 7+(widgetid)*10400, myIntent_icon7, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent myIntent_icon8=new Intent(scontext,UpdateService.class);
        			Bundle extras8=new Bundle();
                    extras8.putInt("code", UpdateService.CODE_ICON8);
                    extras8.putInt("widget", widgetid);
                    myIntent_icon8.putExtras(extras8);
                    //This 1 has to change for each PI.
                    PendingIntent i8PI=PendingIntent.getService(scontext, 8+(widgetid)*10400, myIntent_icon8, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent myIntent_icon9=new Intent(scontext,UpdateService.class);
        			Bundle extras9=new Bundle();
                    extras9.putInt("code", UpdateService.CODE_ICON9);
                    extras9.putInt("widget", widgetid);
                    myIntent_icon9.putExtras(extras9);
                    //This 1 has to change for each PI.
                    PendingIntent i9PI=PendingIntent.getService(scontext, 9+(widgetid)*10400, myIntent_icon9, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent myIntent_icon10=new Intent(scontext,UpdateService.class);
        			Bundle extras10=new Bundle();
                    extras10.putInt("code", UpdateService.CODE_ICON10);
                    extras10.putInt("widget", widgetid);
                    myIntent_icon10.putExtras(extras10);
                    //This 1 has to change for each PI.
                    PendingIntent i10PI=PendingIntent.getService(scontext, 10+(widgetid)*10400, myIntent_icon10, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent myIntent_icon11=new Intent(scontext,UpdateService.class);
             			Bundle extras11=new Bundle();
                         extras11.putInt("code", UpdateService.CODE_ICON11);
                         extras11.putInt("widget", widgetid);
                         myIntent_icon11.putExtras(extras11);
                         //This 1 has to change for each PI.
                         PendingIntent i11PI=PendingIntent.getService(scontext, 11+(widgetid)*10400, myIntent_icon11, PendingIntent.FLAG_UPDATE_CURRENT);

                         Intent myIntent_icon12=new Intent(scontext,UpdateService.class);
             			Bundle extras12=new Bundle();
                         extras12.putInt("code", UpdateService.CODE_ICON12);
                         extras12.putInt("widget", widgetid);
                         myIntent_icon12.putExtras(extras12);
                         //This 1 has to change for each PI.
                         PendingIntent i12PI=PendingIntent.getService(scontext, 12+(widgetid)*10400, myIntent_icon12, PendingIntent.FLAG_UPDATE_CURRENT);

                
                
                Intent myIntent_text1=new Intent(scontext,UpdateService.class);
        			Bundle extrast1=new Bundle();
                    extrast1.putInt("code", UpdateService.CODE_TEXT1);
                    extrast1.putInt("widget", widgetid);
                    myIntent_text1.putExtras(extrast1);
                    //This 1 has to change for each PI.
                    PendingIntent t1PI=PendingIntent.getService(scontext, 1001+(widgetid)*10400, myIntent_text1, PendingIntent.FLAG_UPDATE_CURRENT);

                
			
			//Here the intents with the different codes and the widgetid should be set to run this service.
			
			remoteViews = new RemoteViews(scontext.getPackageName(), layout);
			remoteViews.setOnClickPendingIntent(R.id.icon1, i1PI);
			remoteViews.setOnClickPendingIntent(R.id.icon2, i2PI);
			remoteViews.setOnClickPendingIntent(R.id.icon3, i3PI);
			remoteViews.setOnClickPendingIntent(R.id.icon4, i4PI);
			remoteViews.setOnClickPendingIntent(R.id.icon5, i5PI);
			remoteViews.setOnClickPendingIntent(R.id.icon6, i6PI);
			remoteViews.setOnClickPendingIntent(R.id.icon7, i7PI);
			remoteViews.setOnClickPendingIntent(R.id.icon8, i8PI);
			remoteViews.setOnClickPendingIntent(R.id.icon9, i9PI);
			remoteViews.setOnClickPendingIntent(R.id.icon10, i10PI);
			remoteViews.setOnClickPendingIntent(R.id.icon11, i11PI);
			remoteViews.setOnClickPendingIntent(R.id.icon12, i12PI);
			
			remoteViews.setOnClickPendingIntent(R.id.main_text, t1PI);  
			
			//Set Dark bg.
			//remoteViews.setInt(R.id.LinearLayout01, "setBackgroundResource", R.drawable.background_dark);
			
		}

		public static boolean isAlive(Process p) {
			try {
				p.exitValue();
				return false;
			} catch (IllegalThreadStateException e) {
				return true;
			}
		}
		
}
