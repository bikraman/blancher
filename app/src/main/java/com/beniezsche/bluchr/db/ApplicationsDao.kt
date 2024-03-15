package com.beniezsche.bluchr.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beniezsche.bluchr.model.AppInfo

@Dao
interface ApplicationsDao {
    @Query("SELECT * FROM appinfo WHERE is_hidden IS 0")
    fun getAll(): List<AppInfo>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun get(first: String, last: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(appInfos: List<AppInfo>)

    @Query("UPDATE appinfo SET is_hidden = 1 WHERE packageName LIKE :packageName ")
    fun hideApp(packageName: String)

    @Delete
    fun delete(appInfo: AppInfo)
}