package com.chandra.practice.notesmvvm

import android.Manifest
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import com.chandra.practice.notesmvvm.notification.setAlarmForDateTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable StrictMode ThreadPolicy and VmPolicy
        WindowCompat.setDecorFitsSystemWindows(window, false)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()             // Detect all thread violations (disk, network, etc.)
                .penaltyLog()            // Log violation to Logcat
                .penaltyDeath()          // Crash the app on violation (useful for testing)
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // Detect leaked SQLite objects
                .detectLeakedClosableObjects() // Detect leaked closable objects
                .penaltyLog()                  // Log violations
                .build()
        )
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment !!.navController
        //navController.navigate(R.id.homeFragment)
      //  setAlarmForDateTime(this, "26-Sep-2025 12:30:00 am")
        // Example: This network call on main thread will cause StrictMode violation
        doNetworkOnMainThread()
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun doNetworkOnMainThread() {
        Log.d("51", "${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d("54", "${Thread.currentThread().name}")
                val url = URL("https://www.google.com")
                val connection = url.openConnection()
                connection.connect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Function that deliberately causes a network on main thread violation
   /* private fun doNetworkOnMainThread() {
        try {
            val url = java.net.URL("https://www.google.com")
            val connection = url.openConnection()
            connection.connect()  // This will cause StrictMode violation (Network on main thread)
            Log.d("StrictModeTest", "Connected to Google")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

}