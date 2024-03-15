package com.beniezsche.bluchr.model

import android.content.Context
import com.beniezsche.bluchr.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Favorites {

    val favoriteAppList = ArrayList<AppInfo>()

    fun getFavoriteAppsList(context: Context) : List<AppInfo> {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.bluchr_shared_pref), Context.MODE_PRIVATE)

        val json = sharedPref.getString(context.getString(R.string.favorite_app_list), "")
        val type = object : TypeToken<List<AppInfo>>() {}.type

        favoriteAppList.addAll(Gson().fromJson(json, type) ?: emptyList())

        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun addToList(app: AppInfo, context: Context) {

        val appInfoList = getFavoriteAppsList(context).toMutableList()
        appInfoList.add(app)
        favoriteAppList.add(app)
        saveAppInfoList(appInfoList, context)
    }

    fun saveAppInfoList(appInfoList: List<AppInfo>, context: Context) {
        val json = Gson().toJson(appInfoList)
        val sharedPref = context.getSharedPreferences(context.getString(R.string.bluchr_shared_pref), Context.MODE_PRIVATE)
        sharedPref.edit().putString(context.getString(R.string.favorite_app_list), json).apply()
    }

    fun removeFromList(position: Int, context: Context) {
        val list = getFavoriteAppsList(context).toMutableList()

        list.removeAt(position)

        saveAppInfoList(list, context)
    }
}