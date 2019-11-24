package com.example.planb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class destination_search extends AppCompatActivity {
    EditText search;
    TextView setdate;
    Button setsearch;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener DatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        setdate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.search_destination);

        search = findViewById(R.id.search);
        setdate = findViewById(R.id.setdate);
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(destination_search.this, DatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        setsearch = findViewById(R.id.setsearch);
        setsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), guide_list.class);
                intent.putExtra("REGION", search.getText().toString());
                intent.putExtra("SET_DATE", setdate.getText().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch(item.getItemId()){
            case R.id.registerdestination :
                intent = new Intent(destination_search.this, destination_regist.class);
                startActivity(intent);
                break;
            case R.id.myinfo :
                intent = new Intent(destination_search.this, UpdateUser.class);
                startActivity(intent);
                break;
//            case R.id.logout :
//                Intent intent = new Intent(destination_search.this, logout.class);
//                startActivity(intent);
//                break;
//            case R.id.myregister:
//                Intent intent = new Intent(destination_search.this, myregits.class);
//                startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
