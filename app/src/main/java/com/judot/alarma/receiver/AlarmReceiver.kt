package com.judot.alarma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.judot.alarma.services.AlarmService
import com.judot.alarma.services.RescheduleAlarmsService
import java.util.*


/**
 * The BroadcastReceiver will be used to trigger the alarm to start after
 * the alarm manager generates a broadcast once the system time hits
 * the scheduled alarm time.
 * The BroadcastReceiver looks or for a scheduled alarm, created by AlarmManager in a service,
 * or for a boot. If it is a boot, it reschedules tha alarm */
class AlarmReceiver: BroadcastReceiver() {
    val MONDAY = "MONDAY"
    val TUESDAY = "TUESDAY"
    val WEDNESDAY = "WEDNESDAY"
    val THURSDAY = "THURSDAY"
    val FRIDAY = "FRIDAY"
    val SATURDAY = "SATURDAY"
    val SUNDAY = "SUNDAY"

    /** In the onReceive method of our broadcast receiver we check that the broadcast received is not the broadcast
     * that is sent when the device is booted. If that is not the case then the broadcast relates to an alarm
     * and we then retrieve extras from the Intent to check is the alarm is recurring on not. */
    override fun onReceive(context: Context?, intent: Intent?) {

        // if it is a boot intent it reschedules the alarm
        if (Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            val toastText = String.format("Alarm Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            if (context != null) {
                startRescheduleAlarmsService(context)
            }
        } else {
            // TODO Make a reoccurring alarm
            // if it is other kind of intent it starts the alarm service
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            if(alarmIsToday(intent)){
                if (context != null) {
                    startAlarmService(context, intent)
                }
            }


        }

//        var alarmIdIntent1: Int? = intent?.getIntExtra("alarmId",0)
//
//
//        val i  = Intent(context, Ringing::class.java)
//        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getBroadcast(context , 0, intent,PendingIntent.FLAG_MUTABLE)
//        } else {
//            PendingIntent.getBroadcast(context, 0, intent, 0)
//        }
//

//        val builder = NotificationCompat.Builder(context!!, "foxandroid")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Foxandroid Alarm Manager")
//            .setContentText("Subscribe for mote android related content")
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(123,builder.build())
    }


    /** When the intent comes we check if it is today nad if it is we start the alarm service */
    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val today: Int = calendar.get(Calendar.DAY_OF_WEEK)
        when (today) {
            Calendar.MONDAY -> {
                return if (intent.getBooleanExtra(MONDAY, false)) true else false
            }
            Calendar.TUESDAY -> {
                return if (intent.getBooleanExtra(TUESDAY, false)) true else false
            }
            Calendar.WEDNESDAY -> {
                return if (intent.getBooleanExtra(WEDNESDAY, false)) true else false
            }
            Calendar.THURSDAY -> {
                return if (intent.getBooleanExtra(THURSDAY, false)) true else false
            }
            Calendar.FRIDAY -> {
                return if (intent.getBooleanExtra(FRIDAY, false)) true else false
            }
            Calendar.SATURDAY -> {
                return if (intent.getBooleanExtra(SATURDAY, false)) true else false
            }
            Calendar.SUNDAY -> {
                return if (intent.getBooleanExtra(SUNDAY, false)) true else false
            }
        }
        return false
    }

    /** Start the service in a background*/
    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("TITLE", intent.getStringExtra("TITLE"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    /** It reschedules the alarm in a background depending o the android version.
     * It reshedules it because after a boot, the AlarmManager looses the
     * information of it*/
    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}