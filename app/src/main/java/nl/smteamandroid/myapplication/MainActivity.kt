package nl.smteamandroid.myapplication

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.UnlockEvent
import nl.smteamandroid.myapplication.database.UserEvent
import nl.smteamandroid.myapplication.service.UnlockCounterIntentService
import java.util.*

class MainActivity : AppCompatActivity() {

    // Mock users
    lateinit var db : AppDatabase
    private var serviceIntent : Intent? = null
    lateinit var animimgpage: Animation
    lateinit var bttone: Animation
    lateinit var bttwo: Animation
    lateinit var btthree: Animation
    lateinit var ltr:Animation

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        if(!isAppAllowed()){
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Please allow usage access for this app")

            builder.setPositiveButton("Allow") { dialog, which ->
                var usageAccesSettingsIntent: Intent =
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(Intent(usageAccesSettingsIntent))
            }

            builder.setNegativeButton("Do not allow") { dialog, which ->

            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!isAppAllowed()){
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Please allow usage access for this app")

            builder.setPositiveButton("Allow") { dialog, which ->
                var usageAccesSettingsIntent: Intent =
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(Intent(usageAccesSettingsIntent))
            }

            builder.setNegativeButton("Do not allow") { dialog, which ->

            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()
        startLockCounterService()
        addUsers()

        // Load animation
        animimgpage = AnimationUtils.loadAnimation(this, R.anim.animimgpage)
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone)
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo)
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree)
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr)

        imgpage.startAnimation(animimgpage)
        titlepage.startAnimation(bttone)

        btnLogin.setOnClickListener {

            if (!TextUtils.isEmpty(editEmail.text.toString()) && !TextUtils.isEmpty(editPassword.text.toString())) {
                var userEvent = checkLogin(editEmail.text.toString(), editPassword.text.toString())

                if (!userEvent!!.equals(null)) {
                    startActivity(Intent(this, NavigationActivity::class.java))
                } else {

                }
            }
        }
    }
    fun insertNewUser(currentTime : Date){
        db.unlockEventDao().insertNewLock(
            UnlockEvent(
                1,
                null,
                currentTime.toString(),
                null,
                0,
                null
            )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startLockCounterService(){
        val currentTime : Date = Calendar.getInstance().time
        if (db.unlockEventDao().getById(1) == null){
            insertNewUser(currentTime)
        }
        serviceIntent = Intent(this, UnlockCounterIntentService::class.java)

        if(!isServiceRunning(UnlockCounterIntentService::class.java)){
            startForegroundService(serviceIntent)
        }
    }

    private fun isAppAllowed(): Boolean{
        try {
            val packageManager : PackageManager = getPackageManager()
            val applicationInfo : ApplicationInfo = packageManager.getApplicationInfo(getPackageName(), 0)
            val appsOpsManager : AppOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appsOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName)

            if(mode == AppOpsManager.MODE_DEFAULT){
                val granted : Boolean = checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
                return granted
            }

            return (mode == AppOpsManager.MODE_ALLOWED)
        }
        catch (e : PackageManager.NameNotFoundException){
            return false
        }
    }

    private fun isServiceRunning(serviceClass : Class<*>) : Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.className)) {
                return true
            }
        }
        return false
    }

    private fun checkLogin(username : String, password : String) : UserEvent? {

        var userEvent = db.userEventDao().login(username,password)
        return userEvent
    }

    private fun addUsers(){
        // Add users to list if list is empty
        var users = db.userEventDao().getAll()
        if(users.isEmpty()){
            db.userEventDao().insert(
                UserEvent(
                    null,
                    "herm",
                    "kisjes",
                    1
                )
            )
            db.userEventDao().insert(
                UserEvent(
                    null,
                    "jan",
                    "janssen",
                    2
                )
            )
            db.userEventDao().insert(
                UserEvent(
                    null,
                    "jan",
                    "janssen",
                    3
                )
            )
        }
    }

    private fun checkIfUnlockEventIsAvailable() : Boolean {
        return (db.unlockEventDao().getById(1) != null)
    }

}
