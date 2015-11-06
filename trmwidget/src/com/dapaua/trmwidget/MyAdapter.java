package com.dapaua.trmwidget;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
 
    private List<TwoLines> list;
 
    public MyAdapter(Context context, List<TwoLines> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        TextView sub = (TextView) convertView.findViewById(android.R.id.text2);
 
        TwoLines info = list.get(position);
        title.setText(info.line1);
        sub.setText(info.line2);
        return convertView;
    }
 
    public int getCount() {
        return list.size();
    }
 
    public Object getItem(int arg0) {
        return list.get(arg0);
    }
 
    public long getItemId(int arg0) {
        return arg0;
    }
}
