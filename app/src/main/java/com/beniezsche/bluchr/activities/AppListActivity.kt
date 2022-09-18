package com.beniezsche.bluchr.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.beniezsche.bluchr.R
import com.beniezsche.bluchr.adapter.AppListAdapter
import com.beniezsche.bluchr.model.AppInfo
import kotlinx.android.synthetic.main.activity_app_list.*

class AppListActivity : BaseActivity() {

    lateinit var  appListAdapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)

        rv_all_apps.layoutManager = LinearLayoutManager(this)
        appListAdapter = AppListAdapter(this,getAllApps())

        rv_all_apps.adapter = appListAdapter

        et_app_name.addTextChangedListener(object : TextWatcher{
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
            if (app.label!!.contains(name,true)){
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
            app.label = ri.loadLabel(pm)
            app.packageName = ri.activityInfo.packageName
            app.icon = ri.activityInfo.loadIcon(pm)
            appsList.add(app)
        }

        return appsList
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}