package com.emdad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.BuildConfig;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l12,whatsapp,logout,support;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private LocationHistoryService mService = new LocationHistoryService();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int MULTIPLE_PERMISSIONS =1 ;
    private boolean mBound = false;
    String email="";

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private SharedPreferences mPrefs;


    private MyReceiver myReceiver;




    ImageView header_scan;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,

            Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationHistoryService.LocalBinder binder = (LocationHistoryService.LocalBinder)service;
            mService = binder.getService();
            mBound = true;
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                mService.requestLocationUpdates1(getApplicationContext());
                Log.d("service","fail");
            }
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        setContentView(R.layout.activity_dashboard);

        myReceiver= new MyReceiver();


        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);



        dbHelper = new DatabaseAdapter(Dashboard.this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        sampleDB = this.openOrCreateDatabase("drs",Dashboard.this.MODE_PRIVATE, null);
        setSupportActionBar(toolbar);
        email=pref.getString("email",null);
        System.out.println("email is "+email);
        System.out.println("image"+pref.getString("messanger_image",null));
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        Intent intent = new Intent();
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        startService(new Intent(this, TrackerService.class));
        Intent callrecord = new Intent(Dashboard.this, TService.class);
        startService(callrecord);
//        startService(new Intent(this, TrackerService.class));
//        Intent intent1 = new Intent(Dashboard.this, TService.class);
//        startService(intent1);
        header_scan=(ImageView)toolbar.findViewById(R.id.header_scan);
        setTitle("");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name=(TextView)header.findViewById(R.id.hname);
        TextView hemail=(TextView)header.findViewById(R.id.hemail);
        ImageView imageView=(ImageView)header.findViewById(R.id.imageView);
        name.setText(""+pref.getString("messenger_name",null));
        hemail.setText(""+pref.getString("email",null));
        // name.setText(""+pref.getString("email",null));
        //  http://www.fast-option.com/tamco/messanger_images/1523961736.jpeg

        Picasso.with(this)
                .load("http://www.fast-option.com/tamco/messanger_images/"+pref.getString("messanger_image",null))
                .placeholder(R.drawable.logo)
                .into(imageView);

        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        l3=(LinearLayout)findViewById(R.id.l3);
        l4=(LinearLayout)findViewById(R.id.l4);
        l5=(LinearLayout)findViewById(R.id.l5);
        l6=(LinearLayout)findViewById(R.id.l6);
        l7=(LinearLayout)findViewById(R.id.l7);
        l8=(LinearLayout)findViewById(R.id.l8);
        l9=(LinearLayout)findViewById(R.id.l9);
        l10=(LinearLayout)findViewById(R.id.l10);
        l12=(LinearLayout)findViewById(R.id.l12);



        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Drslist.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Mapss.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Odometer1.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, ScanDrs.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Scanpickup.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Pickuplist.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Pickuphistory.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Drshistory1.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Profile.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Updateprofile.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        l9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
//                Intent intent = new Intent(Dashboard.this, TService.class);
//                stopService(intent);
//                stopLocationService();
                Intent i = new Intent(Dashboard.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                stopLocationService();
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });




