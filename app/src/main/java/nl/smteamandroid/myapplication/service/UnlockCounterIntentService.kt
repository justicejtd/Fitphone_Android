package nl.smteamandroid.myapplication.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.room.Room
import nl.smteamandroid.myapplication.R
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.SleepHourEvent
import nl.smteamandroid.myapplication.database.UnlockEvent
import java.text.SimpleDateFormat
import java.util.*



@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UnlockCounterIntentService : IntentService("UnlockCounterIntentService") {

    lateinit var lockScreen : LockScreenStateReceiver
    lateinit var db : AppDatabase
    lateinit var unlockEvent : UnlockEvent
    var countUnlock = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        lockScreen = LockScreenStateReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(lockScreen, filter)

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()

        unlockEvent = db.unlockEventDao().getById(1)
       // var lastUnlockEvent = unlockEvents.maxBy { it -> it.unlockCount }
        if(unlockEvent != null){
            countUnlock = unlockEvent.unlockCount
        }

        val channelId = createNotificationChannel("my_service", "my background service")
        val notificationManager = NotificationCompat.Builder(this, channelId)
        val notification = notificationManager.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setContentText("Unlock / Lock counter service")
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
        while(true){

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    inner class LockScreenStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            // Screen lock
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                val currentTime : Date = Calendar.getInstance().time

                // Insert last screen lock time



                    db.unlockEventDao().updateLockTime(currentTime.toString(),1)

                Log.d("ScreenOff",db.unlockEventDao().getById(1).unlockCount.toString() +" Time: "+currentTime)

            }

            // Screen unlock
           else if(intent.action == Intent.ACTION_USER_PRESENT){
                Log.d("test", "test")
                //Handle resuming events if user is present/screen is unlocked
                val currentTime : Date = Calendar.getInstance().time

                // The time in format : hh mm ss to check if it is between 4 am and 12 am
                val c = Calendar.getInstance()
                val dateformat =
                    SimpleDateFormat("HH:mm:ss")  //it will give you the date in the formate that is given in the image

                val timeOfUnlock =  dateformat.format(c.time)

                //
                val events  = db.unlockEventDao().getAll() // All events
                var unlockEvent = db.unlockEventDao().getById(1);//events.getOrNull(events.lastIndex)

                // Calculates the difference last lock unlock
                val diff = calculateDiff(unlockEvent,currentTime)

                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24

                var sleepTime = hours.toString() + "hours, " + minutes.toString() + " minutes, " + seconds.toString() + " seconds"

                countUnlock = unlockEvent.unlockCount
                countUnlock++
                db.unlockEventDao().updateUnlockCount(countUnlock, currentTime.toString(), 1)

                val string1 = "04:00:00"
                val time1 = SimpleDateFormat("HH:mm:ss").parse(string1)
                val calendar1 = Calendar.getInstance()
                calendar1.time = time1
                calendar1.add(Calendar.DATE, 1)

                val string2 = "12:00:00"
                val time2 = SimpleDateFormat("HH:mm:ss").parse(string2)
                val calendar2 = Calendar.getInstance()
                calendar2.time = time2
                calendar2.add(Calendar.DATE, 1)

                val d = SimpleDateFormat("HH:mm:ss").parse(timeOfUnlock)
                val calendar3 = Calendar.getInstance()
                calendar3.time = d
                calendar3.add(Calendar.DATE, 1)

                val x = calendar3.time

                // If the last unlock time is between 4 am and 12 am && the difference is over 4 hours
                // Then the user slept
//                if (x.after(calendar1.time) && x.before(calendar2.time)) {
//                    db.sleepHourEventDao().insert(SleepHourEvent(1, hours.toString(), currentTime.toString()))
//                    Log.d("Sleep","You slept for " + sleepTime )
//                    Log.d("Sleep", " Hours: ${db.sleepHourEventDao().getById(1).hours}" )
//                }
            }

            if(unlockEvent != null){
                countUnlock = unlockEvent.unlockCount
            }
        }
    }

    // Measures time between last lock time and current unlock time
    fun calculateDiff(unlockEvent : UnlockEvent?, currentTime : Date?) : Long{
        val last_lock_time = unlockEvent?.lastLocktime
        val last_unlock_time = currentTime.toString()

        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US)
        val date1 = sdf.parse(last_unlock_time)
        val date2 = sdf.parse(last_lock_time)

        return date1.time - date2.time // Long
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        val forceIntent = Intent("com.example.usagestats.unlockcounterservice.UnlockCounterIntentService")
        sendBroadcast(forceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val forceIntent = Intent("com.example.usagestats.unlockcounterservice.UnlockCounterIntentService")
        sendBroadcast(forceIntent)
    }

    fun insertNewUser(currentTime : Date){
        db.unlockEventDao().insertNewLock(
            UnlockEvent(
                1,
                null,
                currentTime.toString(),
                null,
                countUnlock,
                null
            )
        )
    }
}
