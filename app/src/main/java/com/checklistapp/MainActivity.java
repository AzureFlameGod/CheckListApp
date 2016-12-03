package com.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class MainActivity extends AppCompatActivity {
    public static final String TASK_LIST = "tasklist";
    public static final String COLOR_CODE = "color" ;
    public static final String FONT_SIZE = "font";

    private ArrayList<String> itemList;
    private TaskAdapter taskListAdapter;
    private ListView lvItems;
    private Set<String> hashSet;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static final String MyPREFERENCES = "checkboxapp";
    private SharedPreferences sharedpreferences;

    public static int colorNameCode;
    public static int fontSize = 16;

    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainLayout = (RelativeLayout) findViewById(R.id.mail_layout);

        // ADD HERE
        lvItems = (ListView) findViewById(R.id.lvItems);


        itemList = new ArrayList<>();
        hashSet = new HashSet<>();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        colorNameCode = sharedpreferences.getInt(COLOR_CODE, Color.WHITE);
        fontSize = sharedpreferences.getInt(FONT_SIZE , 10);

        mainLayout.setBackgroundColor(colorNameCode);

        hashSet = sharedpreferences.getStringSet(TASK_LIST, null);

        if (hashSet != null)
            itemList.addAll(hashSet);

        taskListAdapter = new TaskAdapter(this, itemList);
        lvItems.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainLayout.setBackgroundColor(colorNameCode);

        if (taskListAdapter != null) {
            taskListAdapter.notifyDataSetChanged();
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
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putStringSet(TASK_LIST, hashSet);
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
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

