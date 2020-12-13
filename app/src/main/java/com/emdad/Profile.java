package com.emdad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
  SharedPreferences pref;
  SharedPreferences.Editor editor;
  ImageView profile_image;
  TextView username,email,courname,vnumber,nnumber,suplier;
  Button button;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle("");
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    editor = pref.edit();
    profile_image=(ImageView)findViewById(R.id.profile_image);
    username=(TextView)findViewById(R.id.username);
    email=(TextView)findViewById(R.id.email);
    courname=(TextView)findViewById(R.id.courname);
    vnumber=(TextView)findViewById(R.id.vnumber);
    nnumber=(TextView)findViewById(R.id.nnumber);
    suplier=(TextView)findViewById(R.id.suplier);
    button=(Button)findViewById(R.id.button);
    Picasso.with(this)
            .load("http://www.emdadsa.net/tamco/messanger_images/"+pref.getString("messanger_image",null))
            .placeholder(R.drawable.logo)
            .into(profile_image);

    username.setText(""+pref.getString("messenger_name",null));
    email.setText(""+pref.getString("email",null));
    courname.setText(""+pref.getString("email",null));
    vnumber.setText(""+pref.getString("vehicle_number",null));
    nnumber.setText(""+pref.getString("mobile",null));
    suplier.setText(""+pref.getString("supplier",null));

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(Profile.this, Updateprofile.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
