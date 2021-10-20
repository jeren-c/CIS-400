package edu.syr.guardian;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button mMapButton;
    Button mLogOutButton;

    private static final String TAG =  "Mainactivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogOutButton = findViewById(R.id.main_button_logout);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(mLogOutButton);
            }
        });

        if (isServicesOK()){
            Log.d(TAG, "Map Service is OK");
            init();
        }



    }

    public void logout(View view) {
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