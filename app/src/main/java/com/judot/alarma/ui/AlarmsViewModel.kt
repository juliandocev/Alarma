package com.judot.alarma.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.data.repository.AlarmsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmsViewModel(private val app: Application, private val alarmsRepository: AlarmsRepository): ViewModel() {

    //TODO(Viz otnovo za room. Zasto ima DAO, RoomDatabase, repository i view model
    // i kak se sluchva vsichko tam. Zasto i kak e companion objecta v roomatabase
    // viz i za recycle view i viewModel

    fun upsert(item: Alarm) = GlobalScope.launch {
        alarmsRepository.upsert(item)
    }

    fun delete(item: Alarm)= GlobalScope.launch {
        alarmsRepository.delete(item)
    }

    fun getAllAlarms()= alarmsRepository.getAllAlarms()
}