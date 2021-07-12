//package com.example.medicaldatabase;
//
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import org.jetbrains.annotations.Nullable;
//
////import android.support.annotation.Nullable;
//
//public class MyService extends IntentService {
//
//
//    public MyService() {
//        super("MyService");
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
////        Date dat  = new Date();//initializes to now
////        Calendar cal_alarm = Calendar.getInstance();
////        Calendar cal_now = Calendar.getInstance();
////        cal_now.setTime(dat);
////        cal_alarm.setTime(dat);
////        cal_alarm.set(Calendar.HOUR_OF_DAY,19);//set the alarm time
////        cal_alarm.set(Calendar.MINUTE,8 );
////        cal_alarm.set(Calendar.SECOND,0);
////        if(cal_alarm.before(cal_now)){//if its in the past increment
////            cal_alarm.add(Calendar.DATE,1);
////        }
////
////        Toast.makeText(this, ""+cal_alarm, Toast.LENGTH_SHORT).show();
////        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
////        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), 1000, pendingIntent);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 100, pendingIntent);
//    }
//}
//
//
