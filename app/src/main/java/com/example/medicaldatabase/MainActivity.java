package com.example.medicaldatabase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private Button insert,viewall;
    private EditText name,date_picker;
    private Spinner time;
    private DatePickerDialog datePickerDialog;
    private DatabaseHelper dbHandler;
    private static int userYear,userMonth,userDate;
    private int hour;
    private int current_date,current_month,current_year;
    private String m_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        viewall=findViewById(R.id.viewall);
        name=findViewById(R.id.name);
        date_picker=findViewById(R.id.date);
        insert=findViewById(R.id.button);
        time = findViewById(R.id.time);

        String[] items = new String[]{"Morning", "Afternoon", "Evening","Night"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        time.setAdapter(adapter);

        Dexter.withContext(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Log.d("Permission","Granted");
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    finish();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            })
            .check();

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        userYear=year;
                        userMonth= monthOfYear;
                        userDate=dayOfMonth;
                        date_picker.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        dbHandler = new DatabaseHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                m_name=name.getText().toString();
                String date=date_picker.getText().toString();
                String Time=time.getSelectedItem().toString();

                if (m_name.isEmpty() || date.isEmpty() || Time.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(time.getSelectedItemPosition()==0){
                    hour=9; //9AM
                }
                else if(time.getSelectedItemPosition()==1){
                    hour=13; //1PM
                }
                else if(time.getSelectedItemPosition()==2){
                    hour=18; //6PM
                }
                else{
                    hour=21; //9PM
                }
                boolean res=dbHandler.addNewMedicine(m_name, date, Time, hour);
                if(res){
                    Toast.makeText(MainActivity.this, "Medicine has been added.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Medicine has not been added.", Toast.LENGTH_SHORT).show();
                }

                Date current=new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(current);
                current_year=cal.get(Calendar.YEAR);
                current_month = cal.get(Calendar.MONTH);
                current_date= cal.get(Calendar.DATE);

                if(current_year==userYear&&current_month==userMonth&&current_date==userDate){
                    if(cal.get(Calendar.HOUR_OF_DAY)>=hour){
                        Toast.makeText(MainActivity.this, "Alarm cannot be set", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();
                        setAlarm(hour);
                    }
                }else if(current_year>=userYear&&current_month>=userMonth&&current_date>userDate){
                    startAlarm();
                }

                name.setText("");
                date_picker.setText("");
                time.setSelection(0);
            }
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHandler.retrieve();
                if(cursor.getCount() == 0){
                    showMessage("Error","Data Not Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(cursor.moveToNext()){
                    buffer.append("Id : "+ cursor.getString(0)+"\n");
                    buffer.append("Medicine Name : "+ cursor.getString(1)+"\n");
                    buffer.append("Date : "+ cursor.getString(2)+"\n");
                    buffer.append("Time : "+ cursor.getString(3)+"\n");
                    buffer.append("Hour : "+ cursor.getString(4)+"\n\n");
                }

                //show message
                showMessage("Data",buffer.toString());
            }
        });
    }

    public void showMessage(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm() {
        int DATA_FETCHER_RC = 123;
        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, DATA_FETCHER_RC,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        mAlarmManager.setInexactRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setAlarm(int hour) {
        Calendar cal;
        cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());

        Toast.makeText(this, "ALARM is  set", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,m_name);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, 0);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
    }
}
