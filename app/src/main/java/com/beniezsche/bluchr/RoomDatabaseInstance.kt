package com.beniezsche.bluchr

import android.content.Context
import androidx.room.Room
import com.beniezsche.bluchr.db.ApplicationDatabase

object RoomDatabaseInstance {

    private var INSTANCE: ApplicationDatabase? = null

    fun getInstance(applicationContext: Context) : ApplicationDatabase {

        return if(INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                applicationContext,
                ApplicationDatabase::class.java, "pii-launcher-db"
            ).build()

            INSTANCE!!
        } else
            INSTANCE!!


    }
}