package com.emdad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Drshistory1 extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String id="";
    private RecyclerView recyclerView;
    ArrayList<Pickupmodel> pickupmodelslist;
    private Drshistoradapter1 adapter;
    int page=1;
    ProgressBar progressBar;
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    //private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drshistory1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler);
        pickupmodelslist= new ArrayList<>();

        adapter = new Drshistoradapter1(this,pickupmodelslist);
        login();
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    System.out.println("find visible itemis "+pastVisiblesItems);
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        Log.i("Yaeye!", "end called");

                        // Do something
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page=page+1;
                                login();
                            }
                        }, 5000);

                        loading = true;
                    }


                }
            }
        });
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
//        mProgressDialog = ProgressDialog.show(this, "DRS List", "Loading..");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=delivered_list&user_id="+id+"&page="+page+"", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                try {

                    JSONArray jsonarray = new JSONArray(response);
                    if(jsonarray.length()==0){
                        Toast.makeText(Drshistory1.this,"No records found", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Drshistory1.this, Dashboard.class);
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
                            String status = jsonobject.getString("delivery_status");
                            ii.setDate(date);
                            ii.setId(id);
                            ii.setIddd(iddd);
                            ii.setImg(img);
                            ii.setName(name);
                            ii.setStatus(status);
                            ii.setDrs_unique_id(jsonobject.getString("drs_unique_id"));
                            ii.setDrs_bar_code(jsonobject.getString("drs_unique_id"));
                            ii.setTotal_awb(jsonobject.getString("total_awb"));
                            //  ii.setPickup_delivered(jsonobject.getString("pickup_delivered"));
                            pickupmodelslist.add(ii);
                        }
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(mLayoutManager);
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
               // mProgressDialog.dismiss();
                Toast.makeText(Drshistory1.this, error.toString(), Toast.LENGTH_LONG).show();

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
}
