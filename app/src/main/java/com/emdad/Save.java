package com.emdad;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Save extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    String start123="",end1="",number1="",type1="",path="",diffe="",id="",slipno="",city="",call_recCheck="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String dura="";
    String lati="",lngi="";
    Location location;
    LocationListener locationListener;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Toolbar toolbar = findViewById(R.id.toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setSupportActionBar(toolbar);
        setTitle("Wait.");
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        slipno=pref.getString("awb",null);
         call_recCheck=  pref.getString("callrec_on", "N");
        dbHelper = new DatabaseAdapter(this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        sampleDB = this.openOrCreateDatabase("drs",this.MODE_PRIVATE, null);
        if (!checkPermission()) {
          //  openActivity();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
             //   openActivity();
            }
        }
      Intent ii=getIntent();
      start123=ii.getStringExtra("start");
        end1=ii.getStringExtra("end");
        path=ii.getStringExtra("path");
        type1=ii.getStringExtra("type");
        number1=ii.getStringExtra("number");
        diffe=ii.getStringExtra("diffe");
       // slipno=ii.getStringExtra("slipno");

        System.out.println(start123);
        System.out.println(end1);
        System.out.println("path is:"+path);
        System.out.println(type1);
        System.out.println("slipno is:"+slipno);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");

        try {
            Date date1 = simpleDateFormat.parse(""+start123);
            Date date2 = simpleDateFormat.parse(""+end1);
            printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

                        long id = dbHelper.Db_Save_Call_log("" +path, ""+start123 , ""+end1, "" ,"",type1,""+number1);
                System.out.println(" database id  is" + id);
                System.out.println(" database id  is" + diffe);
//        Intent ii1=new Intent(this,Calllog.class);
//        startActivity(ii1);
//        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,},
                        10);
                return;
            } else {


                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager == null) {
                    Toast.makeText(Save.this, "Location Manager Not Available",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null)
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    lati= String.valueOf(location.getLatitude());
                    lngi= String.valueOf(location.getLongitude());
                    Geocoder gc = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = gc.getFromLocation(lat, lng, 1);
                        StringBuilder sb = new StringBuilder();
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                                sb.append(address.getAddressLine(i)).append("\n");
                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName());
//                            Toast.makeText(Save.this, "Location Are" + address.getLocality() + ":" + lng,
//                                    Toast.LENGTH_SHORT).show();
                            city=address.getLocality()+","+address.getLocality()+","+address.getPostalCode()+","+address.getCountryName();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        locationListener = new LocationListener() {

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

            public void onProviderEnabled(String arg0) {}

            public void onProviderDisabled(String arg0) {}

            public void onLocationChanged(Location l) {
                location = l;
                locationManager.removeUpdates(this);
                if (l.getLatitude() == 0 || l.getLongitude() == 0) {
                } else {
                    double lat = l.getLatitude();
                    double lng = l.getLongitude();
//                    Toast.makeText(Save.this, "Location Are" + lat + ":" + lng,
//                            Toast.LENGTH_SHORT).show();
                }
            }
        };

add();

    }


    public void add()
    {


        try {

                System.out.println("https://www.emdadsa.net/driver_new.php?method_name=callrecord&driver_id=" + id + "&awb_no=" + slipno + "&location_of_call=" + city.replaceAll(" ", "%20") + "&duration_of_calls=" + dura.replaceAll(" ", "%20") + "&start_time=" + start123.replaceAll(" ", "%20") + "&end_time=" + end1.replaceAll(" ", "%20") + "&lattitude=" + lati + "&logitude=" + lngi + " type="+type1+ "number="+number1+"");

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("https://www.emdadsa.net/driver_new.php?method_name=callrecord&driver_id="+id+"&awb_no="+slipno+"&location_of_call="+city.replaceAll(" ", "%20")+"&duration_of_calls=" + dura.replaceAll(" ", "%20")+"&start_time="+start123.replaceAll(" ", "%20")+"&end_time="+end1.replaceAll(" ", "%20")+"&lattitude="+lati.replaceAll(" ", "%20")+"&logitude="+lngi.replaceAll(" ", "%20")+"&type="+type1+"&number="+number1+"");
                MultipartEntity mpEntity = new MultipartEntity();
                //Path of the file to be uploaded
                String filepath = "";
                // File file = new File(picturePath);
                // console.log("pic file " + file);

                File file = new File(path);
                mpEntity.addPart("file", new FileBody(file));

                System.out.println("pic path is " + path);
                post.setEntity(mpEntity);
                System.out.println("pic path is " + path);
                HttpResponse response1 = client.execute(post);
                //Get the response from the server
                HttpEntity resEntity = response1.getEntity();
                String Response = EntityUtils.toString(resEntity);
                //  System.out.println("pic path is " + picturePath+""+Response);
                Log.d("Response:", Response);
                System.out.println("Response is " + Response);


                // ActivityCompat.finishAffinity(this);
//                AlertDialog.Builder dialog = new AlertDialog.Builder(Save.this);
//                dialog.setTitle( "Status" )
//                        .setMessage("Done!")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                           public void onClick(DialogInterface dialoginterface, int i) {
                editor.putString("callrec_on", "N");
                editor.commit();
                Intent ii = new Intent(Save.this, Shipdetails.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ii.putExtra("bid", "" + slipno);
                ii.putExtra("rphone", "" + number1);
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(ii);
                Save.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // dialoginterface.cancel();
                // }
                //}).show();



        }catch (Exception e)
        {
          System.out.println(e);
        }

    }
    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        dura=""+elapsedMinutes+"hour "+elapsedMinutes+"min "+elapsedSeconds+"sec";
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Ok");
                alertBuilder.setMessage("Permission");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Save.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(Save.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
          //  openActivity();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                  //  openActivity();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
