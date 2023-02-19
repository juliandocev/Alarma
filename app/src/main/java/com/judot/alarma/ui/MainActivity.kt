package com.judot.alarma.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.judot.alarma.R
import com.judot.alarma.data.db.entities.AlarmsDatabase
import com.judot.alarma.data.repository.AlarmsRepository
import com.judot.alarma.databinding.ActivityMainBinding
import com.judot.alarma.other.Constants

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var viewModel: AlarmsViewModel



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
      //  navigateToTrackingFragmentIfNeeded(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        requestPermission()
        createNotificationChannel()

        val alarmsDatabase = AlarmsDatabase(this)
        val alarmsRepository = AlarmsRepository(alarmsDatabase)
        val viewModelProviderFactory = AlarmsViewModelProviderFactory(application,alarmsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(AlarmsViewModel::class.java)


        // Connect bottom navigation view with navigation component
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.AppNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding?.bottomNavigationView?.setupWithNavController(navController)

        navigateToTrackingFragmentIfNeeded(intent)

    }
    // ACTIVATE PERMISSIONS
    private fun hasScheduledExactAlarmPermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.SCHEDULE_EXACT_ALARM) ==
                PackageManager.PERMISSION_GRANTED
    private fun hasScheduledReceiveBootCompletedPermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.RECEIVE_BOOT_COMPLETED) ==
                PackageManager.PERMISSION_GRANTED

    private fun hasScheduledForegroundServicePermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.FOREGROUND_SERVICE) ==
                PackageManager.PERMISSION_GRANTED
    private fun hasScheduledVibratePermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.VIBRATE) ==
                PackageManager.PERMISSION_GRANTED
    private fun requestPermission(){
        // Create the list of permissions to request
        var permissionRequest = mutableListOf<String>()

        if(!hasScheduledExactAlarmPermission()){
            permissionRequest.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }
        if(!hasScheduledReceiveBootCompletedPermission()){
            permissionRequest.add(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        }
        if(!hasScheduledForegroundServicePermission()){
            permissionRequest.add(Manifest.permission.FOREGROUND_SERVICE)
        }
        if(!hasScheduledVibratePermission()){
            permissionRequest.add(Manifest.permission.VIBRATE)
        }

        // Request unrequested permissions
        if(permissionRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(
                this,
                permissionRequest.toTypedArray(),
                0)
        }
    }
    // We use it for the log
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("Permissions Request", "${permissions[i]} GRANTED")
                }else{
                    Log.d("Permissions Request", "${permissions[i]} DENIED")
                }

            }

        }
    }

    // Since Android 8 we need to create a notification channel
    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager. IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    // It comes from onNewIntent
    // It starts if our activity had been destroyed(application closed for example)
    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?){
        if(intent?.action == Constants.ACTION_SHOW_RING_FRAGMENT){ // If that action is attached we know that the this activity
            // has been launched by a notification click and in that case we want to navigate to our tracking fragment
            val navHostFragment= supportFragmentManager.findFragmentById(R.id.AppNavHostFragment) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_ring_fragment)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}