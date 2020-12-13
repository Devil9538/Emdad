package com.emdad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScanDrs extends AppCompatActivity {
    LinearLayout l1,l2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    EditText edittext;
    private static ProgressDialog mProgressDialog;
    String id="",messenger_code="",city="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    ArrayList<Pickupmodel> pickupmodelslist;
    private Scandrsadapter1 adapter;
    String uid="";
    TextView slip_no,nowithcomma;
    StringBuffer output ;
    Button button;
    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
   // private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_drs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        dbHelper = new DatabaseAdapter(ScanDrs.this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        sampleDB = this.openOrCreateDatabase("drs",ScanDrs.this.MODE_PRIVATE, null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        messenger_code=pref.getString("messenger_code",null);
        city=pref.getString("city",null);
        Intent ii=getIntent();
        uid=ii.getStringExtra("scanvalue");
        edittext=(EditText)findViewById(R.id.edittext);
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        recyclerView = findViewById(R.id.recycler);
        pickupmodelslist= new ArrayList<>();
        nowithcomma=(TextView) findViewById(R.id.nowithcomma);
        output= new StringBuffer(110);
        button=(Button)findViewById(R.id.button);
        l1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }
                else
                {
                    Intent i = new Intent(ScanDrs.this, SimpleScannerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.length()>12)
                 {
//                     adapter = new Scandrsadapter(ScanDrs.this,pickupmodelslist);
//                     scandrs();
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(uid!=null)
        {
            System.out.println("uid is"+uid);
            edittext.setText(uid);
            adapter = new Scandrsadapter1(ScanDrs.this,pickupmodelslist);
            shipmentDetailforDrs();
        }
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(edittext.getText().toString().equals(""))
              {
                  edittext.setError("Enter AWB Number");
              }
              else
              {
                  adapter = new Scandrsadapter1(ScanDrs.this,pickupmodelslist);
                  shipmentDetailforDrs();
              }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowithcomma.getText().toString().equals("")){
                    Toast.makeText(ScanDrs.this, "First Pickup", Toast.LENGTH_LONG).show();
                }
                else
                {
                    pickup();
                }
            }
        });
