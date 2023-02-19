package com.judot.alarma.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import butterknife.ButterKnife
import com.judot.alarma.R
import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.services.AlarmService
import java.util.*

/** The activity will only be shown when the user
 * selects the push notification shown when an alarm is active. */
class RingActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring)
        var dismiss: Button = findViewById(R.id.activity_ring_dismiss)
        var snooze: Button = findViewById(R.id.activity_ring_snooze)

        ButterKnife.bind(this)

        dismiss.setOnClickListener {
            val intentService = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intentService)
            finish()
        }

//        snooze.setOnClickListener {
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.timeInMillis = System.currentTimeMillis()
//            calendar.add(Calendar.MINUTE, 10)
//            val alarm = Alarm(
//                alarmId =null,
//                hour =calendar.get(Calendar.HOUR_OF_DAY),
//                minute =calendar.get(Calendar.MINUTE),
//                started =true,
//                recurring =false,
//                monday =false,
//                tuesday =false,
//                wednesday =false,
//                thursday =false,
//                friday =false,
//                saturday =false,
//                sunday = false,
//                title ="Snooze",
//                created = System.currentTimeMillis()
//            )
//            alarm.schedule(applicationContext)
//            val intentService = Intent(applicationContext, AlarmService::class.java)
//            applicationContext.stopService(intentService)
//            finish()
//        }

        animateClock()

    }

    private fun animateClock() {
        var clock: ImageView = findViewById(R.id.activity_ring_clock)
        val rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f)
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        rotateAnimation.duration = 800
        rotateAnimation.start()
    }
}