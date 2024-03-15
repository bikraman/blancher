package com.beniezsche.bluchr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.beniezsche.bluchr.model.AppInfo
import com.beniezsche.bluchr.model.Favorites
import com.beniezsche.bluchr.ui.theme.BluchrTheme
import com.beniezsche.bluchr.util.DrawableUtil
import com.beniezsche.bluchr.viewmodels.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ComposeMainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BluchrTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Display 2 items
                    val pagerState = rememberPagerState(pageCount = {
                        2
                    })
                    val scope = rememberCoroutineScope()

                    BackHandler(enabled = true) { }

                    val context = LocalContext.current
                    val list = remember { mutableStateListOf<AppInfo>() }
                    list.addAll(Favorites.getFavoriteAppsList(context))

                    val vm = remember { AppViewModel(context) }

                    HorizontalPager(state = pagerState, beyondBoundsPageCount = 1) { page ->
                        // Our page content
                        when (page) {
                            0 -> {
                                FavoriteAppsList(list)
                            }
                            1 -> {
                                AppList(vm,
                                    onAppAddedToFavorites = { app ->
                                    val favoritesList = Favorites.getFavoriteAppsList(context)

                                    if (favoritesList.size < 5) {

                                        Favorites.addToList(app, context)
                                        list.add(app)
                                        Toast
                                            .makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Favorites list already too much",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                },
                                    onBackPressed = {
                                        scope.launch {
                                            pagerState.scrollToPage(0)
                                    }}
                                )
                            }
                        }
                    }
                }
            }
        }

        //Hide the status bars

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}


private fun getAllApps(context: Context): ArrayList<AppInfo> {

    val pm: PackageManager =  context.packageManager!!
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(viewModel: AppViewModel,onAppAddedToFavorites: (app: AppInfo) -> Unit, onBackPressed: () -> Unit) {

    val context = LocalContext.current

    BackHandler {
        onBackPressed()
    }

    val allAppsList = remember { mutableStateListOf<AppInfo>() }
    val searchedAppsList = remember { mutableStateOf(ArrayList<AppInfo>())}

    LaunchedEffect(Unit) {
        allAppsList.addAll(getAllApps(context))
        searchedAppsList.value.addAll(allAppsList)

        viewModel.addAppInfoToDB(allAppsList)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAllAppList().collect {
                searchedAppsList.value = ArrayList(it)
//                searchedAppsList.addAll(it)
            }
        }
    }


    val searchItem = remember { mutableStateOf("") }

    val enabled = remember { mutableStateOf(true) }

    Column {
        TextField(
            value = searchItem.value,
            placeholder = {
                Text("Click here to search apps")
            },
            enabled = enabled.value,
            onValueChange = { searchTerm ->
                searchItem.value = searchTerm
                searchedAppsList.value.clear()
                val filteredList = allAppsList.filter { it.label.contains(searchTerm, true) }
                searchedAppsList.value.addAll(filteredList)

                if (filteredList.size == 1) {
                    openApp(filteredList[0].packageName, context)
                }

            },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            itemsIndexed(searchedAppsList.value) { index, app->
                if (!app.isHidden) {
                    AppItem(app, onAppAddedToFavorites = onAppAddedToFavorites, onAppHidden = {
                        viewModel.hideApp(app.packageName)
//                        searchedAppsList.value.removeAt(index)
                    })
                }
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(app: AppInfo, onAppAddedToFavorites: (appInfo: AppInfo) -> Unit, onAppHidden: (app: AppInfo) -> Unit) {

    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }

    if(showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {

            Column (modifier = Modifier.background(Color.Black).padding(10.dp)){
                Text(text = app.label, fontSize = 20.sp)
                Divider(thickness = 1.dp, color = Color.Blue, modifier = Modifier.padding(top = 5.dp))
                Text(text = "Add To Favorites", modifier = Modifier.padding(top = 15.dp).clickable {
                    onAppAddedToFavorites(app)
                    showDialog.value = false
                })
                Text(text = "Hide App", modifier = Modifier.padding(top = 15.dp).clickable {
                    onAppHidden(app)
                    showDialog.value = false
                })
            }
        }
    }

    Text(text = app.label,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    openApp(app.packageName, context)
                },
                onLongClick = {
                    showDialog.value = true //!showDialog.value
//                    onAppAddedToFavorites(app)
                }

            )

    )
}

private fun openApp(packageName: String, context: Context){
    val launchIntent = (context as Activity).packageManager?.getLaunchIntentForPackage(packageName)
    context.startActivity(launchIntent)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteAppsList(list: SnapshotStateList<AppInfo>) {

    val context = LocalContext.current

    LazyColumn {
        itemsIndexed(list) { index, app ->
            Text(
                text = app.label,
                modifier = Modifier
                    .padding(10.dp)
                    .combinedClickable(
                        onClick = {
                            openApp(app.packageName, context)
                        },
                        onLongClick = {
                            list.removeAt(index)
                            removeFromList(index, context)
                        }
                    )
            )
        }
    }
}

fun removeFromList(position: Int, context: Context) {
    val list = Favorites.getFavoriteAppsList(context).toMutableList()
    list.removeAt(position)
    Favorites.saveAppInfoList(list, context)
}