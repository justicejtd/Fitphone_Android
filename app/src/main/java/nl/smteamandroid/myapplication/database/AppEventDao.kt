package nl.smteamandroid.myapplication.database

import androidx.room.*

@Dao
interface AppEventDao {
    @Query("Select * FROM AppEvent WHERE category NOT NULL")
    fun getAll(): List<AppEvent>

    @Insert
    fun insertRow(vararg appEvent : AppEvent)

    @Query("DELETE FROM AppEvent")
    fun deleteAll()

    @Ignore
    @Query("SELECT SUM(total_time_used) as total_time_used, pacakage_name, appName, category, last_time_used FROM AppEvent GROUP BY category")
    fun getCategories(): List<AppEvent>

    @Ignore
    @Query("SELECT SUM(total_time_used) as total_time_used FROM AppEvent")
    fun getTotalUsageTime(): Long

    @Query("SELECT SUM(total_time_used) as total_time_used, pacakage_name, appName, category, last_time_used FROM AppEvent WHERE category = :selectedCategory AND category not null GROUP BY appName")
    fun getApps(selectedCategory: String):  List<AppEvent>

    @Query("SELECT SUM(total_time_used) as total_time_used FROM AppEvent WHERE category = null")
    fun getHoursOthers():  Double

    @Query("SELECT pacakage_name FROM AppEvent WHERE pacakage_name = :packageName")
    fun getApp(packageName : String): String

    @Query("UPDATE AppEvent SET last_time_used = :lastTimeUsed , total_time_used = :totalTimeUsed WHERE pacakage_name = :packageName")
    fun updateApp(packageName: String, lastTimeUsed: Long, totalTimeUsed: Long)
}