//        checkLocationPermission();
//        if (isServiceRunning(TrackerService.class)) {
//            // If service already running, simply update UI.
//            setTrackingStatus(R.string.tracking);
//        }
////        else if (transportID.length() > 0 && email.length() > 0 && password.length() > 0) {
////            // Inputs have previously been stored, start validation.
////            checkLocationPermission();
////        } else {
////            // First time running - check for inputs pre-populated from build.
////
////        }

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            // finish();
        }






        header_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }
                else {
                    Intent i = new Intent(Dashboard.this, Headerscan.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }

    public boolean checkIsServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                //   PacificCustomLog.debug("isServiceRunning", service.service.getClassName() + "running");
                return true;
            }
        }
        //  PacificCustomLog.debug("isServiceRunning", "Service not running");

        return false;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onDestroy() {
//        Intent intent = new Intent(Dashboard.this, TService.class);
//        stopService(intent);
        //   stopLocationService();
        super.onDestroy();
        dbHelper.DeleteAll1234();
        dbHelper.DeleteAll();
        mService.removeLocationUpdates();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(Dashboard.this, Mapss.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(Dashboard.this, Odometer1.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.drs_list) {
            Intent i = new Intent(Dashboard.this, Drslist.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.scan_drs) {
            Intent i = new Intent(Dashboard.this, ScanDrs.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.scan_pickup) {
            Intent i = new Intent(Dashboard.this, Scanpickup.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        else if (id == R.id.nav_tools) {
            editor.clear();
            editor.commit();
            Intent i = new Intent(Dashboard.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setTrackingStatus(int status) {
        boolean tracking = status == R.string.tracking;
//        mTransportIdEditText.setEnabled(!tracking);
//        mEmailEditText.setEnabled(!tracking);
//        mPasswordEditText.setEnabled(!tracking);
//        mStartButton.setVisibility(tracking ? View.INVISIBLE : View.VISIBLE);
//        if (mSwitch != null) {
//            // Initial broadcast may come before menu has been initialized.
//            mSwitch.setChecked(tracking);
//        }
//        ((TextView) findViewById(R.id.title)).setText(getString(status));
    }

    @Override
    protected void onStart() {

        super.onStart();




        bindService(new Intent(this, LocationHistoryService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        //  boolean checkIsServiceRunning = checkIsServiceRunning(LocationUpdatesService.class);

        //    if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//            if(checkIsServiceRunning)
//                mService.requestLocationUpdates1(getApplicationContext());
//            else
//                Log.d("service","fail");
//        }

    }

    /**
     * Receives status messages from the tracking service.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTrackingStatus(intent.getIntExtra(getString(R.string.status), 0));
        }
    };



    @Override
    protected void onResume() {


        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationHistoryService.ACTION_BROADCAST));


    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();

    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();

    }
    /**
     * First validation check - ensures that required inputs have been
     * entered, and if so, store them and runs the next check.
     */
    private void checkInputFields() {
//        checkLocationPermission();
//        if (mTransportIdEditText.length() == 0 || mEmailEditText.length() == 0 ||
//                mPasswordEditText.length() == 0) {
//            Toast.makeText(TrackerActivity.this, R.string.missing_inputs, Toast.LENGTH_SHORT).show();
//        } else {
//            // Store values.
//            SharedPreferences.Editor editor = mPrefs.edit();
//            editor.putString(getString(R.string.transport_id), mTransportIdEditText.getText().toString());
//            editor.putString(getString(R.string.email), mEmailEditText.getText().toString());
//            editor.putString(getString(R.string.password), mPasswordEditText.getText().toString());
//            editor.apply();
//            // Validate permissions.
//
//         //   mSwitch.setEnabled(true);
//        }
    }

    /**
     * Second validation check - ensures the app has location permissions, and
     * if not, requests them, otherwise runs the next check.
     */
    private void checkLocationPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (locationPermission != PackageManager.PERMISSION_GRANTED
                || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        } else {
            checkGpsEnabled();
        }
    }

    /**
     * Third and final validation check - ensures GPS is enabled, and if not, prompts to
     * enable it, otherwise all checks pass so start the location tracking service.
     */
    private void checkGpsEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            reportGpsError();
        } else {
            //  resolveGpsError();
            startLocationService();
        }
    }

    /**
     * Callback for location permission request - if successful, run the GPS check.
     */


    @SuppressLint("NewApi")
    private void startLocationService() {
        // Before we start the service, confirm that we have extra power usage privileges.
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        Intent intent = new Intent();
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        startService(new Intent(this, TrackerService.class));
    }

    private void stopLocationService() {
        // stopService(new Intent(this, TrackerService.class));
    }


    private void confirmStop() {
        //mSwitch.setChecked(true);
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_stop))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        mSwitch.setChecked(false);
//                        mTransportIdEditText.setEnabled(true);
//                        mEmailEditText.setEnabled(true);
//                        mPasswordEditText.setEnabled(true);
//                        mStartButton.setVisibility(View.VISIBLE);
                        //      stopLocationService();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void reportPermissionsError() {
//        if (mSwitch != null) {
//            mSwitch.setChecked(false);
//        }
//        Snackbar snackbar = Snackbar
//                .make(
//                        findViewById(R.id.rootView),
//                        getString(R.string.location_permission_required),
//                        Snackbar.LENGTH_INDEFINITE)
//                .setAction(R.string.enable, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(android.provider.Settings
//                                .ACTION_APPLICATION_DETAILS_SETTINGS);
//                        intent.setData(Uri.parse("package:" + getPackageName()));
//                        startActivity(intent);
//                    }
//                });
//
//        // Changing message text color
//        snackbar.setActionTextColor(Color.RED);
//
//        // Changing action button text color
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(
//                android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        snackbar.show();
    }

    private void resolvePermissionsError() {
//        if (mSnackbarPermissions != null) {
//            mSnackbarPermissions.dismiss();
//            mSnackbarPermissions = null;
//        }
    }

    private void reportGpsError() {
//        if (mSwitch != null) {
//            mSwitch.setChecked(false);
//        }
//        Snackbar snackbar = Snackbar
//                .make(findViewById(R.id.rootView), getString(R.string
//                        .gps_required), Snackbar.LENGTH_INDEFINITE)
//                .setAction(R.string.enable, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                });

        // Changing message text color
//        snackbar.setActionTextColor(Color.RED);
//
//        // Changing action button text color
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
//                .snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        snackbar.show();

    }

    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.shipment),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Dashboard.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(Dashboard.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){

            case REQUEST_PERMISSIONS_REQUEST_CODE:

                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    mService.requestLocationUpdates1(Dashboard.this);
                }
                else {
                    // Permission denied.
                    //  setButtonsState(false);
                    Snackbar.make(
                            findViewById(R.id.shipment),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }

            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    //   getCallDetails(); // Now you call here what ever you want :)
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                }
                return;
            }

        }
    }


    public static void setDefaults(String key, Boolean value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //Fonction appelée pour récupérer ce qui a été saisi en mémoire
    public static Boolean getDefaults(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }



    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationHistoryService.EXTRA_LOCATION);
            Log.i("Working","done");
            // HashMap<String, String> latlong =new HashMap<>();
            if (location != null) {
//                             Toast.makeText(Shipdetails.this, Utils.getLocationText(location),
//                        Toast.LENGTH_SHORT).show();


                //  latlong.put("latlong",Utils.getLocationText(location));
                mService.firebaseData1(Utils.getLocationText(location));
//                  mService.firebaseTime("Sachin");
                Log.i("Working","????");
            }


        }

    }


}
