package nl.smteamandroid.myapplication.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class AppEvent(
    @PrimaryKey(autoGenerate = true) val uid : Int?,
    @ColumnInfo(name = "pacakage_name") val packageName: String?,
    @ColumnInfo(name = "appName") val appName: String?,
    @ColumnInfo(name = "category") val categoryName : String?,
    @ColumnInfo(name = "last_time_used") val lastTimeUsed: Long,
    @ColumnInfo(name = "total_time_used") val totalTimeUsed: Long
):Serializable