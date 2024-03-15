package com.beniezsche.bluchr.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.activities.AppListActivity
import com.beniezsche.bluchr.adapter.FavoriteAppListAdapter
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import java.util.*

class HomeFragment : Fragment() {

    private var timer : Timer? = null

    lateinit var rvFavoriteApps : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)

        rvFavoriteApps = view.findViewById<RecyclerView>(R.id.rv_favorite_apps)
        val tvTime = view.findViewById<TextView>(R.id.tv_time)
        val tvAllApps = view.findViewById<ImageView>(R.id.tv_all_apps)

        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rvFavoriteApps.adapter = FavoriteAppListAdapter(requireContext(), getAllFavoriteApps())
        rvFavoriteApps.layoutManager = linearLayoutManager


        timer = Timer()

        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {

                activity?.runOnUiThread{
                    tvTime.text = getTime()
                }

            }

        },0,1000)

    }

    private fun getTime(): String {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d",hour,minute)
    }

    private fun getAllFavoriteApps(): ArrayList<AppInfo> {
        return ArrayList(Favorites.getFavoriteAppsList(requireContext()))
    }


}