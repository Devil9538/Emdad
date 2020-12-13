package com.emdad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class Updateprofile extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageView profile_image;
    Button button;
    EditText supplier,mnumber,vnumber,cnumber,email,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        profile_image=(ImageView)findViewById(R.id.profile_image);
        button=(Button)findViewById(R.id.button);
        supplier=(EditText)findViewById(R.id.supplier);
        mnumber=(EditText)findViewById(R.id.mnumber);
        vnumber=(EditText)findViewById(R.id.vnumber);
        cnumber=(EditText)findViewById(R.id.cnumber);
        email=(EditText)findViewById(R.id.email);
        username=(EditText)findViewById(R.id.username);

        Picasso.with(this)
                .load("http://emdadsa.net/messanger_images/"+pref.getString("messanger_image",null))
                .placeholder(R.drawable.logo)
                .into(profile_image);

        username.setText(""+pref.getString("messenger_name",null));
        email.setText(""+pref.getString("email",null));
        cnumber.setText(""+pref.getString("email",null));
        vnumber.setText(""+pref.getString("vehicle_number",null));
        mnumber.setText(""+pref.getString("mobile",null));
        supplier.setText(""+pref.getString("supplier",null));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
