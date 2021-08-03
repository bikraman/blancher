package com.beniezsche.bluchr.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.beniezsche.bluchr.activities.MainActivity
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.model.AppInfo


class FavoriteAppListAdapter(private val context: Context, private val appList: ArrayList<AppInfo>): RecyclerView.Adapter<FavoriteAppListAdapter.AppViewHolder>() {

    private val displayMetrics = DisplayMetrics()
    private var itemWidth = 0


    init {

        @Suppress("DEPRECATION")
        (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        itemWidth = displayMetrics.widthPixels / 4
    }


    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): AppViewHolder {

        return AppViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_app,parent, false))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        val appInfo = appList[position]


//        val displayMetrics = DisplayMetrics()
//        @Suppress("DEPRECATION")
//        (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//
//        val deviceWidth = displayMetrics.widthPixels / 4
        holder.itemView.layoutParams.width = itemWidth


        holder.ivAppIcon.setImageDrawable(appInfo.icon)
        holder.tvAppName.text = appInfo.label

        holder.itemView.setOnClickListener {

            val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName.toString())
            context.startActivity(launchIntent)
        }



    }

    override fun getItemCount(): Int {
        return appList.size
    }

    class AppViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val ivAppIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
        val tvAppName: TextView = itemView.findViewById(R.id.tv_app_name)
//        val cvParent: ConstraintLayout = itemView.findViewById(R.id.cv_parent)

    }

}

