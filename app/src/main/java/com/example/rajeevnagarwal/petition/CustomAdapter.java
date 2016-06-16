package com.example.rajeevnagarwal.petition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 6/11/2016.
 */
public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Row> objects;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
    public CustomAdapter(Context context, ArrayList<Row> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }
    public int getCount() {
        return objects.size();
    }

    public Row getItem(int position) {
        return objects.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.activity_fetch_petitions,
                    parent);
        }
        ViewHolder holder=null;
        holder = new ViewHolder();
        holder.textView1 = (TextView) itemView.findViewById(R.id.id);
        holder.textView2 = (TextView) itemView.findViewById(R.id.heading);
       /* ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.content_fetch_petitions, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.id);
            holder.textView2 = (TextView) convertView.findViewById(R.id.heading);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/
        holder.textView1.setText(objects.get(position).getId());
        holder.textView2.setText(objects.get(position).getHeading());
        return itemView;
    }
}
