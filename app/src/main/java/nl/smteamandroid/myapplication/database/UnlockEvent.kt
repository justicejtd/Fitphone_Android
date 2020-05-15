package nl.smteamandroid.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnlockEvent(
    @PrimaryKey(autoGenerate = true)val uid: Int?,
    @ColumnInfo(name = "unlocktime") val lastUnlocktime: String?,
    @ColumnInfo(name = "locktime") val lastLocktime: String?,
    @ColumnInfo(name = "time_between_last_unlock") val timeBetweenLastUnlock: String?,
    @ColumnInfo(name = "unlock_count") val unlockCount: Int,
    @ColumnInfo(name = "sleep_time") val sleepTime : String?
)