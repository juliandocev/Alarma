package com.judot.alarma.data.repository

import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.data.db.entities.AlarmsDatabase

class AlarmsRepository( val db:AlarmsDatabase) {

    suspend fun upsert(item: Alarm) = db.getAlarmDao().upsert(item)

    suspend fun delete(item: Alarm) = db.getAlarmDao().delete(item)

    fun getAllAlarms()=db.getAlarmDao().getAllAlarms()
}