//        adapter = new Scandrsadapter1(ScanDrs.this,pickupmodelslist);
//        dataset();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // dbHelper.DeleteAll1234();
        Intent i = new Intent(ScanDrs.this, Dashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
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

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

               // Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }}//end onRequestPermi


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                uid=result.getContents();
                Log.d("MainActivity", "Scanned");
               // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void scandrs()
    {
        mProgressDialog = ProgressDialog.show(this, "Scan Drs", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=scanDrs&slip_no="+edittext.getText().toString()+"&messanger_id=1&user_id="+id+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
          //      pickupmodelslist.clear();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String responsestatus = jsonObject.getString("status");
                    Log.d("ADebugTag", "Value: " + responsestatus);

                    if (responsestatus.equals("1")) {

                        JSONArray jsonarray =jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            Pickupmodel ii = new Pickupmodel();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("shipment_id");
                            ii.setName("SHIPMENT No.:"+name+"<br> Reciever Phone:"+jsonobject.getString("reciever_phone")+"<br> Reciever Address:"+jsonobject.getString("reciever_address")+"<br> Reciever Name:"+jsonobject.getString("reciever_name"));
                            pickupmodelslist.add(ii);
                            String ae='"' + name + '"';
                            output.append(ae+",");
                        }
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        adapter.notifyDataSetChanged();
                        System.out.println("response is : " + response);
                        nowithcomma.setText(output.toString().substring(0, output.length() - 1));

                    }
                    else if(responsestatus.equals("0"))
                    {
                        Toast.makeText(ScanDrs.this,"AWB Not Found!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ScanDrs.this, error.toString(), Toast.LENGTH_LONG).show();

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

        requestQueue.add(stringRequest);
    }




    public void scandrs1()
    {
        mProgressDialog = ProgressDialog.show(this, "Scan Drs", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=barcodescanDrs&drs_unique_id="+uid+"&messanger_id="+id+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
        //        pickupmodelslist.clear();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responsestatus = jsonObject.getString("status");
                    Log.d("ADebugTag", "Value: " + responsestatus);

                    if (responsestatus.equals("1")) {

                        JSONArray jsonarray =jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            Pickupmodel ii = new Pickupmodel();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("slip_no");
                            ii.setName("SHIPMENT No.:"+name+"<br> Reciever Phone:"+jsonobject.getString("reciever_phone")+"<br> Reciever Address:"+jsonobject.getString("reciever_address")+"<br> Reciever Name:"+jsonobject.getString("reciever_name"));
                            pickupmodelslist.add(ii);
                            String ae='"' + name + '"';
                            output.append(ae+",");
                        }
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        adapter.notifyDataSetChanged();
                        System.out.println("response is : " + response);
                        nowithcomma.setText(output.toString().substring(0, output.length() - 1));
                    }
                    else if(responsestatus.equals("0"))
                    {
                        Toast.makeText(ScanDrs.this,"AWB Not Found!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ScanDrs.this, error.toString(), Toast.LENGTH_LONG).show();

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

        requestQueue.add(stringRequest);
    }


    public void shipmentDetailforDrs()
    {
        mProgressDialog = ProgressDialog.show(this, "Sacn Drs", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=shipmentDetailforDrs&drs_unique_id="+uid+"&messanger_id="+id+"&slip_no="+edittext.getText().toString()+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
              //  pickupmodelslist.clear();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responsestatus = jsonObject.getString("status");
                    Log.d("ADebugTag", "Value: " + responsestatus);

                    if (responsestatus.equals("1")) {

                        JSONArray jsonarray =jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            Pickupmodel ii = new Pickupmodel();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("slip_no");
//                            ii.setName("SHIPMENT No.:"+name+"<br> Reciever Phone:"+jsonobject.getString("reciever_phone")+"<br> Reciever Address:"+jsonobject.getString("reciever_address")+"<br> Reciever Name:"+jsonobject.getString("reciever_name"));
//                            pickupmodelslist.add(ii);
//                            String ae='"' + name + '"';
//                            output.append(ae+",");

                            if (dbHelper.exi123(name)) {
                                Toast.makeText(ScanDrs.this, "Already Scaned!", Toast.LENGTH_LONG).show();
                            } else {
                                long id = dbHelper.DbSaveWine123("" + name, "" , "", "" + jsonobject.getString("reciever_phone"), "" , "" , "" , "", ""+jsonobject.getString("reciever_name"), ""+jsonobject.getString("reciever_address"));
                                System.out.println(" database id  is" + id);
                            }
                        }
                        dataset();
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                        adapter.notifyDataSetChanged();
//                        System.out.println("response is : " + response);
//                        nowithcomma.setText(output.toString().substring(0, output.length() - 1));
                    }
                    else if(responsestatus.equals("0"))
                    {
                        Toast.makeText(ScanDrs.this,"Shipment not exists!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ScanDrs.this, error.toString(), Toast.LENGTH_LONG).show();

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

        requestQueue.add(stringRequest);
    }


    public void dataset()
    {
        pickupmodelslist.clear();
        try {
            Cursor mCursor = dbHelper.getAllcart123();
            Cursor mCursor1;
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {
                    //  System.out.println("m cursor 1"+mCursor.getString(1));
                    Pickupmodel ii = new Pickupmodel();
                    ii.setName("SHIPMENT No.:"+mCursor.getString(1)+"<br> Reciever Phone:"+mCursor.getString(4)+"<br> Reciever Address:"+mCursor.getString(10)+"<br> Reciever Name:"+mCursor.getString(9));
                    ii.setId(mCursor.getString(1));
                    pickupmodelslist.add(ii);
                    String ae='"' + mCursor.getString(1) + '"';
                    output.append(ae+",");

                    mCursor.moveToNext();
                }
            }
            nowithcomma.setText(output.toString().substring(0, output.length() - 1));
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            adapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {

        }
    }

    public void pickup()
    {
        final String re="["+nowithcomma.getText().toString()+"]";
        System.out.println(Constant.URL+"?method_name=makedrs&awb_no="+re+"&city="+city+"&id=1&messenger_code="+messenger_code+"");
        mProgressDialog = ProgressDialog.show(this, "Drs Generated.", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=makedrs&awb_no="+re+"&city="+city+"&id="+id+"&messenger_code="+messenger_code+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                //  pickupmodelslist.clear();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String responsestatus = jsonObject.getString("drs_no");
                    Log.d("ADebugTag", "Value: " + responsestatus);


                        Toast.makeText(ScanDrs.this,"Drs Generated!"+responsestatus, Toast.LENGTH_LONG).show();

                    dbHelper.DeleteAll1234();
                    dataset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ScanDrs.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("awb_no", ""+re);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
