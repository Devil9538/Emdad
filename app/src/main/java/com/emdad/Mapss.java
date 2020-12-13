package com.emdad;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Mapss extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<LatLng> points = new ArrayList<>();
    PolylineOptions lineOptions ;
    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);
    private static final LatLng ORIGIN = new LatLng(26.8473285, -80.8784857);
    private static final LatLng DESCT = new LatLng(66.04961062044963, -172.83840849265653);
    ArrayList<HashMap<String, String>> location,location1,location2,location3;
    HashMap<String, String> map,map1,map2,map3;
    GoogleMap googleMap;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;
    private Double la1 = 0.00;
    private Double lo2 = 0.00;
    List<String> ids = new ArrayList<String>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String dname="",vehicle_number="",rdate="",id="",slip_name="";
    Marker m;
    CustomInfoWindowGoogleMap customInfoWindow;
 Button button,button1;
 TextView date123,st;
    final Calendar myCalendar = Calendar.getInstance();
    ImageView backward,forward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        location=new ArrayList<HashMap<String, String>>();
        map=new HashMap<String, String>();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        dname=pref.getString("messenger_name",null);
        System.out.println("image"+pref.getString("messanger_image",null));
        id=pref.getString("id",null);
        vehicle_number=pref.getString("vehicle_number",null);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        customInfoWindow = new CustomInfoWindowGoogleMap(Mapss.this);
        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button1);
        date123=(TextView)findViewById(R.id.date);
        st=(TextView)findViewById(R.id.st);
        backward=(ImageView)findViewById(R.id.backward);
        forward=(ImageView)findViewById(R.id.forward);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        date123.setText(dateString);
        date123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Date today = new Date();
//                Calendar c = Calendar.getInstance();
//                c.setTime(today);
//                c.add( Calendar.DAY_OF_WEEK, +6 ); // Subtract 6 months
//                long maxDate = c.getTime().getTime();
//                DatePickerDialog dialog = new DatePickerDialog(Mapss.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, (month+1));
//                        myCalendar.set(Calendar.DAY_OF_MONTH, day_of_month);
//                        String myFormat = "yyyy-MM-dd";
//                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//                        date123.setText(String.valueOf(sdf.format(myCalendar.getTime())));
//                        rdate= String.valueOf(sdf.format(myCalendar.getTime()));
//                    }
//                },myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());// TODO: used to hide previous date,month and year
//                myCalendar.add(Calendar.YEAR, 0);
//                dialog.getDatePicker().setMaxDate(maxDate);// TODO: used to hide future date,month and year
//                dialog.show();

//                DatePickerDialog dialog = new DatePickerDialog(Mapss.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        String myFormat = "yyyy-MM-dd"; // your format
//                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//
//                        date123.setText(String.valueOf(sdf.format(myCalendar.getTime())));
//                        rdate= String.valueOf(sdf.format(myCalendar.getTime()));
//                    }
//
//                     };
//                new DatePickerDialog(Mapss.this, (DatePickerDialog.OnDateSetListener) dialog, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        date123.setText(String.valueOf(sdf.format(myCalendar.getTime())));
                        rdate= String.valueOf(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(Mapss.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();




                date123.setText(rdate);
                System.out.println("rdate is:"+date123.getText().toString());
               // login();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    cal.setTime(sdf.parse(date123.getText().toString()));
                    cal.add(Calendar.DATE, -1);
                    String wantedDate = sdf.format(cal.getTime());
                    System.out.println("wantedDate is:"+wantedDate);
                    date123.setText(wantedDate);
                }catch (Exception e)
                {

                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    cal.setTime(sdf.parse(date123.getText().toString()));
                    cal.add(Calendar.DATE, +1);
                    String wantedDate = sdf.format(cal.getTime());
                    System.out.println("wantedDate is:"+wantedDate);
                    date123.setText(wantedDate);
                }catch (Exception e)
                {

                }
            }
        });
        login();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Mapss.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);

//                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//                text.setText(msg);
//
                Button dialogButton = (Button) dialog.findViewById(R.id.button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                Spinner spinCountry;
                spinCountry= (Spinner)dialog. findViewById(R.id.s1);//fetch the spinner from layout file
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Mapss.this,
                        android.R.layout.simple_spinner_item, getResources()
                        .getStringArray(R.array.slot_values));//setting the country_array to spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCountry.setAdapter(adapter);
//if you want to set any action you can do in this listener
                spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        String select_item =spinCountry.getItemAtPosition(position).toString();
                        st.setText(select_item);
                        System.out.println("csv is"+select_item);
                        //dialog.dismiss();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


                dialog.show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }





