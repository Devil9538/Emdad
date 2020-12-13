package com.emdad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.util.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// This class can access everything from its parent...


public class TService extends Service implements LocationListener {

    //private static final int MULTIPLE_PERMISSIONS =1 ;
    /*
    ==========location working =========================*/
    boolean isGPSEnable = false;
    boolean callStart = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;


    @SuppressLint("MissingPermission")
    @Nullable



    private void fn_getlocation() {
        if(callStart) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (isGPSEnable) {
                location = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 50, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Geocoder gc = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
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
                                city = address.getLocality() + "," + address.getLocality() + "," + address.getPostalCode() + "," + address.getCountryName();

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //fn_update(location);
                    }
                }
            }


        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location){

        intent.putExtra("latutide",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,notify_interval);
        intent = new Intent(str_receiver);



//        fn_getlocation();
    }
    /* ==================================================== */


    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    private MediaRecorder recorder;
    private File audiofile;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";

    long st;
    long et;
    long diffe;
    String start123="",end1="",number1="",type1="",path="",id="",slipno="",city="",call_recCheck="",awb="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String dura="";
   // String lati="",lngi="";


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
//        Bundle extras = arg0.getExtras();
//        awb = (String) extras.get("slipno");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "destroy");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("StartService", "TService");
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_OUT);
            filter.addAction(ACTION_IN);
            this.registerReceiver(new CallReceiver(), filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startRecording() {
        callStart=true;
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/TestRecording");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = "Record";
        try {
            audiofile = File.createTempFile(file_name, ".mp3", sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        recorder = new MediaRecorder();
//                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        //System.out.println("audiofile is "+audiofile.getAbsolutePath());
        
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        recordstarted = true;
    }

    private void stopRecording() {
        if (recordstarted) {
            recorder.stop();
            recordstarted = false;
            callStart=false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public abstract class PhonecallReceiver extends BroadcastReceiver {

        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private Date callStartTime;
        private boolean isIncoming;
        private String savedNumber;  //because the passed incoming is only valid in ringing
        private DatabaseAdapter dbHelper;
        SQLiteDatabase sampleDB;

        @Override
        public void onReceive(Context context, Intent intent) {

            dbHelper = new DatabaseAdapter(context);
            try {
                dbHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            sampleDB = context.openOrCreateDatabase("call",context.MODE_PRIVATE, null);
//        startRecording();
            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
               // awb=intent.getExtras().getString("slipno");
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {
              //  awb=intent.getExtras().getString("slipno");
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if(TextUtils.isEmpty(number)) {
                    Log.d("incomingchecknulll", savedNumber + " ");
                }
                else
                {savedNumber = number;
                    Log.d("incomingcheck", savedNumber + " ");}
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }


                onCallStateChanged(context, state, number);
            }
        }

        //Derived classes should override these to respond to specific events of interest
        protected abstract void onIncomingCallReceived(Context ctx, String number, Date start);

        protected abstract void onIncomingCallAnswered(Context ctx, String number, Date start);

        protected abstract void onIncomingCallEnded(Context ctx, String number, Date start, Date end);

        protected abstract void onOutgoingCallStarted(Context ctx, String number, Date start);

        protected abstract void onOutgoingCallEnded(Context ctx, String number, Date start, Date end);

        protected abstract void onMissedCall(Context ctx, String number, Date start);

        //Deals with actual events

        //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
        //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
        public void onCallStateChanged(Context context, int state, String number) {
            if (lastState == state) {
                //No change, debounce extras

                return;
            }
            System.out.println("state of call:"+state);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    Date currentDate = new Date(System.currentTimeMillis());
                    callStartTime = currentDate;
                   // savedNumber = number;
                    onIncomingCallReceived(context, number, callStartTime);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    //Transition of ringing->offhook are pick  ups of incoming calls.  Nothing done on them
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        Date currentDate2 = new Date(System.currentTimeMillis());
                        callStartTime = currentDate2;
                        st= System.currentTimeMillis();
                     //   startRecording();
                        onOutgoingCallStarted(context, number, callStartTime);
                    } else {
                        isIncoming = true;
                        Date currentDate3 = new Date(System.currentTimeMillis());
                        callStartTime = currentDate3;
                        st= System.currentTimeMillis();
                     //   startRecording();
                        onIncomingCallAnswered(context, number, callStartTime);
                    }

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        //Ring but no pickup-  a miss
                        onMissedCall(context, savedNumber, callStartTime);
                    } else {
                        if (isIncoming) {
                            stopRecording();


                            Date currentDate4 = new Date(System.currentTimeMillis());
                            onIncomingCallEnded(context, savedNumber, callStartTime, currentDate4);

                        } else {
                            stopRecording();
                            Date currentDate5 = new Date(System.currentTimeMillis());
                            onOutgoingCallEnded(context, savedNumber, callStartTime, currentDate5);
                        }
                        et = System.currentTimeMillis();
                        diffe = et - st;
                        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        editor = pref.edit();
                        id = pref.getString("id", null);
                        slipno = pref.getString("awb", null);
                        call_recCheck = pref.getString("callrec_on", "N");

                        RequestQueue requestQueue = Volley.newRequestQueue(context);


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL + "?method_name=callrecord&driver_id=" + id + "&awb_no=" + slipno + "&location_of_call=" + city.replaceAll(" ", "%20") + "&duration_of_calls=" + dura.replaceAll(" ", "%20") + "&start_time=" + start123.replaceAll(" ", "%20") + "&end_time=" + end1.replaceAll(" ", "%20") + "&lattitude=" + latitude + "&logitude=" + longitude + "&type=" + type1 + "&number=" + savedNumber + "", new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // mProgressDialog.dismiss();
                                //  Toast.makeText(Drshistory1.this, error.toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                File file = new File(audiofile.getAbsolutePath());

                                FileInputStream fileInputStreamReader = null;
                                try {
                                    fileInputStreamReader = new FileInputStream(file);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                byte[] bytes = new byte[(int)file.length()];
                                try {
                                    fileInputStreamReader.read(bytes);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
                                params.put("path", ""+encodedfile);

                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                50000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(stringRequest);


                        // context.getApplicationContext().startActivity(ii);

                    }



                    break;
            }
            lastState = state;
        }

    }

    public class CallReceiver extends PhonecallReceiver {

        @Override
        protected void onIncomingCallReceived(Context ctx, String number, Date start) {
            Log.d("onIncomingCallReceived", number + " " + start.toString());

        }

        @Override
        protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
            Log.d("onIncomingCallAnswered", number + " " + start.toString());



        }

        @Override
        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            Log.d("onIncomingCallEnded", number + " " + start.toString() + "\t" + end.toString());
            start123=start.toString();
            end1=end.toString();
            number1=number;
            type1="incoming";

//                long id = dbHelper.Db_Save_Call_log("" + audiofile.getAbsolutePath(), ""+start123 , ""+end1, "" ,"",type1);
//                System.out.println(" database id  is" + id);

        }

        @Override
        protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
            Log.d("onOutgoingCallStarted", number + " " + start.toString());
        //    System.out.println("audiofile is "+audiofile.getAbsolutePath());

        }

        @Override
        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
            Log.d("onOutgoingCallEnded", number + " " + start.toString() + "\t" + end.toString());
      //      System.out.println("audiofile is "+audiofile.getAbsolutePath());
            start123=start.toString();
            end1=end.toString();
            number1=number;
            type1="outgoing";

//                long id = dbHelper.Db_Save_Call_log("" + audiofile.`(), ""+start123 , ""+end1, "" ,"",type1);
//                System.out.println(" database id  is" + id);




        }

        @Override
        protected void onMissedCall(Context ctx, String number, Date start) {
            Log.d("onMissedCall", number + " " + start.toString());
//        PostCallHandler postCallHandler = new PostCallHandler(number, "janskd" , "")
        }

    }

}
