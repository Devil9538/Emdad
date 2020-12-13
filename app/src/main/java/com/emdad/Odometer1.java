package com.emdad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class Odometer1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
ImageView speedView,speedView1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //ImageView speedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odometer1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        speedView=(ImageView)findViewById(R.id.speedView);
        speedView1=(ImageView)findViewById(R.id.speedView1);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name=(TextView)header.findViewById(R.id.hname);
        TextView hemail=(TextView)header.findViewById(R.id.hemail);
        ImageView imageView=(ImageView)header.findViewById(R.id.imageView);
        name.setText(""+pref.getString("messenger_name",null));
        hemail.setText(""+pref.getString("email",null));
        // name.setText(""+pref.getString("email",null));
        //  http://www.fast-option.com/tamco/messanger_images/1523961736.jpeg

        Picasso.with(this)
                .load("http://www.emdadsa.net/messanger_images/"+pref.getString("messanger_image",null))
                .placeholder(R.drawable.logo)
                .into(imageView);
        speedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Odometer1.this, Start123.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        speedView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Odometer1.this, Stop.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(Odometer1.this, Dashboard.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(Odometer1.this, Mapss.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        else if (id == R.id.drs_list) {
            Intent i = new Intent(Odometer1.this, Drslist.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.scan_drs) {
            Intent i = new Intent(Odometer1.this, ScanDrs.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.scan_pickup) {
            Intent i = new Intent(Odometer1.this, Scanpickup.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        else if (id == R.id.nav_slideshow) {
//            Intent i = new Intent(Odometer1.this, Odometer1.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.nav_tools) {
            editor.clear();
            editor.commit();
            Intent i = new Intent(Odometer1.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
