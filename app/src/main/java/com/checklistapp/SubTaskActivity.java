package com.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//import org.apache.commons.io.FileUtils;

public class SubTaskActivity extends AppCompatActivity {
   // public static final String SUB_TASK_LIST = "sub_tasklist";
    private ArrayList<String> itemList;
    private SubTaskAdapter subTaskAdapter;
    private ListView lvItems;
    private Set<String> hashSet;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private SharedPreferences sharedpreferences;
    private RelativeLayout mainLayout;
    private static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = getIntent().getStringExtra("sub");
        getSupportActionBar().setTitle(title);

        mainLayout = (RelativeLayout) findViewById(R.id.mail_layout);
        mainLayout.setBackgroundColor(MainActivity.colorNameCode);
        // ADD HERE
        lvItems = (ListView) findViewById(R.id.lvItems);


        itemList = new ArrayList<>();
        hashSet = new HashSet<>();

        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);


        hashSet = sharedpreferences.getStringSet(title, null);
        if (hashSet != null)
            itemList.addAll(hashSet);

        subTaskAdapter = new SubTaskAdapter(this, itemList, title);
        lvItems.setAdapter(subTaskAdapter);
        subTaskAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainLayout.setBackgroundColor(MainActivity.colorNameCode);

        if (subTaskAdapter != null) {
            subTaskAdapter.notifyDataSetChanged();
        }
    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString().trim();

        if (itemText.length() != 0 && itemText != null) {

            Date date = new Date();
            String dateString = dateFormat.format(date);
            String status = "false";

            String finalString = itemText + "=" + dateString + "=" + status;

            itemList.add(finalString);

            hashSet = new HashSet<>();
            hashSet.addAll(itemList);

            if (sharedpreferences == null)
                sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putStringSet(title, hashSet);
            editor.commit();


            etNewItem.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(SubTaskActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}