package com.checklistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user on 24-Nov-16.
 */
public class SubTaskAdapter extends BaseAdapter {

    private ArrayList<String> _list;
    private AppCompatActivity _activity;
    private LayoutInflater layoutInflater;
    ViewHolder holder = null;
    private String _title;
    private SharedPreferences sharedpreferences;
    private static ArrayList<Boolean> isItemSeleced;

    public SubTaskAdapter(AppCompatActivity activity, ArrayList<String> list, String title) {
        _list = list;
        _activity = activity;
        _title = title;

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

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

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

                _list.remove(position);
                _list.add(position, finalString);

                HashSet<String> hashSet = new HashSet<>();
                hashSet.addAll(_list);

                if (sharedpreferences == null)
                    sharedpreferences = _activity.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet(_title, hashSet);
                editor.commit();
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
                editor.putStringSet(_title, hashSet);
                editor.commit();

                notifyDataSetChanged();
                Toast.makeText(_activity, "Item removed", Toast.LENGTH_SHORT).show();

                return false;
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

        return convertView;
    }

    class ViewHolder {

        public RelativeLayout row_Layout;
        public TextView _tasknameTextView, _dateTextView;
        public CheckBox checkBox;
    }
}