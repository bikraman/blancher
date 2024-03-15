package com.beniezsche.bluchr.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.adapter.AppListAdapter
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.util.DrawableUtil


class AppListActivity : BaseActivity() {

    lateinit var  appListAdapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_apps)

        val rvAllApps = findViewById<RecyclerView>(R.id.rv_all_apps)
        val etAppName = findViewById<EditText>(R.id.et_app_name)

        val w = window // in Activity's onCreate() for instance

        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        rvAllApps.layoutManager = LinearLayoutManager(this)
        appListAdapter = AppListAdapter(this,getAllApps())

        rvAllApps.adapter = appListAdapter

        etAppName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var string = p0.toString()

                var list = getApp(string)

                appListAdapter.setCurrentList(list)

                if(list.size == 1){
                    openApp(list[0].packageName.toString())
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
    }

    private fun getApp(name: String): ArrayList<AppInfo> {

        val list = ArrayList<AppInfo>()

        for(app in getAllApps()) {
            if (app.label.contains(name,true)){
                list.add(app)
            }
        }

        return list
    }

    private fun openApp(packageName: String){
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(launchIntent)
        finish()
    }

    private fun getAllApps(): ArrayList<AppInfo> {

        val pm: PackageManager = packageManager
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

        return appsList
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}