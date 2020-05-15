package nl.smteamandroid.myapplication.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StartServiceActivity : AppCompatActivity() {

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        var serviceIntent : Intent = Intent(applicationContext, UnlockCounterIntentService::class.java)
        startService(serviceIntent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onStart() {
        super.finish()
    }



}