package nl.smteamandroid.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEvent (
    @PrimaryKey(autoGenerate = true)val uid: Int?,
    @ColumnInfo(name="username") val username : String?,
    @ColumnInfo(name="password") val password : String?,
    @ColumnInfo(name="rank") val rank : Int?
    )