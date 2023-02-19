package com.judot.alarma.data.db.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.judot.alarma.data.db.AlarmDao

@Database(
    entities = [Alarm::class],
    version = 1
)
abstract class AlarmsDatabase: RoomDatabase() {

    abstract fun getAlarmDao():AlarmDao

    companion object{

        @Volatile
        private var instance: AlarmsDatabase? = null
        private var Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock){
            instance?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmsDatabase::class.java,
                "AlarmsDB.db"
            )
                .fallbackToDestructiveMigration()// If you change the version and you don't have this it won't work
                .build()
    }


}