package nl.smteamandroid.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UnlockEventDao {
    @Query("SELECT * FROM UnlockEvent")
    fun getAll(): List<UnlockEvent>

    @Query("SELECT * FROM UnlockEvent WHERE uid = :user_id")
    fun getById(user_id : Int): UnlockEvent


    @Query("SELECT * FROM UnlockEvent WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UnlockEvent>

    @Insert
    fun insertAll(vararg unlockEvent: UnlockEvent)

    @Insert
    fun insertNewUnlock(vararg unlockEvent : UnlockEvent)

    @Insert
    fun insertNewLock(vararg unlockEvent : UnlockEvent)

    @Query("UPDATE UnlockEvent SET unlock_count=:unlockCount1, unlocktime=:unlocktime1 WHERE uid = :user_id")
    fun updateUnlockCount(unlockCount1: Int, unlocktime1: String?, user_id: Int)

    @Query("UPDATE UnlockEvent SET locktime=:locktime WHERE uid = :user_id")
    fun updateLockTime(locktime: String, user_id: Int)

    @Query("SELECT COUNT (*) FROM UnlockEvent")
    fun count(): Int

    @Delete
    fun delete(user: UnlockEvent)

    @Query("UPDATE UnlockEvent SET unlock_count=:unlockCount1 WHERE uid = :user_id")
    fun setUnlockCount(unlockCount1: Int, user_id: Int)
}