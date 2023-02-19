package com.judot.alarma.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.judot.alarma.data.db.entities.Alarm


@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Alarm)

    @Delete
    suspend fun delete(item:Alarm)

    @Query("SELECT * FROM alarms_table")
    fun getAllAlarms(): LiveData<List<Alarm>>
}