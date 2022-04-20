/*
 * Developed by Md Rayhan Uddin (rayhanuddin55@gmail.com)
 */

package com.parallax.partyapp.Utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager


import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mLocationManager: LocationManager? = null

    private var broadcastIntent: Intent = Intent(BROADCAST_ACTION)

    private var mLocationListeners = arrayOf(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER),
        LocationListener(LocationManager.PASSIVE_PROVIDER)
    )

    private inner class LocationListener(provider: String) : android.location.LocationListener {
        internal var mLastLocation: Location

        init {
            Log.e(TAG, "LocationListener $provider")
            mLastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: $location")
            mLastLocation.set(location)

            broadcastIntent.putExtra(INTENT_DOUBLE_LAT, location.latitude)
            broadcastIntent.putExtra(INTENT_DOUBLE_LON, location.longitude)
            broadcastIntent.putExtra(INTENT_FLOAT_ACCURACY, location.accuracy)

            sendBroadcast(broadcastIntent)
        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: $provider")
        }
    }


    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onCreate() {

        Log.e(TAG, "onCreate")

        isServiceRunning = true

        initializeLocationManager()

        try {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.e(TAG, "onLastLocation: $location")

                    location?.let {
                        broadcastIntent.putExtra(INTENT_DOUBLE_LAT, location.latitude)
                        broadcastIntent.putExtra(INTENT_DOUBLE_LON, location.longitude)
                        broadcastIntent.putExtra(INTENT_FLOAT_ACCURACY, location.accuracy)

                        sendBroadcast(broadcastIntent)
                    }
                }

            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                LOCATION_INTERVAL.toLong(),
                LOCATION_DISTANCE.toFloat(),
                mLocationListeners[1]
            )

            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_INTERVAL.toLong(),
                LOCATION_DISTANCE.toFloat(),
                mLocationListeners[0]
            )


        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }

    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()

        isServiceRunning = false

        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex)
                }

            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(
            TAG,
            "initializeLocationManager - LOCATION_INTERVAL: $LOCATION_INTERVAL LOCATION_DISTANCE: $LOCATION_DISTANCE"
        )
        if (mLocationManager == null) {
            mLocationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        }
    }

    companion object {

        var isServiceRunning = false

        private val TAG = this::class.java.name
        private const val LOCATION_INTERVAL = 0
        private const val LOCATION_DISTANCE = 0

        const val BROADCAST_ACTION = "broadcast_action_location"
        const val INTENT_DOUBLE_LAT = "lat"
        const val INTENT_DOUBLE_LON = "lon"
        const val INTENT_FLOAT_ACCURACY = "accuracy"
    }
}


/*
    // From Activity or Fragment

    // Create Broadcast receiver
    private var locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("lat", intent?.getDoubleExtra(LocationService.INTENT_DOUBLE_LAT, 0.0).toString())
            Log.d("lon", intent?.getDoubleExtra(LocationService.INTENT_DOUBLE_LON, 0.0).toString())
            Log.d("accuracy", intent?.getFloatExtra(LocationService.INTENT_FLOAT_ACCURACY, 0f).toString())

        }
    }

    // Register broadcast receiver
    registerReceiver(locationBroadcastReceiver, IntentFilter(LocationService.BROADCAST_ACTION))

    //Unregister Broadcast receiver
    unregisterReceiver(locationBroadcastReceiver)

*/