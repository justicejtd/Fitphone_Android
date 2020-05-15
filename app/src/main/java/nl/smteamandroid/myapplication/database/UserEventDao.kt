package nl.smteamandroid.myapplication.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert

@Dao
interface UserEventDao {
    @Query("SELECT * FROM UserEvent")
    fun getAll(): List<UserEvent>

    @Query("SELECT * FROM UserEvent WHERE username = :userName AND password = :password")
    fun login(userName : String, password : String) : UserEvent

    @Insert
    fun insert(vararg user : UserEvent)
}