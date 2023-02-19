package com.judot.alarma.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat
import com.judot.alarma.R
import com.judot.alarma.application.App.Companion.CHANNEL_ID
import com.judot.alarma.data.db.entities.TITLE
import com.judot.alarma.other.Constants
import com.judot.alarma.ui.MainActivity
import com.judot.alarma.ui.RingActivity

/** We use the service to create the alarm. Normally the scheduled alarm
 * will be lost on turn off or restarting the phone.  The service  it is the way
 * to call a function after a boot. So we save the alarm and if the phone
 * reboots  it schedules the alarm again */
class AlarmService: Service() {

    //TODO here the alarm on a start of a service. Move it to the receiver
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer!!.isLooping = true
//        val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//        val vibrator = vibratorManager.getDefaultVibrator();

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        //vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    /** On every entry in the service.
     * When the notification starts it will take the user to an activity where
     * he can select to either dismiss or snooze the alarm. */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            this.action = Constants.ACTION_SHOW_RING_FRAGMENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE))
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(alarmTitle)
            .setContentText("Ring Ring .. Ring Ring")
            .setSmallIcon(R.drawable.ic_alarm_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        mediaPlayer!!.start()
        //TODO Ima niakakav problem s vibraciata. Depreciated method
//        val pattern = longArrayOf(0, 100, 1000)
//        //vibrator?.vibrate(pattern, 0)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0));
//
//        } else {
//            // backward compatibility for Android API < 26
//            // noinspection deprecation
//            vibrator?.vibrate(pattern, 0);
//        }
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        vibrator!!.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}