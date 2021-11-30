package edu.syr.guardian;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class Map extends FragmentActivity implements OnMapReadyCallback {

//    GoogleMap map;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
//
//        LatLng TechCtr = new LatLng(43.03760745511544, -76.13050018452081);
//        map.addMarker(new MarkerOptions().position(TechCtr).title("My Location"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(TechCtr));
//    }

    // Callback interface for when the map is ready to be used.
    //Once an instance of this interface is set on a MapFragment or MapView object,
    // the onMapReady(GoogleMap) method is triggered when the map is ready to be used and
    // provides a non-null instance of GoogleMap.

    public static final int LOCATION_PERMISSION_REQUEST_CODE  = 1234;
    private static String FINE_LOCATION =  Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted =  false;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //check for permission
        getLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        Log.d(TAG, "inside onCreate");
        initMap();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            //Log.d(TAG, "getLocation: permissions granted");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLastLocation = location;
                        LatLng FromSensor = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(FromSensor).title("Real Location"));
                    }
                }
            });
        }
    }

    private void initMap()
    {
        // put log"inside intiMap"
        Log.d(TAG, "inside intiMap");
        SupportMapFragment mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Map.this);
    }

    //this callback method is called when permission request comeas back
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "inside onRequestPermissionsResult");
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                    //initialzie the map fragment
                    initMap();
                }

        }
    }

    public void getLocationPermission()
    {
        String[] permissions =  {Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted =  true;
        else
        {
            //ask for permission
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "Map is ready!");
        mMap =  googleMap;
        LatLng TechCtr = new LatLng(43.03760745511544, -76.13050018452081);
        LatLng HoL = new LatLng(43.03878212486239, -76.13454515767154);
        getLocation();
        mMap.addMarker(new MarkerOptions().position(TechCtr).title("My Location"));
        mMap.addMarker(new MarkerOptions().position(HoL).title("User Location"));
        Log.d(TAG, "Marked added");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TechCtr));
    }
}