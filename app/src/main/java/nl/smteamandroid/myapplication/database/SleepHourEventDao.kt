package nl.smteamandroid.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SleepHourEventDao {

    @Insert
    fun insert(vararg sleepHourEvent: SleepHourEvent)

    @Query("SELECT * FROM SleepHourEvent WHERE uid = :user_id")
    fun getById(user_id: Int): SleepHourEvent

   // @Query("SELECT * FROM SleepHourEvent WHERE date BETWEEN DATEADD(DAY, 2 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) AND  DATEADD(DAY, 8 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) ");

}