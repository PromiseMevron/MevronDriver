package com.mevron.rides.driver.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedAddress::class], version = 1)
abstract class MevronDatabase: RoomDatabase() {

    abstract fun addDAO(): MevronDao

    companion object {
        var INSTANCE: MevronDatabase? = null

        fun getAppDataBase(context: Context): MevronDatabase? {
            if (INSTANCE == null){
                synchronized(MevronDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MevronDatabase::class.java, "mevron_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }
}