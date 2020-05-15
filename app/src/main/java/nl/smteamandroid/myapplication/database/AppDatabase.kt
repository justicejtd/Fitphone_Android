package nl.smteamandroid.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AppEvent::class, UnlockEvent::class, UserEvent::class, SleepHourEvent::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appEventDao(): AppEventDao
    abstract fun unlockEventDao() : UnlockEventDao
    abstract fun userEventDao() : UserEventDao
    abstract fun sleepHourEventDao() : SleepHourEventDao
}
