
package com.beniezsche.bluchr.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.activities.AppListActivity
import com.beniezsche.bluchr.activities.BaseActivity
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import com.beniezsche.bluchr.util.DrawableUtil
import com.google.gson.Gson


class AppListAdapter(private val context: Context, private val appList: ArrayList<AppInfo>): RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): AppViewHolder {

        return AppViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false))

    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        val appInfo = appList[position]

        holder.ivAppIcon.setImageDrawable(DrawableUtil.base64ToDrawable(appInfo.icon))
        holder.tvAppName.text = appInfo.label

        holder.itemView.setOnClickListener {

            val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName.toString())
            context.startActivity(launchIntent)
            (context as AppListActivity).finish()
        }

        holder.itemView.setOnLongClickListener {

            val favoritesList = Favorites.getFavoriteAppsList(context)

            if(favoritesList.size < 5) {

                Favorites.addToList(appInfo, context)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Favorites list already too much", Toast.LENGTH_SHORT).show()
            }

            true
        }

    }

    override fun getItemCount(): Int {
        return appList.size
    }

    fun setCurrentList(newList: ArrayList<AppInfo>){

        appList.clear()
        appList.addAll(newList)
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val ivAppIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
        val tvAppName: TextView = itemView.findViewById(R.id.tv_app_name)

    }

}

