package com.emdad;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Status extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static ProgressDialog mProgressDialog;
    String id="",messenger_code="",city="",gh="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String bid="",status="",drs_unique_id="",slipno="",city_id="";
    private List status123,status1234,status12345;
    Spinner spinner,s2;
    EditText rdate,reason;
    Button button;
    final Calendar myCalendar = Calendar.getInstance();
    AutoCompleteTextView autoCompleteTextView;
    String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        messenger_code=pref.getString("messenger_code",null);
        city=pref.getString("city",null);
        status123 = new ArrayList<>();
        status1234 = new ArrayList<>();
        status12345 = new ArrayList<>();
        status123.add("Select Reason");
        status1234.add("Select area");
        status12345.add("Select Date");
        spinner = (Spinner) findViewById(R.id.s1);
        s2 = (Spinner) findViewById(R.id.s2);
        pickup();
        Intent ii=getIntent();
        drs_unique_id=ii.getStringExtra("drs_unique_id");
        slipno=ii.getStringExtra("slipno");
        city_id=ii.getStringExtra("city_id");
        System.out.println(drs_unique_id+"bid is"+slipno);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spin , status123);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        rdate=(EditText)findViewById(R.id.rdate);
        autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        reason=(EditText)findViewById(R.id.reason);
        button=(Button)findViewById(R.id.button);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status=spinner.getItemAtPosition(position).toString();
                if(status.equals("Customer Not in location"))
                {
                    code1();
                    rdate.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
                else if(status.equals("Wrong Area/CS Issue"))
                {
                    code1();
                    rdate.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                }
                else if(status.equals("Reschedule For Future Date"))
                {
                    code1();
                    rdate.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
                else if(status.equals("Mobile Close"))
                {
                    code1();
                    rdate.setVisibility(View.INVISIBLE);
                    s2.setVisibility(View.INVISIBLE);
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
                else if(status.equals("No Answer"))
                {
                    code1();
                    rdate.setVisibility(View.INVISIBLE);
                    s2.setVisibility(View.INVISIBLE);
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
                else if(status.equals("Customer Not in location"))
                {
                    code1();
                    rdate.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                }
                else
                {
                    code1();
                    rdate.setVisibility(View.INVISIBLE);
                    s2.setVisibility(View.INVISIBLE);
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                }
                code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String statusss=s2.getItemAtPosition(position).toString();
                rdate.setText(statusss);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
////                Date today = new Date();
////                Calendar c = Calendar.getInstance();
////                c.setTime(today);
////                c.add( Calendar.DAY_OF_WEEK, +6 ); // Subtract 6 months
////                long maxDate = c.getTime().getTime();
////                DatePickerDialog dialog = new DatePickerDialog(Status.this, new DatePickerDialog.OnDateSetListener() {
////                    @Override
////                    public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
////                        myCalendar.set(Calendar.YEAR, year);
////                        myCalendar.set(Calendar.MONTH, (month+1));
////                        myCalendar.set(Calendar.DAY_OF_MONTH, day_of_month);
////                        String myFormat = "yyyy-MM-dd";
////                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//////                        if (myCalendar.get(Calendar.DAY_OF_WEEK)!=Calendar.FRIDAY) {
//////                            rdate.setText(String.valueOf(sdf.format(myCalendar.getTime())));
//////                        }
//////                        else
//////                        {
//////                            Toast.makeText(Status.this, "Friday not selectable.", Toast.LENGTH_LONG).show();
//////                        }
////                        rdate.setText(String.valueOf(sdf.format(myCalendar.getTime())));
////                      //  rdate= String.valueOf(sdf.format(myCalendar.getTime()));
////                    }
////                }
////                ,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
////                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());// TODO: used to hide previous date,month and year
////                myCalendar.add(Calendar.YEAR, 0);
////
////                dialog.getDatePicker().setMaxDate(maxDate);// TODO: used to hide future date,month and year
////                dialog.show();
//
//
//
////                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
////                    @Override
////                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                        // TODO Auto-generated method stub
////                        myCalendar.set(Calendar.YEAR, year);
////                        myCalendar.set(Calendar.MONTH, monthOfYear);
////                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
////                        String myFormat = "yyyy-MM-dd"; // your format
////                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
////
////                        rdate.setText(sdf.format(myCalendar.getTime()));
////                    }
////
////                };
////                new DatePickerDialog(Status.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//
//

//
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Status.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );


                //  dpd.setMaxDate(maxDate);
                final Calendar minCalendar = Calendar.getInstance();
                minCalendar.setTime(new Date());
                dpd.setMinDate(minCalendar);

                final Calendar maxCalendar = Calendar.getInstance();
                maxCalendar.add(Calendar.DAY_OF_WEEK, 7);
                //picker.setMaxDate(calendar);
                dpd.setMaxDate(maxCalendar);
                Calendar sunday;
                List<Calendar> weekends = new ArrayList<>();
                int weeks = 1;

                for (int i = 0; i < (weeks * 1) ; i = i + 1) {
                    // sunday = Calendar.getInstance();
                    maxCalendar.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - maxCalendar.get(Calendar.DAY_OF_WEEK) + 1 + i));
                    // saturday = Calendar.getInstance();
                    // saturday.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - saturday.get(Calendar.DAY_OF_WEEK) + i));
                    // weekends.add(saturday);
                    weekends.add(maxCalendar);
                }
                Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
                dpd.setDisabledDays(disabledDays);
               dpd.show(getSupportFragmentManager(), "Datepickerdialog");
//

            }
        });



        Date cc = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cc);
        DateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(cc);
        //1 day
        System.out.println("Current time => " + formattedDate);
        System.out.println("finalDay" + finalDay);
        if(!finalDay.equals("Friday"))
        {
            status12345.add(formattedDate);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH, 1);
        cc = c.getTime();

        String formattedDate1 = df.format(cc);
        DateFormat format3=new SimpleDateFormat("EEEE");
        String finalDay3=format3.format(cc);
        System.out.println("Current time => " + formattedDate1);
        System.out.println("Current time => finalDay3" + finalDay3);
        if(!finalDay3.equals("Friday"))
        {
            status12345.add(formattedDate1);
        }

        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH, 1);
        cc = c.getTime();

        String formattedDate2 = df.format(cc);
        DateFormat format4=new SimpleDateFormat("EEEE");
        String finalDay4=format4.format(cc);
        System.out.println("Current time => " + formattedDate2);
        System.out.println("Current time => finalDay4" + finalDay4);
        if(!finalDay4.equals("Friday"))
        {
            status12345.add(formattedDate2);
        }

        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH, 1);
        cc = c.getTime();

        String formattedDate3 = df.format(cc);
        DateFormat format5=new SimpleDateFormat("EEEE");
        String finalDay5=format5.format(cc);
        System.out.println("Current time => " + formattedDate3);
        System.out.println("Current time => finalDay5" + finalDay5);
        if(!finalDay5.equals("Friday"))
        {
            status12345.add(formattedDate3);
        }


        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH, 1);
        cc = c.getTime();

        String formattedDate4= df.format(cc);
        DateFormat format6=new SimpleDateFormat("EEEE");
        String finalDay6=format6.format(cc);
        System.out.println("Current time => " + formattedDate4);
        System.out.println("Current time => finalDay6" + finalDay6);
        if(!finalDay6.equals("Friday"))
        {
            status12345.add(formattedDate4);
        }

        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH,1);
        cc = c.getTime();

        String formattedDate5= df.format(cc);
        DateFormat format7=new SimpleDateFormat("EEEE");
        String finalDay7=format7.format(cc);
        System.out.println("Current time => " + formattedDate5);
        System.out.println("Current time => finalDay7" + finalDay7);
        if(!finalDay7.equals("Friday"))
        {
            status12345.add(formattedDate5);
        }

        c.setTime(cc);
        c.add(Calendar.DAY_OF_MONTH,1);
        cc = c.getTime();

        String formattedDate6= df.format(cc);
        DateFormat format8=new SimpleDateFormat("EEEE");
        String finalDay8=format8.format(cc);
        System.out.println("Current time => " + formattedDate6);
        System.out.println("Current time => finalDay8" + finalDay8);
        if(!finalDay8.equals("Friday"))
        {
            status12345.add(formattedDate6);
        }

        ArrayAdapter<String> adapter34 = new ArrayAdapter<String>(this, R.layout.spin , status12345);
        adapter34.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter34);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("Select Reason"))
                {
                    ((TextView)spinner.getSelectedView()).setError("Select Status");
                }

                else if(reason.getText().toString().equals(""))
                {
                    reason.setError("Enter Reason");
                }
                else if(reason.getText().toString().length()<=10)
                {
                    reason.setError("Enter reason length less than 10 character.");
                }

                else if(status.equals("Customer Not in location"))
                {
                    if(rdate.getText().toString().equals("Select Date"))
                    {
                        ((TextView)s2.getSelectedView()).setError("Select Date");
                        // return;
                    }
                    else
                    {
                        add();
                    }
                }
                else if(status.equals("Wrong Area/CS Issue"))
                {
                    if(rdate.getText().toString().equals("Select Date"))
                    {
                        rdate.setError("Select Date");
                        // return;
                    }
                    else
                    {
                        add();
                    }
                }
                else if(status.equals("Reschedule For Future Date"))
                {
                    if(rdate.getText().toString().equals("Select Date"))
                    {
                        rdate.setError("Select Date");
                        //  return;
                    }
                    else
                    {
                        add();
                    }
                }
                else if(status.equals("Customer Not in location"))
                {
                    if(rdate.getText().toString().equals("Select Date"))
                    {
                        rdate.setError("Select Date");

                    }
                    else
                    {
                        add();
                    }
                }
                else
                {
                    add();
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
    @Override
    public void onResume() {
        super.onResume();
//        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
//
//        if(dpd != null) dpd.setOnDateSetListener(this);
    }



    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Status.this);
        dialog.setTitle( "Hello" )
                .setMessage(message)
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                }).show();
    }


    public void pickup()
    {
        System.out.println(Constant.URL+"?method_name=statusSubDetail&status=1");

        mProgressDialog = ProgressDialog.show(this, "Loading..", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=statusSubDetail&status=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                try {

                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        System.out.println("" + response);
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        status123.add(jsonobject.getString("sub_status"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(Status.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("id", ""+id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void add()
    {

        if(rdate.getText().toString().equals("Select Date"))
        {
            gh="";
        }
        else if(!rdate.getText().toString().equals("Select Date"))
        {
            gh=rdate.getText().toString();
        }
        System.out.println(Constant.URL+"?method_name=get_status_new_test&coment_status="+code+"&status=1&coment_status_other="+reason.getText().toString().replaceAll(" ","%20")+"&reschedule_date="+gh+"&slipno="+slipno+"&drs_id="+drs_unique_id+"&user_id="+id+"&area="+autoCompleteTextView.getText().toString().replaceAll(" ","%20")+"");

        mProgressDialog = ProgressDialog.show(this, "Status Change", "Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                try {

                    System.out.println(""+response);

//                    JSONObject jsonobject = new JSONObject(response);
//                    String error=jsonobject.getString("error");
//                    JSONObject jsonobject2 = new JSONObject(error);
//                    String jsonobject1 = jsonobject2.getString("comment");

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Status.this);
                    dialog.setTitle( "Status" )
                            .setMessage("Shipment Updated as not Delivered.")
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    //Toast.makeText(Status.this,"Update!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Status.this, Drsdetails.class);
                                    intent.putExtra("uid", ""+drs_unique_id);
                                    startActivity(intent);
                                    finish();
                                    onBackPressed();
                                    finish();
                                    dialoginterface.cancel();
                                }
                            }).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(Status.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if(rdate.getText().toString().equals("Select Date"))
                {
                    gh="";
                }
                else if(!rdate.getText().toString().equals("Select Date"))
                {
                    gh=rdate.getText().toString();
                }
                params.put("coment_status", ""+code);
                params.put("coment_status_other", ""+reason.getText().toString());
                params.put("status", "1");
                params.put("reschedule_date",""+gh);
                params.put("area",""+autoCompleteTextView.getText().toString());

                params.put("method_name", "get_status_new_test");
                params.put("slipno", ""+slipno);
                params.put("drs_id", ""+drs_unique_id);
                params.put("user_id", ""+id);
                params.put("coment_status", ""+code);
                params.put("coment_status", ""+code);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }



    public void code()
    {
        System.out.println("Naveen ");
        System.out.println(Constant.URL+"?method_name=get_status_code&name="+status.replaceAll(" ","%20")+"");

//        mProgressDialog = ProgressDialog.show(this, "Loading..", "Loading..");
//        mProgressDialog.setCancelable(true);
//        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=get_status_code&name="+status.replaceAll(" ","%20")+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // mProgressDialog.dismiss();
                try {
                    System.out.println("Naveen "+response);
                    code=response;
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        code=jsonobject.getString("code");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mProgressDialog.dismiss();
                Toast.makeText(Status.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("id", ""+id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void code1()
    {
        System.out.println("Naveen ");
        System.out.println(Constant.URL+"?method_name=routs&city="+city_id+"");

//        mProgressDialog = ProgressDialog.show(this, "Loading..", "Loading..");
//        mProgressDialog.setCancelable(true);
//        mProgressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=routs&city="+city_id+"" ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // mProgressDialog.dismiss();
                try {
                    System.out.println("Naveen "+response);
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        System.out.println("" + response);
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        status1234.add(jsonobject.getString("route"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Status.this,
                            android.R.layout.simple_dropdown_item_1line, status1234);
                    autoCompleteTextView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mProgressDialog.dismiss();
                Toast.makeText(Status.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("id", ""+id);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        rdate.setText(date);
    }
}
