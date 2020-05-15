package nl.smteamandroid.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SleepHourEvent (
    @PrimaryKey(autoGenerate = true) val uid : Int?,
    @ColumnInfo(name = "hours") val hours: String?,
    @ColumnInfo(name = "date") val date: String?
    )
