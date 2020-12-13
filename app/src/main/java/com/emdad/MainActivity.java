package com.emdad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText email,password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
//        Intent intent = new Intent(MainActivity.this, TService.class);
//        stopService(intent);
//        stopLocationService();
        String value = pref.getString("id",null);
        if (value == null) {
            // the key does not exist
        } else {
               Intent i = new Intent(MainActivity.this, Dashboard.class);
                startActivity(i);
               finish();
        }
        button=(Button)findViewById(R.id.button);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().equals(""))
                {
                    email.setError("Enter Email");
                }
                else  if(password.getText().toString().equals(""))
                {
                    password.setError("Enter Password");
                }
//                else if (!email.getText().toString().matches(emailPattern) && email.getText().toString().length() > 0)
//                {
//                    email.setError("Invalid Email");
//                }
                else
                {
                    login();
                }


//                Intent i = new Intent(MainActivity.this, Dashboard.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private void stopLocationService() {
    }

    public void login()
{
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=login&email="+email.getText().toString()+"&password="+password.getText().toString()+"", new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
            try {
                System.out.println("response is "+response);
                JSONObject jsonObject = new JSONObject(response);
                String responsestatus = jsonObject.getString("status");
                Log.d("ADebugTag", "Value: " + response);

                if (responsestatus.equals("1")) {

                    JSONArray jj=jsonObject.getJSONArray("data");
                    JSONObject jj1=jj.getJSONObject(0);
                    editor.putString("id",jj1.getString("id"));  // Saving string
                    editor.putString("messenger_name",jj1.getString("messenger_name"));  // Saving string
                    editor.putString("messenger_code",jj1.getString("messenger_code"));  // Saving string
                    editor.putString("branch",jj1.getString("branch"));  // Saving string
                    editor.putString("city",jj1.getString("city"));  // Saving string
                    editor.putString("location_id",jj1.getString("location_id"));  // Saving string
                    editor.putString("mobile",jj1.getString("mobile"));  // Saving string
                    editor.putString("email",email.getText().toString());  // Saving string
                    editor.putString("messanger_image",jj1.getString("messanger_image"));  // Saving string
                    editor.putString("vehicle_number",jj1.getString("vehicle_number"));  // Saving string
                    editor.putString("supplier",jj1.getString("supplier"));  // Saving string
                    editor.putString("password",password.getText().toString());  // Saving string
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Login Successfully!!!", Toast.LENGTH_LONG).show();
                  //  String Id = jsonObject.getString("ID");
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                   // intent.putExtra("ID", Id);
                    startActivity(intent);
                    finish();
                   // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                } else {

                    Toast.makeText(MainActivity.this, "Wrong Credentails!!!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

        }
    }
    ) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();

            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());

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
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
