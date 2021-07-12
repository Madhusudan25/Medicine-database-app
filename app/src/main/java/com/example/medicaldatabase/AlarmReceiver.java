package com.example.medicaldatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmReceiver extends BroadcastReceiver{

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        Date current=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        int current_year=cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH)+1;
        int current_date= cal.get(Calendar.DATE);

        String DBDate=current_date+"/"+current_month+"/"+current_year;

        DatabaseHelper dbHandler =new DatabaseHelper(context);
        Log.d("TAG", "onReceive: "+DBDate);

        Cursor cursor = dbHandler.getRecords(DBDate);

        if(cursor.getCount() == 0){
            Toast.makeText(context, "DBDATE=" + DBDate+"\nNo data found", Toast.LENGTH_SHORT).show();
            return;
        }

        while(cursor.moveToNext()){
            setAlarm(Integer.parseInt(cursor.getString(4)),cursor.getString(1),context);
        }
    }

    private void setAlarm(int hour,String MedicineName,Context context) {
        Calendar cal;
        cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());

        Toast.makeText(context, "ALARM is set", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,"Time to take medicine "+ MedicineName);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, 0);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(i);
    }

}