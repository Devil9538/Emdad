package com.emdad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.PushData;
import com.teliver.sdk.models.TripBuilder;
import com.teliver.sdk.models.UserBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shipdetails extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final int MULTIPLE_PERMISSIONS =1 ;
    EditText edtPhoneNo;
    TextView lblinfo;
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    String rphone="",slipno="";
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,  Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };



    String id="",bid="",status="",drs_unique_id="",city_id="",reph="",slipnotest="",address="";
    Spinner s1;
    Button button,button1,call_btn,open_map_btn;
    private static ProgressDialog mProgressDialog;
    TextView area_street,sd,length,width,height,weight,awbno,refno,cal,date2,name,location,mail,phone,cname,clocation,cmail,cphone,bookingdate,parcel,shipment,cod,pod;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String rationale = "Please provide permissions";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                // do your task.
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//            }
//        });
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        Intent ii=getIntent();
        bid=ii.getStringExtra("bid");
        editor.putString("awb", bid);
        editor.commit();
     slipnotest=pref.getString("awb",null);
        drs_unique_id=ii.getStringExtra("drs_unique_id");
       // city_id=ii.getStringExtra("city_id");
System.out.println(bid+"city_id is"+slipnotest);
        awbno=(TextView)findViewById(R.id.awbno);
        bookingdate=(TextView)findViewById(R.id.bookingdate);
        area_street=(TextView)findViewById(R.id.area_street);
        parcel=(TextView)findViewById(R.id.parcel);
        shipment=(TextView)findViewById(R.id.shipment);
        cod=(TextView)findViewById(R.id.cod);
        pod=(TextView)findViewById(R.id.pod);
        refno=(TextView)findViewById(R.id.refno);
        date2=(TextView)findViewById(R.id.date2);
        cal=(TextView)findViewById(R.id.cal);
        location=(TextView)findViewById(R.id.location);
        mail=(TextView)findViewById(R.id.mail);
        phone=(EditText)findViewById(R.id.phone);
        name=(TextView)findViewById(R.id.name);
        cname=(TextView)findViewById(R.id.cname);
        clocation=(TextView)findViewById(R.id.clocation);
        cmail=(TextView)findViewById(R.id.cmail);
        cphone=(TextView)findViewById(R.id.cphone);
        length=(TextView)findViewById(R.id.length);
        width=(TextView)findViewById(R.id.width);
        height=(TextView)findViewById(R.id.height);
        weight=(TextView)findViewById(R.id.weight);
        sd=(TextView)findViewById(R.id.sd);

        login();
        Teliver.identifyUser(new UserBuilder(""+pref.getString("messenger_name",null))
                .setUserType(UserBuilder.USER_TYPE.OPERATOR).build());
      //  System.out.println("http://www.fast-option.com/tamco/driver_new.php?method_name=booking_detail&user_id=1&booking_id="+bid+"");
        s1=(Spinner)findViewById(R.id.s1);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status=s1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button1);
        call_btn =(Button) findViewById(R.id.call_btn);
        open_map_btn = findViewById(R.id.open_map_btn);

        open_map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address!=null) {
//                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr="+address));
                    intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(Shipdetails.this,"Address Not Found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Shipdetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();
                        if(status==null)
                        {
                            ((TextView)s1.getSelectedView()).setError("Select Status");
                        }
                        else if(status.equals("Select Status")){
                            ((TextView)s1.getSelectedView()).setError("Select Status");
                        }
                        else if(status.equals("Not Delivered"))
                        {
                            Intent i = new Intent(Shipdetails.this, Status.class);
                            i.putExtra("slipno",bid);
                            i.putExtra("drs_unique_id",drs_unique_id);
                            i.putExtra("city_id",city_id);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                        else if(status.equals("Delivered"))
                        {
                            Intent i = new Intent(Shipdetails.this, Canvas.class);
                            i.putExtra("slipno",bid);
                            i.putExtra("drs_unique_id",drs_unique_id);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                        else
                        {

                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(Shipdetails.this);
        if (resultCode != ConnectionResult.SUCCESS &&
                apiAvailability.isUserResolvableError(resultCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(Shipdetails.this, resultCode,
                    900);
            dialog.setCancelable(false);
            dialog.show();
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startTrip("Delivery start.","Your order Delivery start.",""+pref.getString("messenger_name",null),""+bid);
//                Intent i = new Intent(Shipdetails.this, ActivityDriver.class);
//                i.putExtra("slipno",bid);
//                i.putExtra("drs_unique_id",drs_unique_id);
//                i.putExtra("rname",name.getText().toString());
//                i.putExtra("rmail",mail.getText().toString());
//                i.putExtra("rphone",phone.getText().toString());
//                startActivity(i);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+reph));
//                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(sIntent);

                String recname = cname.getText().toString();
                String phoneNo = phone.getText().toString();
                editor.putString("phone", phoneNo);
                editor.commit();
                if (phoneNo.trim().equals("")) {
                    lblinfo.setText("Please enter a number to call on!");
                } else {
                    Boolean isHash = false;
                    if (phoneNo.subSequence(phoneNo.length() - 1, phoneNo.length()).equals("#")) {
                        phoneNo = phoneNo.substring(0, phoneNo.length() - 1);
                        String callInfo = "tel:" + phoneNo + Uri.encode("#");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(callInfo));
                        startActivity(callIntent);
                    } else {
//                        Intent intent = new Intent(Shipdetails.this, TService.class);
//                        intent.putExtra("slipno",bid);
//
//                        intent.putExtra("drs_unique_id",drs_unique_id);
//                        intent.putExtra("bid",bid);
//                        intent.putExtra("city_id",city_id);
//
//                        startService(intent);
                        System.out.println("bimal test "+drs_unique_id);
                        String callInfo = "tel:" + phoneNo;
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(callInfo));
                        startActivity(callIntent);
                    }
                    System.out.println("Naveen rajput"+cname);
//                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
//                            // Sets the MIME type to match the Contacts Provider
//                            intent.putExtra(ContactsContract.Intents.Insert.NAME, ""+recname);
//                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, ""+phoneNo);
//                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//                            startActivity(intent);


                    }


            }


        });
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+reph));
//                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(sIntent);
            } else {

                // Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }}//end onRequestPermi
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

    private void startTrip(String title, String msg, String userId, String trackingId) {
        try {
            if (trackingId.isEmpty()) {
                // Utils.showSnack(viewRoot, getString(R.string.text_enter_valid_id));
                Toast.makeText(Shipdetails.this,"Enter valid shipment", Toast.LENGTH_LONG).show();
                }
            else if (!isNetConnected(Shipdetails.this)) {
                    // Utils.showSnack(viewRoot, getString(R.string.text_no_internet))
                Toast.makeText(Shipdetails.this,"No Internet.", Toast.LENGTH_LONG).show();
                }
            else{
                        //  dialogBuilder.dismiss();
                        TripBuilder builder = new TripBuilder(trackingId);
                        if (!userId.isEmpty()) {
                            PushData pushData = new PushData(userId.split(","));
                            pushData.setPayload(msg);
                            pushData.setMessage(title);
                            builder.withUserPushObject(pushData);
                        }
                        Teliver.startTrip(builder.build());
                        Toast.makeText(Shipdetails.this,R.string.txt_wait_start_trip, Toast.LENGTH_LONG).show();
                        // Utils.showSnack(viewRoot, getString(R.string.txt_wait_start_trip));
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            public static boolean isNetConnected(Context context) {
                ConnectivityManager conMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                return conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected();
            }

    public void login()
    {
        System.out.println(Constant.URL+"?method_name=booking_detail&user_id="+id+"&booking_id="+bid+"");
        mProgressDialog = ProgressDialog.show(this, "Loading..", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=booking_detail&user_id="+id+"&booking_id="+bid+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                System.out.println("response is - "+response);
                try {
                    JSONObject jsonobject = new JSONObject(response);
                   // JSONObject jsonarray =new JSONObject();
                 //   JSONObject jsonobject = response;



                    //String name = jsonobject.getString("delivered");
                    address = jsonobject.getString("locdata");
                    awbno.setText("AWB No.:"+jsonobject.getString("slip_no"));
                    refno.setText("Ref No.:"+jsonobject.getString("booking_id"));
                    cal.setText(""+jsonobject.getString("req_delevery_time"));
                    date2.setText(""+jsonobject.getString("time_slot"));
                    name.setText(""+jsonobject.getString("reciever_name"));
                    location.setText(""+jsonobject.getString("reciever_address"));
                    mail.setText(""+jsonobject.getString("reciever_email"));
                    phone.setText(""+jsonobject.getString("reciever_phone"));
                    reph=jsonobject.getString("reciever_phone");
                    cname.setText(""+jsonobject.getString("sender_name"));
                    clocation.setText(""+jsonobject.getString("sender_address"));
                    cmail.setText(""+jsonobject.getString("sender_email"));
                    cphone.setText(""+jsonobject.getString("sender_phone"));
                    sd.setText(""+jsonobject.getString("schedule_date"));
                    area_street.setText(""+jsonobject.getString("area_street"));
                    bookingdate.setText("Booking Date / Time:"+jsonobject.getString("entrydate"));
                   city_id=jsonobject.getString("destination");
                    parcel.setText("Parcel Description:");
                    shipment.setText("Shipment Type:"+jsonobject.getString("mode"));
                    cod.setText("COD Amount: "+jsonobject.getString("total_cod_amt")+"(SAR)");
                    pod.setText("POD:"+jsonobject.getString("code"));
                //    drs_unique_id=jsonobject.getString("drs_unique_id");
                    JSONArray jk=jsonobject.getJSONArray("diamention");
                    if(jk.length()>0) {
                        JSONObject jk1 = jk.getJSONObject(0);
                        slipno = jk1.getString("slip_no");
                        length.setText("LENGTH: " + jk1.getString("length"));
                        width.setText("WIDTH: " + jk1.getString("width"));
                        height.setText("HEIGHT: " + jk1.getString("height"));
                        weight.setText("WEIGHT: " + jk1.getString("wieght"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(Shipdetails.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ""+id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private boolean checkPermissions() {
        int result;
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    public void onRequestPermissionsResult1(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
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
}
