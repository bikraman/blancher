package com.beniezsche.bluchr.model

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class AppInfo {
    @PrimaryKey
    var packageName: String = ""
    @ColumnInfo(name = "label")
    var label: String = ""

    @ColumnInfo(name = "icon")
    var icon: String = ""

    @ColumnInfo(name = "is_hidden")
    var isHidden: Boolean = false

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
}