//        MarkerOptions options = new MarkerOptions();
//        options.position(new LatLng(40.722543,
//                -73.998585));
//        options.position(new LatLng(40.7057, -73.9964));
//        options.position(new LatLng(40.7064, -74.0094));
//        mMap.addMarker(options);
//        String url = getMapsApiDirectionsUrl();
//        System.out.println(url);
//        ReadTask downloadTask = new ReadTask();
//        downloadTask.execute("https://maps.googleapis.com/maps/api/directions/json?origin=40.722543,-73.998585&destination=40.722543,-73.998585&waypoints=optimize:true|40.722543,-73.998585|40.7057,-73.9964|40.7064,-74.0094&sensor=false&key=AIzaSyAv2o5ear3waD88PCYsDGqWLNd8v6kzo0U");
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE,
//                13));
//      addMarkers();

        mMap.setMyLocationEnabled(true);



    }
    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }


    public void login()
    {
        System.out.println(Constant.URL+"?method_name=locationdeliveryMap&user_id="+id+"&date="+date123.getText().toString()+"&timeslote="+st.getText().toString().replaceAll(" ","%20")+"");
        map.clear();
//        mProgressDialog = ProgressDialog.show(this, "Pick Up List", "Loading..");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=locationdeliveryMap&user_id="+id+"&date="+date123.getText().toString()+"&timeslote="+st.getText().toString().replaceAll(" ","%20")+"", new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                System.out.println(response);
              //  mProgressDialog.dismiss();
                //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                try {
                    StringBuilder result = new StringBuilder();
                    JSONObject jj=new JSONObject(response);
                    String error=jj.getString("startpoint");
                    JSONObject jsonobject23 = new JSONObject(error);
                    //JSONObject jsonobject2 = new JSONObject(error);
                    la1= Double.parseDouble(jsonobject23.getJSONObject("0").getString("latitute"));
                    lo2= Double.parseDouble(jsonobject23.getJSONObject("0").getString("longitute"));
                    JSONArray jj1=jj.getJSONArray("path");
                    StringBuilder result1 = new StringBuilder();
                    for (int i=0;i<jj1.length();i++)
                    {
                        JSONObject kk=jj1.getJSONObject(i);
                      //  System.out.println(kk.getString("lat")+""+kk.getString("longtitude"));

                        Double lat= Double.parseDouble(kk.getString("lat"));
                        Double lng= Double.parseDouble(kk.getString("longtitude"));

                        map = new HashMap<String, String>();
                        map.put("LocationID",kk.getString("location"));
                        map.put("Latitude", kk.getString("lat"));
                        map.put("Longitude", kk.getString("longtitude"));
                        map.put("LocationName", kk.getString("slipno"));
                        map.put("delivered", kk.getString("delivered"));
                        slip_name=kk.getString("slipno");
                        String fg=kk.getString("lat")+","+kk.getString("longtitude");
                        ids.add(fg);
//                        result1.append(kk.getString("lat")+","+kk.getString("longtitude"));
//                        result1.append("|");
                        location.add(map);
                    }

                   // String csv= result1.length() > 0 ? result1.substring(0, result1.length() - 1): "";


                   String csv =android.text.TextUtils.join("|", ids);
                    System.out.println("csv is"+csv);
                    for (int i = 0; i < location.size(); i++) {
                        Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
                        Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
                        String name = location.get(i).get("LocationName").toString();

                        MarkerOptions options;
                      //  MarkerOptions options = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
                        if(location.get(i).get("delivered").toString().equals("Y")) {
                            options  = new MarkerOptions().position(new LatLng(Latitude, Longitude))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.oran)).title(location.get(i).get("LocationName").toString());
                            //  marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        }
                        else
                        {
                             options = new MarkerOptions().position(new LatLng(Latitude, Longitude))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)).title(location.get(i).get("LocationName").toString());
                        }
                        final InfoWindowData info = new InfoWindowData();
                        info.setDname(dname);
                        info.setVname(vehicle_number);
                        info.setDname(location.get(i).get("LocationName").toString());

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                View myContentView = Mapss.this.getLayoutInflater().inflate(R.layout.info, null);
                                TextView tvTitle = ((TextView) myContentView.findViewById(R.id.dname));
                                tvTitle.setText("Driver:"+dname);
                                TextView tvSnippet = ((TextView) myContentView.findViewById(R.id.vname));
                                tvSnippet.setText("Vehicle No.:"+vehicle_number);
                                TextView ship = ((TextView) myContentView.findViewById(R.id.slipno));
                                ship.setText("Shipment No.:"+marker.getTitle());
                                ImageView ii = ((ImageView) myContentView.findViewById(R.id.imageView));
                                Picasso.with(Mapss.this)
                                        .load("http://tamco.sa/messanger_images/"+pref.getString("messanger_image",null))
                                        .placeholder(R.drawable.logo)
                                        .into(ii);
                                return myContentView;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        m=mMap.addMarker(options);

                       // m.setTag(info);
                        m.showInfoWindow();

                    }
                    String url = getMapsApiDirectionsUrl();
                    System.out.println(url);
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute("https://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo2+"&destination="+la1+","+lo2+"&waypoints=optimize:true|"+csv+"&sensor=false&key=AIzaSyCwHj-g8i7-YgpbPzxNalsE1LtD9y_CfOs");
                   System.out.println("https://maps.googleapis.com/maps/api/directions/json?origin="+location.get(0).get("Latitude").toString()+","+location.get(0).get("Longitude").toString()+"&destination="+location.get(0).get("Latitude").toString()+","+location.get(0).get("Longitude").toString()+"&waypoints=optimize:true|"+csv+"&sensor=false&key=AIzaSyCwHj-g8i7-YgpbPzxNalsE1LtD9y_CfOs");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location.get(0).get("Latitude").toString()),  Double.parseDouble(location.get(0).get("Longitude").toString())),
                            13));
                    MarkerOptions options;
                    if (mMap != null) {
                        for (int i = 0; i < location.size(); i++) {
                            Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
                            Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
                            System.out.println(Latitude + "rfr" + Longitude);
                            String name = location.get(i).get("LocationName").toString();
                            slip_name=location.get(i).get("LocationName").toString();
                            final InfoWindowData info = new InfoWindowData();
                            info.setDname(dname);
                            info.setVname(vehicle_number);
                            info.setDname(name);
                          //  customInfoWindow = new CustomInfoWindowGoogleMap(Mapss.this);
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    View myContentView = Mapss.this.getLayoutInflater().inflate(R.layout.info, null);
                                    TextView tvTitle = ((TextView) myContentView.findViewById(R.id.dname));
                                    tvTitle.setText("Driver:"+dname);
                                    TextView tvSnippet = ((TextView) myContentView.findViewById(R.id.vname));
                                    tvSnippet.setText("Vehicle No.:"+vehicle_number);
                                    TextView ship = ((TextView) myContentView.findViewById(R.id.slipno));
                                    ship.setText("Shipment No.:"+marker.getTitle());
                                    ImageView ii = ((ImageView) myContentView.findViewById(R.id.imageView));
                                    Picasso.with(Mapss.this)
                                            .load("https:/www.emdadsa.net/messanger_images/"+pref.getString("messanger_image",null))
                                            .placeholder(R.drawable.logo)
                                            .into(ii);
                                    return myContentView;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    return null;
                                }
                            });
                            if(location.get(i).get("delivered").toString().equals("Y")) {

                                m = mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.oran)).title(location.get(i).get("LocationName").toString()));
                            }else
                            {

                                m = mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)).title(location.get(i).get("LocationName").toString()));
                            }
                          //  m.setTag(info);
                            m.showInfoWindow();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
          //   ''   mProgressDialog.dismiss();
                Toast.makeText(Mapss.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public String getLocationFromAddress(String strAddress) {
        System.out.println("Naveen is in function");
        Geocoder coder = new Geocoder(Mapss.this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            System.out.println(lat + "," + lng);
            return lat + "," + lng;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
                + "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
                + BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
                + WALL_STREET.longitude;
        String OriDest = "origin="+ORIGIN.latitude+","+ORIGIN.longitude+"&destination="+DESCT.latitude+","+DESCT.longitude;

        String sensor = "sensor=false";
        String params = OriDest+"&"+waypoints+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+params+"&key=AIzaSyCwHj-g8i7-YgpbPzxNalsE1LtD9y_CfOs";
        return url;
    }

    private void addMarkers() {
        if (mMap != null) {
//            for (int i = 0; i < location.size(); i++) {
//                Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
//                Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
//                System.out.println(Latitude+"rfr"+Longitude);
//                String name = location.get(i).get("LocationName").toString();
//                mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude))
//                        .title(""+name));
//
//            }
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
               // System.out.println("routes is "+routes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                   System.out.println(lng+"jhfcuwejhweh"+lng);
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(15);
                polyLineOptions.color(Color.BLUE);
            }
            if (polyLineOptions != null){
                mMap.addPolyline(polyLineOptions);
            }
          //  mMap.addPolyline(polyLineOptions);
        }
    }
}
