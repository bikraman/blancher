package com.beniezsche.bluchr.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beniezsche.bluchr.model.AppInfo

@Database(entities = [AppInfo::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationsDao
}