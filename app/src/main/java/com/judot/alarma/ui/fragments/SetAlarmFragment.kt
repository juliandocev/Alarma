package com.judot.alarma.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.databinding.FragmentSetAlarmBinding
import com.judot.alarma.receiver.AlarmReceiver
import com.judot.alarma.ui.AlarmsViewModel
import com.judot.alarma.ui.MainActivity
import java.util.*


class SetAlarmFragment(var isNewEntry:Boolean = true) : Fragment() {
    private var binding: FragmentSetAlarmBinding? = null
    lateinit var viewModel: AlarmsViewModel
    private lateinit var alarmManager: AlarmManager
    //private var sharedPref: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentSetAlarmBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).viewModel
        //sharedPref = context?.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        // Close btn
        binding?.ivClose?.setOnClickListener() {
            activity?.onBackPressed()

        }
        // Time
        val timePicker = binding?.timePicker
        timePicker?.setIs24HourView(true)
        var tpHour = 0
        var tpMinute = 0
        // Vibration
        var vibrate = false
        val switchVibration: SwitchCompat? = binding?.switchVibrate
        // Days
        val monday = binding?.tvMonday
        var isMonday = false
        val tuesday = binding?.tvTuesday
        var isTuesday = false
        val wednesday = binding?.tvWednesday
        var isWednesday = false
        val thursday = binding?.tvThursday
        var isThursday = false
        val friday = binding?.tvFriday
        var isFriday = false
        val saturday = binding?.tvSaturday
        var isSaturday = false
        val sunday = binding?.tvSunday
        var isSunday = false

        // TODO tova go slozi vuv values
        val colorOrange = "#FF5722"
        val colorGrey = "#686565"

        var idAlarm :Int? = null

        if(isNewEntry){
            // Set Time
            val calendar: Calendar = Calendar.getInstance()
            tpHour = calendar.get(Calendar.HOUR)
            tpMinute = calendar.get(Calendar.MINUTE)
            timePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
                tpHour = hourOfDay
                tpMinute = minute
            }
            // Set Vibration
            switchVibration?.setOnCheckedChangeListener { buttonView, isChecked ->
                vibrate = isChecked
            }
            // Set unique Id
            idAlarm = Random().nextInt(Int.MAX_VALUE)
        }else{
            val alarm = arguments?.getSerializable("alarm") as Alarm
            idAlarm = alarm.id

            // Set Time
            tpHour = alarm.hours
            tpMinute = alarm.minutes
            timePicker?.hour = tpHour
            timePicker?.minute = tpMinute
            timePicker?.setOnTimeChangedListener { view, hourOfDay, minute ->
                tpHour = hourOfDay
                tpMinute = minute
            }
            // Set Vibration
            vibrate = alarm.isVibrate
            switchVibration?.isChecked = alarm.isVibrate
            switchVibration?.setOnCheckedChangeListener { buttonView, isChecked ->
                vibrate = isChecked
            }

            // Set Days
            isMonday = alarm.isMonday
            if(!isMonday){
                monday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                monday?.setTextColor(Color.parseColor(colorOrange))

            }
            isTuesday = alarm.isTuesday
            if(!isTuesday){
                tuesday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                tuesday?.setTextColor(Color.parseColor(colorOrange))
            }
            isWednesday = alarm.isWednesday
            if(!isWednesday){
                wednesday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                wednesday?.setTextColor(Color.parseColor(colorOrange))
            }
            isThursday = alarm.isThursday
            if(!isThursday){
                thursday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                thursday?.setTextColor(Color.parseColor(colorOrange))
            }
            isFriday = alarm.isFriday
            if(!isFriday){
                friday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                friday?.setTextColor(Color.parseColor(colorOrange))
            }
            isSaturday = alarm.isSaturday
            if(!isSaturday){
                saturday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                saturday?.setTextColor(Color.parseColor(colorOrange))
            }
            isSunday = alarm.isSunday
            if(!isSunday){
                sunday?.setTextColor(Color.parseColor(colorGrey))
            } else{
                sunday?.setTextColor(Color.parseColor(colorOrange))
            }

        }

        // SET THE DAYS

        //MONDAY
        monday?.setOnClickListener(){
            isMonday = if(!isMonday){
                monday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                monday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //TUESDAY
        tuesday?.setOnClickListener(){
            isTuesday = if(!isTuesday){
                tuesday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                tuesday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //WEDNESDAY
        wednesday?.setOnClickListener(){
            isWednesday = if(!isWednesday){
                wednesday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                wednesday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //THURSDAY
        thursday?.setOnClickListener(){
            isThursday = if(!isThursday){
                thursday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                thursday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //FRIDAY
        friday?.setOnClickListener(){
            isFriday = if(!isFriday){
                friday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                friday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //SATURDAY
        saturday?.setOnClickListener(){
            isSaturday = if(!isSaturday){
                saturday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                saturday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }
        //SUNDAY
        sunday?.setOnClickListener(){
            isSunday = if(!isSunday){
                sunday.setTextColor(Color.parseColor(colorOrange))
                true
            } else{
                sunday.setTextColor(Color.parseColor(colorGrey))
                false
            }
        }

        // Check btn
        /** We take all the information from the view and we create the alarm. Than we save it and we schedule it*/
        binding?.ivCheck?.setOnClickListener(){

            val alarm = Alarm(
                id = idAlarm,
                hours = tpHour,
                minutes = tpMinute,
                isVibrate = vibrate,
                isOn = true,
                isMonday = isMonday,
                isTuesday = isTuesday,
                isWednesday = isWednesday,
                isThursday = isThursday,
                isFriday = isFriday,
                isSaturday = isSaturday,
                isSunday = isSunday
                //TODO ringtones and else






            )

            // save the alarm
            viewModel.upsert(alarm)
            // schedule the alarm
            alarm.schedule(context,alarm)

            // go back as back btn had been pressed
            activity?.onBackPressed()

        }

        return binding?.root
    }

//    private fun saveTime(hour: Int,minute: Int){
//        val editor = sharedPref?.edit()
//        editor?.putInt("hour", hour)
//        editor?.putInt("minute", minute)
//        editor?.commit()
//
//
//    }





//    private fun setAlarm(context: Context?, hour:Int, minute:Int, idAlarm:Int){
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY,hour)
//        calendar.set(Calendar.MINUTE,minute)
//        calendar.set(Calendar.SECOND,0)
//
//        val alarmManager =context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, AlarmReceiver::class.java)
//        // Unique id for every alarm set. Used to make difference in the broadcast receiver
//        val uniqueIntentId = idAlarm
//        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getBroadcast(context , uniqueIntentId, intent.putExtra("alarmId",uniqueIntentId),PendingIntent.FLAG_MUTABLE)
//        } else {
//            PendingIntent.getBroadcast(context, uniqueIntentId, intent.putExtra("alarmId",uniqueIntentId), 0)
//        }
////        alarmManager.setRepeating(
////            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
////            AlarmManager.INTERVAL_DAY,pendingIntent
////        )
//        alarmManager.setExact(
//            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
//            pendingIntent
//        )
//
//
//    }




    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}