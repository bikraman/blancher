package com.beniezsche.bluchr.activities

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import com.google.gson.Gson

open class BaseActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)

        if(getSharedPreferences(getString(R.string.bluchr_shared_pref), Context.MODE_PRIVATE).contains(getString(R.string.string_favorite_apps))){
            Favorites.favoriteAppList.addAll(Gson().fromJson<ArrayList<AppInfo>>(getSharedPreferences(getString(R.string.bluchr_shared_pref), Context.MODE_PRIVATE).getString(getString(R.string.string_favorite_apps),null), AppInfo::class.java))
        }

    }

    fun saveListAsJson(list: ArrayList<AppInfo>){

        val listAsJson = Gson().toJson(list)
        val editor = getSharedPreferences(getString(R.string.bluchr_shared_pref),Context.MODE_PRIVATE).edit()
        editor.putString(getString(R.string.string_favorite_apps),listAsJson)
        editor.commit()

    }



}