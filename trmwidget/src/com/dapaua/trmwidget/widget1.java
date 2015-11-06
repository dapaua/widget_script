package com.dapaua.trmwidget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class widget1 extends AppWidgetProvider {

	int timeout = 15; // Timeout in seconds;
	public RemoteViews remoteViews;
	public Context scontext;
	public AppWidgetManager sappWidgetManager;
	public ComponentName myWidget;
	public String text1;
	 AlarmManager m;
	

	 
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		scontext = context;
		sappWidgetManager = appWidgetManager;
		
		m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//myWidget = new ComponentName(context, widget1.class);
		
		int wid;
		for (int i =0;i<appWidgetIds.length;i++)
			{
			System.out.println("TW:Widget id:"+appWidgetIds[i]);
			wid = appWidgetIds[i];
			updateWidget(context,appWidgetManager,wid);
			  
			}

}
	public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int id)
	{
		Intent intent = new Intent(context, UpdateService.class);  
        intent.putExtra("widget", id);
        context.startService(intent);
		
	}
}
