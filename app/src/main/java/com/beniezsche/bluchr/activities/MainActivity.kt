package com.beniezsche.bluchr.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.adapter.AppListAdapter
import com.beniezsche.bluchr.adapter.FavoriteAppListAdapter
import com.beniezsche.bluchr.model.AppInfo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), View.OnTouchListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this@MainActivity)

        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_favorite_apps.adapter = FavoriteAppListAdapter(this@MainActivity, getAllFavoriteApps())
        rv_favorite_apps.layoutManager = linearLayoutManager


        Timer().scheduleAtFixedRate(object : TimerTask(){
            override fun run() {

                runOnUiThread{
                    tv_time.text = getTime()
                }

            }

        },0,1000)


        tv_all_apps.setOnClickListener {
            val intent = Intent(this, AppListActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up)
        }





//        rv_apps.layoutManager = LinearLayoutManager(this)
//        rv_apps.adapter = AppListAdapter(this, getAllApps())
    }

    private fun getTime(): String {

        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d",hour,minute)
    }

    private fun getAllFavoriteApps(): ArrayList<AppInfo> {

        //val sharedPref = getSharedPreferences(getString(R.string.bluchr_shared_pref),Context.MODE_PRIVATE)

        //val favoriteAppList = sharedPref.getString(,null) as ArrayList<AppInfo>

        val start = 1

        return ArrayList(getAllApps().subList(start, start + 4))
    }

//    private fun showFavoriteApps(shouldShow: Boolean) {
//
//
//        if(shouldShow){
//            rv_favorite_apps.visibility = View.VISIBLE
//            tv_favorite_apps.visibility = View.VISIBLE
//        }
//        else {
//            rv_favorite_apps.visibility = View.GONE
//            tv_favorite_apps.visibility = View.GONE
//        }
//    }

    override fun onResume() {
        super.onResume()

        //@Suppress("DEPRECATION")
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //fullScreen()


    }

    private fun getAllApps(): ArrayList<AppInfo> {

        val pm: PackageManager = packageManager
        val appsList = ArrayList<AppInfo>()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        val allApps = pm.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app = AppInfo()
            app.label = ri.loadLabel(pm)
            app.packageName = ri.activityInfo.packageName
            app.icon = ri.activityInfo.loadIcon(pm)
            appsList.add(app)
        }

        return appsList
    }

    @Suppress("DEPRECATION")
    private fun fullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun showWallpaper() {

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}