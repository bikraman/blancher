package com.beniezsche.bluchr.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.beniezsche.bluchr.ComposeMainActivity
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.adapter.FavoriteAppListAdapter
import com.beniezsche.bluchr.fragments.AllAppsFragment
import com.beniezsche.bluchr.fragments.HomeFragment
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity(), View.OnTouchListener {


    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        viewPager.adapter = ScreenAdapter(supportFragmentManager)

        val linearLayoutManager = LinearLayoutManager(this@HomeActivity)

        val tvAllApps = findViewById<ImageView>(R.id.tv_all_apps)

        linearLayoutManager.orientation = RecyclerView.VERTICAL


        tvAllApps.setOnClickListener {
            val intent = Intent(this, AppListActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up)
        }

        startActivity(Intent(this, ComposeMainActivity::class.java))

    }

    inner class ScreenAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> AllAppsFragment()
                else -> HomeFragment()
            }
        }

    }


    private fun getTime(): String {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d",hour,minute)
    }

    private fun getAllFavoriteApps(): ArrayList<AppInfo> {
        return ArrayList(Favorites.getFavoriteAppsList(this))
    }

    override fun onResume() {
        super.onResume()

        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        fullScreen()

//        rvFavoriteApps.adapter = FavoriteAppListAdapter(this@HomeActivity, getAllFavoriteApps())


    }


    @Suppress("DEPRECATION")
    private fun fullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val w = window // in Activity's onCreate() for instance

        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun showWallpaper() {

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}