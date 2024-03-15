package com.beniezsche.bluchr.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beniezsche.bluchr.RoomDatabaseInstance
import com.beniezsche.bluchr.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class AppViewModel(context: Context): ViewModel() {

    private val db = RoomDatabaseInstance.getInstance(context)
    fun getAllAppList() : Flow<List<AppInfo>> {

        val data = flow {

            while (true) {
                emit(db.applicationDao().getAll())
                delay(1000)
            }
        }

        return data

    }


    fun hideApp(packageName: String) {

        CoroutineScope(Dispatchers.IO).launch {
            db.applicationDao().hideApp(packageName)

        }
    }
    fun addAppInfoToDB(list: List<AppInfo>) {

        CoroutineScope(Dispatchers.IO).launch {
            if(db.applicationDao().getAll().isEmpty())
                db.applicationDao().insertAll(list)
        }
    }
}