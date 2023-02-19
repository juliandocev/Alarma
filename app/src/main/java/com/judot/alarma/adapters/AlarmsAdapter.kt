package com.judot.alarma.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judot.alarma.R
import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.databinding.AlarmItemBinding
import com.judot.alarma.ui.AlarmsViewModel
import com.judot.alarma.ui.MainActivity
import com.judot.alarma.ui.fragments.SetAlarmFragment


class AlarmsAdapter(
    private val viewModel: AlarmsViewModel,
    private val communicator: Communicator // Interface for passing data

): RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    inner class  AlarmViewHolder(binding: AlarmItemBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        val tvAlarmTime = binding.tvAlarmTime
        val switch = binding.switchAlarm
        val monday = binding.tvAlarmMonday
        val tuesday = binding.tvAlarmTuesday
        val wednesday = binding.tvAlarmWednesday
        val thursday = binding.tvAlarmThursday
        val friday = binding.tvAlarmFriday
        val saturday = binding.tvAlarmSaturday
        val sunday = binding.tvAlarmSunday

        val ivDelete = binding.ivDelete
        val llAlarmClick = binding.llAlarmClick
        init{
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            val alarms = differ.currentList

            val position = adapterPosition
            val alarm =  alarms[position]

            if (position != RecyclerView.NO_POSITION) {
                communicator.passDataCom(alarm)
            }
        }


    }

    private val differCallback = object : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }

    // tool that will take the two list and tell the differences
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {

        return AlarmViewHolder(
            AlarmItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
//    private var onItemClickListener: ((Alarm) -> Unit)? = null
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = differ.currentList[position]
        val hours = if(alarm.hours <10){
            "0${alarm.hours}"
        }else{
            alarm.hours.toString()
        }

        val minutes = if(alarm.minutes <10){
            "0${alarm.minutes}"
        }else{
            alarm.minutes.toString()
        }
        val alarmTime = "${hours}:${minutes}"

        holder.itemView.apply {

            // Hour
            holder.tvAlarmTime.text = alarmTime
            // Alarm On/Off
            holder.switch.isChecked = alarm.isOn

            holder.switch.setOnCheckedChangeListener { it, isChecked ->
                if(!isChecked){
                    alarm.isOn = false
                }
                if(isChecked){
                    alarm.isOn=true
                }
                viewModel.upsert(alarm)
            }

            // Days
            if(alarm.isMonday){
                holder.monday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.monday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isTuesday){
                holder.tuesday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.tuesday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isWednesday){
                holder.wednesday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.wednesday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isThursday){
                holder.thursday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.thursday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isFriday){
                holder.friday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.friday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isSaturday){
                holder.saturday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.saturday.setTextColor(Color.parseColor("#686565"))
            }
            if(alarm.isSunday){
                holder.sunday.setTextColor(Color.parseColor("#FF5722"))
            }
            else{
                holder.sunday.setTextColor(Color.parseColor("#686565"))
            }




            // Delete
            holder.ivDelete.setOnClickListener() {
                viewModel.delete(alarm)
                Snackbar.make(it, "Successfully deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.upsert(alarm)

                    }
                    show()
                }
            }

        }


    }


    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    // Interface for passing data
    interface Communicator{
        fun passDataCom(item: Alarm)

    }


}