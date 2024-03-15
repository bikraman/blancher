package com.beniezsche.bluchr.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.activities.AppListActivity
import com.beniezsche.bluchr.adapter.AppListAdapter
import com.beniezsche.bluchr.adapter.FavoriteAppListAdapter
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import com.beniezsche.bluchr.util.DrawableUtil
import java.util.*
import kotlin.collections.ArrayList

class AllAppsFragment : Fragment() {

    lateinit var  appListAdapter: AppListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_all_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAllApps = view.findViewById<RecyclerView>(R.id.rv_all_apps)
        val etAppName = view.findViewById<EditText>(R.id.et_app_name)


        rvAllApps.layoutManager = LinearLayoutManager(context)
        appListAdapter = AppListAdapter(requireContext(),getAllApps())

        rvAllApps.adapter = appListAdapter

        val appList = getAllApps()

        etAppName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val string = p0.toString()

                val list = getApp(string, appList)

                appListAdapter.setCurrentList(list)

                if(list.size == 1){
                    openApp(list[0].packageName)
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun getApp(name: String, applist: ArrayList<AppInfo>): ArrayList<AppInfo> {

        val list = ArrayList<AppInfo>()

        for(app in applist) {
            if (app.label.contains(name,true)){
                list.add(app)
            }
        }

        return list
    }

    private fun openApp(packageName: String){
        val launchIntent = activity?.packageManager?.getLaunchIntentForPackage(packageName)
        startActivity(launchIntent)
//        finish()
    }

    private fun getAllApps(): ArrayList<AppInfo> {

        val pm: PackageManager = activity?.packageManager!!
        val appsList = ArrayList<AppInfo>()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        val allApps = pm.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app = AppInfo()
            app.label = ri.loadLabel(pm).toString()
            app.packageName = ri.activityInfo.packageName
            app.icon = DrawableUtil.drawableToBase64(ri.activityInfo.loadIcon(pm)).toString()
            appsList.add(app)
        }

        appsList.sortBy { appInfo ->
            appInfo.label.uppercase()
        }

        return appsList
    }


}