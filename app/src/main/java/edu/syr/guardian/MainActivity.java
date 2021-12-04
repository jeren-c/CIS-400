package edu.syr.guardian;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button mMapButton;
    Button mLogOutButton;

    private static final String TAG =  "Mainactivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogOutButton = findViewById(R.id.main_button_logout);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        if (isServicesOK()){
            Log.d(TAG, "Map Service is OK");
            init();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "Clicked on home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.nav_map:
                        Toast.makeText(getApplicationContext(), "Clicked on map", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Map.class));
                        break;
                    case R.id.nav_health:
                        Toast.makeText(getApplicationContext(), "Clicked on health", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HealthActivity.class));
                        break;
                    case R.id.nav_info:
                        Toast.makeText(getApplicationContext(), "Clicked on information", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(getApplicationContext(), "Not Implemented", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.nav_log_out:
                        Toast.makeText(getApplicationContext(), "Clicked on logout", Toast.LENGTH_SHORT).show();
                        logout();
                        break;

                }

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void init(){
        mMapButton = findViewById(R.id.button_show_map);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Map.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK()
    {
        //checking google play service availibility
        Log.d(TAG, "checking google version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        //check if the service connection is success
        if (available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "Google API services available");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            // checking if the error is solvable
            Log.d(TAG, "error, we can fix it");
            Dialog dialog =  GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
        }
        else
        {
            //if cannot solve, just tell the user
            Toast.makeText(this, "Google API service not vaialable. Cannot fix", Toast.LENGTH_LONG).show();
        }

        return false;
    }

}