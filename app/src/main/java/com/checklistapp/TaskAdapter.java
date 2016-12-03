package com.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

/**
 * Created by user on 24-Nov-16.
 */
public class TaskAdapter extends BaseAdapter {

    private ArrayList<String> _list;
    private AppCompatActivity _activity;
    private LayoutInflater layoutInflater;
    ViewHolder holder = null;
    private SharedPreferences sharedpreferences;
    private static ArrayList<Boolean> isItemSeleced;
    // keeps track of if item was removed on current view call
    private boolean removed;

    public TaskAdapter(AppCompatActivity activity, ArrayList<String> list) {
        _list = list;
        _activity = activity;
        removed = false;
        isItemSeleced = new ArrayList<>();
        layoutInflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return _list.size();
    }

    @Override
    public Object getItem(int position) {
        return _list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.task_row, null);
            holder = new ViewHolder();

            holder.row_Layout = (RelativeLayout) convertView.findViewById(R.id.task_row_layout);
            holder._tasknameTextView = (TextView) convertView.findViewById(R.id.taskname_tv);
            holder._dateTextView = (TextView) convertView.findViewById(R.id.date_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.task_checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data;
                if (isItemSeleced.get(position)) {
                    holder.checkBox.setSelected(false);
                    String dataString = _list.get(position);
                    data = dataString.split("=");
                    data[2] = "false";

                } else {


                    holder.checkBox.setSelected(true);
                    String dataString = _list.get(position);
                    data = dataString.split("=");

                    data[2] = "true";
                }

                String finalString = data[0] + "=" + data[1] + "=" + data[2];

                _list.set(position, finalString);
//                _list.add(position, finalString);


                HashSet<String> hashSet = new HashSet<>();
                hashSet.addAll(_list);

                if (sharedpreferences == null)
                    sharedpreferences = _activity.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet(MainActivity.TASK_LIST, hashSet);
                editor.commit();
            }
        });


        String dataString = _list.get(position);
        String[] data = dataString.split("=");
        holder._tasknameTextView.setText(data[0]);
        holder._dateTextView.setText(data[1]);

        holder._tasknameTextView.setTextSize(MainActivity.fontSize);
        holder._dateTextView.setTextSize(MainActivity.fontSize);

        String status = data[2];

        if (status.equalsIgnoreCase("false")) {
            isItemSeleced.add(position, false);
            holder.checkBox.setChecked(false);
        } else {
            isItemSeleced.add(position, true);
            holder.checkBox.setChecked(true);
        }

        holder.row_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removes item before hitting this. throws error on get in some cases.
                if (removed) {
                    removed = false;
                } else {
                    String dataString = _list.get(position);
                    String[] data = dataString.split("=");

                    Intent intent = new Intent(_activity, SubTaskActivity.class);
                    intent.putExtra("sub", data[0]);
                    _activity.startActivity(intent);
                }
            }
        });

        holder.row_Layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isItemSeleced.remove(position);
                _list.remove(position);

                HashSet<String> hashSet = new HashSet<>();
                hashSet.addAll(_list);

                if (sharedpreferences == null)
                    sharedpreferences = _activity.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet(MainActivity.TASK_LIST, hashSet);
                editor.commit();

                // update data here instead of in main activity
                notifyDataSetChanged();
                Toast.makeText(_activity, "Item removed", Toast.LENGTH_SHORT).show();
                removed = true;
                return false;
            }
        });

        return convertView;

    }

    class ViewHolder {

        public RelativeLayout row_Layout;
        public TextView _tasknameTextView, _dateTextView;
        public CheckBox checkBox;
    }
}