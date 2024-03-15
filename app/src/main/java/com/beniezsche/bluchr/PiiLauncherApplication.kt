package com.beniezsche.bluchr

import android.app.Application

class PiiLauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        RoomDatabaseInstance.getInstance(this.applicationContext)
    }
}