package com.emdad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Drslist extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String id="";
    private RecyclerView recyclerView;
    ArrayList<Pickupmodel> pickupmodelslist;
    private Drshistoryadapter adapter;
    private static ProgressDialog mProgressDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drslist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       setTitle("DRS List");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        recyclerView = findViewById(R.id.recycler);
        pickupmodelslist= new ArrayList<>();

        adapter = new Drshistoryadapter(this,pickupmodelslist);
        login();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
//        mSwipeRefreshLayout.post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                mSwipeRefreshLayout.setRefreshing(true);
//
//                // Fetching data from server
//                adapter = new Drshistoryadapter(Drslist.this,pickupmodelslist);
//                login();
//            }
//        });
       // mSwipeRefreshLayout.
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



    public void login()
    {
        mProgressDialog = ProgressDialog.show(this, "DRS List", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=drs_list&user_id="+id+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                try {
                    pickupmodelslist.clear();
                    JSONArray jsonarray = new JSONArray(response);
                    if(jsonarray.length()==0){
                        Toast.makeText(Drslist.this,"No records found", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Drslist.this, Dashboard.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }else {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            Pickupmodel ii = new Pickupmodel();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("delivered");
                            String id = jsonobject.getString("id");
                            String img = jsonobject.getString("drs_bar_image");
                            String iddd = jsonobject.getString("shipment_id");
                            String date = jsonobject.getString("drs_date");
                            String status = jsonobject.getString("delivered");
                            ii.setDate(date);
                            ii.setId(id);
                            ii.setIddd(iddd);
                            ii.setImg(img);
                            ii.setName(name);
                            ii.setStatus(status);
                            ii.setDrs_unique_id(jsonobject.getString("drs_unique_id"));
                            ii.setDrs_bar_code(jsonobject.getString("drs_unique_id"));
                            ii.setTotal_awb(jsonobject.getString("total_awb"));
                            ii.setPickup_delivered(jsonobject.getString("pickup_delivered"));
                            pickupmodelslist.add(ii);
                        }
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        adapter.notifyDataSetChanged();

                        System.out.println("response is : " + response);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(Drslist.this, error.toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onRefresh() {
        adapter = new Drshistoryadapter(this,pickupmodelslist);
        login();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
