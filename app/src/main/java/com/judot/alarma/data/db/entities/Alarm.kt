package com.judot.alarma.data.db.entities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.judot.alarma.receiver.AlarmReceiver
import java.io.Serializable
import java.util.*

val MONDAY = "MONDAY"
val TUESDAY = "TUESDAY"
val WEDNESDAY = "WEDNESDAY"
val THURSDAY = "THURSDAY"
val FRIDAY = "FRIDAY"
val SATURDAY = "SATURDAY"
val SUNDAY = "SUNDAY"
val RECURRING = "RECURRING"
val TITLE = "TITLE"


@Entity(tableName = "alarms_table")
 data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "ringtone")
    var ringtone: String? = null,
    @ColumnInfo(name = "hours")
    var hours: Int = -1,
    @ColumnInfo(name = "minutes")
    var minutes: Int = -1,
    @ColumnInfo(name = "isVibrate")
    var isVibrate: Boolean = false,
    @ColumnInfo(name = "isOn")
    var isOn: Boolean = false,
    @ColumnInfo(name = "monday")
    var isMonday: Boolean = false,
    @ColumnInfo(name = "tuesday")
    var isTuesday: Boolean = false,
    @ColumnInfo(name = "wednesday")
    var isWednesday: Boolean = false,
    @ColumnInfo(name = "thursday")
    var isThursday: Boolean = false,
    @ColumnInfo(name = "friday")
    var isFriday: Boolean = false,
    @ColumnInfo(name = "saturday")
    var isSaturday: Boolean = false,
    @ColumnInfo(name = "sunday")
    var isSunday: Boolean = false

    ): Serializable {

    // TODO zasto intenta, koito slagame v AlarmManager e za receiver a ne za service????
    /** Schedule the alarm */
    fun schedule(context: Context?, alarm: Alarm) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hours)
        calendar.set(Calendar.MINUTE, alarm.minutes)
        calendar.set(Calendar.SECOND, 0)
        /** we will be using the AlarmManager with a PendingIntent to trigger a BroadcastReceiver
         * The AlarmManager uses the alarming system from the Android operating system to trigger
         * a broadcast to be sent once an alarm is active. The AlarmManager allows you to schedule
         * both once off alarms and repeating alarms. When an alarm is no longer required the
         * AlarmManager can also be used to cancel an alarm. */
        //TODO 1 mai i dvata varianta stavat. Purvo si misleh che ima zabaviane ot tuk, no mai e ot ako sloza putExtra v apply pri intenta
        //val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmManager = context?.getSystemService(AlarmManager::class.java)
        val intent = Intent(context, AlarmReceiver::class.java)
        //TODO 3 ako sloza applay blovl i vutre sloza putExtra, ne se vkluchva alarmata koga triabva a i niama zvuk. Zasto li?
        //intent.putExtra(RECURRING, recurring)
        intent.putExtra(MONDAY, alarm.isMonday)
        intent.putExtra(TUESDAY, alarm.isFriday)
        intent.putExtra(WEDNESDAY, alarm.isWednesday)
        intent.putExtra(THURSDAY, alarm.isThursday)
        intent.putExtra(FRIDAY, alarm.isFriday)
        intent.putExtra(SATURDAY, alarm.isSaturday)
        intent.putExtra(SUNDAY, alarm.isSunday)

        /** We use a PendingIntent with the AlarmManager to send an action to perform
         * when the alarm goes off which will be handled by our BroadcastReceiver.
         * we will send details about our alarm inside the PendingIntent using Intent extras
         * such as if the alarm is recurring or not and if so what days of the week the alarm
         * should sound. This information will be used in the BroadcastReceiver to only create
         * the alarm service for recurring alarms if the current day of the week falls on one
         * of the days permitted by the alarm */
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarm.id?.let {
                // TODO za po goliama preciznost mozebi da slozish it.hasCode()
                // TODO viz dali ne e po dobre taka PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                PendingIntent.getBroadcast(
                    context, it, intent,
                    PendingIntent.FLAG_MUTABLE
                )
            }
        } else {
            alarm.id?.let {
                PendingIntent.getBroadcast(
                    context,
                    it,
                    intent,
                    0
                )
            }
        }
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,pendingIntent
//        )
//        alarmManager?.setExact(
//            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//            pendingIntent
//        )


        //TODO 2 tova mai e po dobro za alarmi
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            pendingIntent
        )


    }

    //TODO
    fun cancelAlarm(context: Context, alarm:Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = alarm.id?.let {
            PendingIntent.getBroadcast(
                context,
                it, intent, 0
            )
        }
        alarmManager.cancel(alarmPendingIntent)
        alarm.isOn = false
        val toastText =
            String.format("Alarm cancelled for %02d:%02d with id %d", alarm.hours, alarm.minutes, alarm.id)
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }

}


