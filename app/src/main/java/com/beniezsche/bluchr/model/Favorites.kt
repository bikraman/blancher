package com.beniezsche.bluchr.model

import com.google.gson.Gson

object Favorites {

    var favoriteAppList = ArrayList<AppInfo>()

    fun saveListAsJson(list: ArrayList<AppInfo>){

        Gson().toJson(list)

    }
}