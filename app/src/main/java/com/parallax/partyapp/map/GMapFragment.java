package com.parallax.partyapp.map;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parallax.partyapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static String BROADCAST_ACTION = "googleMapBroadCastAction";
    public static String INTENT_ADDRESS = "pickupAdress";
    public static String INTENT_LAT = "lat";
    public static String INTENT_LNG = "lng";

    Intent intent = new Intent(BROADCAST_ACTION);

    private SupportMapFragment mapFragment;

    public GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;
    private Geocoder geocoder;

    private String markerAddress;

//    OnConnectedListener onConnectedListener;

    boolean mTimerIsRunning;

    String finalAddress;
    LatLng finalLatlng;

    LinearLayout layoutMarker;


    public GMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_gmap, container, false);

        layoutMarker = viewGroup.findViewById(R.id.layoutMarker);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        isGpsOn();


        return viewGroup;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }

        buildGoogleApiClient();
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

//        moveCamera(new LatLng(16.853119, 96.166905), 8f);

        new Handler().postDelayed(() -> {
            try {
                moveCamera(new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude()), 15f);
            } catch (Exception e) {
//                moveCamera(new LatLng(16.853119, 96.166905), 8f);
            }
        }, 1000);

        new Handler().postDelayed(() -> setMarkerGetAddress(mGoogleMap.getCameraPosition().target), 1000);


        setMapListeners();

    }

    public void hideMarker() {
        if (layoutMarker.getVisibility() == View.VISIBLE) {
            layoutMarker.setVisibility(View.GONE);
        }
    }

    private void setMapListeners() {

        mGoogleMap.setOnMyLocationButtonClickListener(() -> {
            if (isGpsOn()) {
                try {
                    LatLng latLng = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                    setMarkerGetAddress(latLng);
                    moveCamera(latLng, 15);
                } catch (Exception ignored) {

                }
            }
            return true;
        });

        mGoogleMap.setOnCameraMoveStartedListener(i -> mTimerIsRunning = true);

        mGoogleMap.setOnCameraIdleListener(() -> {
            if (mTimerIsRunning) {
//                mGoogleMap.clear();
                setMarkerGetAddress(mGoogleMap.getCameraPosition().target);
                mTimerIsRunning = false;
            }
        });
    }

    public void moveCamera(LatLng latLng, float zoomLevel) {

        Log.d("latlngMoveCamera", String.valueOf(latLng.latitude));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

//        //remove previously placed Marker
//        if (marker != null) {
//            marker.remove();
//        }
//
//        //place marker where user just clicked
//        marker = mGoogleMap.addMarker(new MarkerOptions().position(pickupLatLng));
//        marker.setDraggable(true);
    }

    public void moveCameraWithAnimation(LatLng latLng, float zoomLevel) {

        Log.d("latlngMoveCamera", String.valueOf(latLng.latitude));

        MarkerOptions mo = new MarkerOptions();
        mo.position(latLng);

        mGoogleMap.addMarker(mo);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

//        //remove previously placed Marker
//        if (marker != null) {
//            marker.remove();
//        }
//
//        //place marker where user just clicked
//        marker = mGoogleMap.addMarker(new MarkerOptions().position(pickupLatLng));
//        marker.setDraggable(true);
    }


    private void setMarkerGetAddress(LatLng latLng) {
        Log.d("mylast", String.valueOf(latLng.latitude));

        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() == 0) {
            return;
        }

        Address address = addresses.get(0);

        StringBuilder sb = new StringBuilder();
        if (address != null) {
//            for (int i = 0; i < pickupAdress.getMaxAddressLineIndex(); i++) {
//                sb.append(pickupAdress.getAddressLine(i) + "\n");
//            }
            try {
                sb.append(address.getAddressLine(0));
                if (address.getLocality() != null && !sb.toString().toLowerCase().trim().contains(address.getLocality().toLowerCase().trim()))
                    sb.append(", ").append(address.getLocality());
            } catch (Exception ignored) {

            }
            //Toasty.makeText(getContext(), sb.toString() + "," + pickupAdress.getCountryName().toString(), Toasty.TYPE_ERROR);

            Log.d("selected: sb", String.valueOf(sb));
            Log.d("selected: countryName", String.valueOf(address.getCountryName()));
            Log.d("selected: locality", String.valueOf(address.getLocality())); // city name
            Log.d("selected: featureName", String.valueOf(address.getFeatureName()));
            Log.d("selected: adminArea", String.valueOf(address.getAdminArea()));
            Log.d("selected: subAdminArea", String.valueOf(address.getSubAdminArea()));
            Log.d("selected: subLocatity", String.valueOf(address.getSubLocality()));
        }

        markerAddress = sb.toString();

//        if (layoutMarker.getVisibility() == View.GONE) {
//            layoutMarker.setVisibility(View.VISIBLE);
//        }

        finalAddress = markerAddress;
        finalLatlng = latLng;


        intent.putExtra(INTENT_ADDRESS, finalAddress);
        intent.putExtra(INTENT_LAT, latLng.latitude + "");
        intent.putExtra(INTENT_LNG, latLng.longitude + "");

        if (getContext() != null) {
            getContext().sendBroadcast(intent);
        }

//        ((AddAddressFragment) getParentFragment()).setTvSelectedAddress(markerAddress);

        //remove previously placed Marker
//        if (marker != null) {
//            marker.remove();
//        }
//
//        //place marker where user just clicked
//        marker = mGoogleMap.addMarker(new MarkerOptions().position(pickupLatLng).title(sb.toString()));
//        marker.setDraggable(true);

    }

    private boolean isGpsOn() {

        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.gps_disabled_enabled_it)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            alert.show();

            return false;
        }

        return true;
    }

    public List getFinalAddressAndLatlng() {
        if (finalAddress == null || finalLatlng == null) {
            return null;
        }
        List item = new ArrayList();
        item.add(finalAddress);
        item.add(finalLatlng);
        return item;
    }

    public void requestLocationUpdate() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public void removeLocationUpdate() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        requestLocationUpdate();

        if (isGpsOn()) {
            try {
                LatLng latLng = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                setMarkerGetAddress(latLng);

//                onConnectedListener.onConnected();
            } catch (Exception e) {
//
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoogleApiClient.disconnect();
    }


}
