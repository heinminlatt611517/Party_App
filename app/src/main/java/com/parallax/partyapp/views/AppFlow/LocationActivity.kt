package com.parallax.partyapp.views.AppFlow

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.parallax.partyapp.Models.LatLng
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.RuntimeData
import com.parallax.partyapp.Utils.hide
import com.parallax.partyapp.map.GMapFragment
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*

class LocationActivity : BaseActivity() {

    private var locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            address = intent?.getStringExtra(GMapFragment.INTENT_ADDRESS)
            lat = intent?.getStringExtra(GMapFragment.INTENT_LAT)
            lng = intent?.getStringExtra(GMapFragment.INTENT_LNG)

        }
    }

    companion object {
        val KEY_LAT = "key lat"
        val KEY_LNG = "key lng"
        val KEY_DRAW_POLY_OFF = "key_draw_poly"
    }

    var address: String? = null
    var lat: String? = null
    var lng: String? = null

    var gMapFragment = GMapFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val latSaved = intent?.getDoubleExtra(KEY_LAT, 0.0)
        val lngSaved = intent?.getDoubleExtra(KEY_LNG, 0.0)
        val isDrawPolyOff = intent?.getBooleanExtra(KEY_DRAW_POLY_OFF, false)!!

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext!!, getString(R.string.google_maps_key))
        }

        if (latSaved == 0.0) {
            registerReceiver(locationBroadcastReceiver, IntentFilter(GMapFragment.BROADCAST_ACTION))
        }

        supportFragmentManager.beginTransaction().replace(R.id.container_map, gMapFragment).commit()

        if (latSaved != 0.0) {

            layout_search.hide()
            btn_done.hide()


            Handler().postDelayed({

                gMapFragment.hideMarker()

                if (isDrawPolyOff) {
                    gMapFragment.moveCameraWithAnimation(
                        com.google.android.gms.maps.model.LatLng(latSaved!!, lngSaved!!),
                        12f
                    )
                } else {
                    drawPolyLine(latSaved!!, lngSaved!!)
                }


            }, 1500)
        }


        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        layout_search.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(this)
            startActivityForResult(intent, 8888)
        }

        btn_done.setOnClickListener {

            if (latSaved == 0.0) {
                lat?.let {
                    RuntimeData.latLng = LatLng(address!!, lat!!, lng!!)
                }
            }

            onBackPressed()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun drawPolyLine(savedLat: Double, savedLng: Double) {

        if (RuntimeData.latLng2 == null) return

        val myLatLng = RuntimeData.latLng2

        val from = com.google.android.gms.maps.model.LatLng(
            myLatLng?.lat?.toDouble()!!,
            myLatLng.lng.toDouble()!!
        )

        val to = com.google.android.gms.maps.model.LatLng(savedLat, savedLng)

        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
            .from(from)
            .to(to)
            .transportMode(TransportMode.DRIVING)
            .execute(object : DirectionCallback {
                override fun onDirectionSuccess(direction: Direction, rawBody: String) {
                    if (direction.isOK) {
                        val route = direction.routeList[0]
                        gMapFragment.mGoogleMap?.addMarker(
                            MarkerOptions().position(from).title(
                                getString(R.string.my_location)
                            )
                        )
                        gMapFragment.mGoogleMap?.addMarker(
                            MarkerOptions().position(to).title(
                                getString(R.string.party_location)
                            )
                        )

                        val directionPositionList = route.legList[0].directionPoint
//                        val directionPositionList2 = route.legList[1].directionPoint

                        gMapFragment.mGoogleMap?.addPolyline(
                            DirectionConverter.createPolyline(
                                this@LocationActivity,
                                directionPositionList,
                                5,
                                Color.RED
                            )
                        )
//                        gMapFragment.mGoogleMap?.addPolyline(DirectionConverter.createPolyline(this@LocationActivity, directionPositionList2, 5, Color.BLUE))
                        setCameraWithCoordinationBounds(route)

                    } else {
                        Log.d("direction", "Direction failed")
                    }
                }

                override fun onDirectionFailure(t: Throwable) {
                    Log.d("direction", "Direction failed" + t.localizedMessage)
                }
            })
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        gMapFragment.mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8888) {
            when (resultCode) {
                Activity.RESULT_OK -> {

                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Log.i("pickedPlace", "Place: " + place.name + ", " + place.id)

                    gMapFragment.moveCameraWithAnimation(
                        com.google.android.gms.maps.model.LatLng(
                            place.latLng?.latitude!!,
                            place.latLng?.longitude!!
                        ),
                        12f
                    )

                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)

                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }


    override fun onDestroy() {

        try {
            unregisterReceiver(locationBroadcastReceiver)
        } catch (e: Exception) {
        }
        super.onDestroy()

    }
}
