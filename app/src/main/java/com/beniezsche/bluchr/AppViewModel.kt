package com.beniezsche.bluchr

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beniezsche.bluchr.model.AppInfo

class AppViewModel: ViewModel() {



    fun getAllAppList() : MutableLiveData<List<AppInfo>>{


        return MutableLiveData()

    }
}