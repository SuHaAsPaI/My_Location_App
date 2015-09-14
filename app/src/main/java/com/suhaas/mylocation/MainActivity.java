package com.suhaas.mylocation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tLat,tLong,tAlti,tAcc;
    Button bShare;
    final Context context = this;
    private LocationManager locationManager;
    private String provider;
    double lat,lon,alt,acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bShare=(Button)findViewById(R.id.button);
        tLat=(TextView)findViewById(R.id.textView);
        tLong=(TextView)findViewById(R.id.textView2);
        tAlti=(TextView)findViewById(R.id.textView3);
        tAcc=(TextView)findViewById(R.id.textView4);


        bShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });


        // Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        // startActivity(gpsOptionsIntent);
        // Get the location manager
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            tLat.setText("Latitude = Unkwown");
            tLong.setText("Longitude = Unknown");
            tAlti.setText("Altitude = Unknown");
            tAcc.setText("Accuracy = Unknown");
        }


    }


    private void shareImage() {
        String score=("Latitiude="+String.valueOf(lat)+"\nLongitude="+String.valueOf(lon)+"\nAltitude="+String.valueOf(alt)+"\nAccuracy="+String.valueOf(acc));
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        ///Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/drawable/" + "aaa");
       // sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Location");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, score);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=(double)(location.getLatitude());
        lon=(double)(location.getLongitude());
        alt=(double)(location.getAltitude());
        acc=(double)(location.getAccuracy());
        tLat.setText("Latitude = " + String.valueOf(lat));
        tLong.setText("Longitude = " + String.valueOf(lon));
        tAlti.setText("Altitude = " + String.valueOf(alt));
        tAcc.setText("Accuracy = " + String.valueOf(acc));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        showSettingsAlert();
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }



    public void showSettingsAlert(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
