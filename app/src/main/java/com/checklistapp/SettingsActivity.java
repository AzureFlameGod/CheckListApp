package com.checklistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * Created by user on 25-Nov-16.
 */
public class SettingsActivity extends AppCompatActivity {

    private String[] colorNames = {"Red", "Yellow", "Bue","White", "Green", "Cyan", "Magneta"};
    private int[] colorNamesId = {Color.RED, Color.YELLOW, Color.BLUE,Color.WHITE , Color.GREEN, Color.CYAN, Color.MAGENTA};
    private String[] fontSizes = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33"};
    private int[] colors = new int[]{Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA};
    private Spinner color_spinner, font_spinner;
    private RelativeLayout mainLayout;
    private Button button;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        /*mainLayout = (RelativeLayout) findViewById(R.id.mail_layout);
        mainLayout.setBackgroundColor(MainActivity.colorNameCode);
        */
        button = (Button) findViewById(R.id.color_btn);
        button.setBackgroundColor(MainActivity.colorNameCode);

        color_spinner = (Spinner) findViewById(R.id.spinner);
        color_spinner.setSelection(MainActivity.colorNameCode);
        color_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.colorNameCode = colorNamesId[position];
                button.setBackgroundColor(MainActivity.colorNameCode);

                if (sharedpreferences == null)
                    sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(MainActivity.COLOR_CODE , MainActivity.colorNameCode);
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, colorNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        color_spinner.setAdapter(aa);

        font_spinner = (Spinner) findViewById(R.id.fontspinner);
        font_spinner.setSelection(MainActivity.fontSize-10);
        font_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.fontSize = Integer.valueOf(fontSizes[position]);
                button.setBackgroundColor(MainActivity.colorNameCode);

                if (sharedpreferences == null)
                    sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(MainActivity.FONT_SIZE , MainActivity.fontSize);
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter font_aa = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, fontSizes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        font_spinner.setAdapter(font_aa);
    }
}
