package com.judot.alarma.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.judot.alarma.data.repository.AlarmsRepository

class AlarmsViewModelProviderFactory(

    val app: Application,
    val alarmsRepository: AlarmsRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  AlarmsViewModel(app, alarmsRepository) as T
    }
}