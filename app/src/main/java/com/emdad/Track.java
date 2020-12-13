package com.emdad;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TripListener;
import com.teliver.sdk.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class Track extends AppCompatActivity implements TripListener {
    private LocationManager manager;

    private TripsAdapter mAdapter;

    private List<Trip> currentTrips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Teliver.setTripListener(this);
        manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        currentTrips = new ArrayList<>();
        currentTrips.addAll(Teliver.getCurrentTrips());
        recyclerView.setLayoutManager(new LinearLayoutManager(Track.this));

        mAdapter = new TripsAdapter(Track.this);
        //mAdapter.setData(currentTrips,  Track.this);
        recyclerView.setAdapter(mAdapter);
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
    public void onTripStarted(Trip tripDetails) {
        Log.d("Driver:", "Trip started::" + tripDetails);
        changeStatus(tripDetails.getTrackingId(), true);
    }

    @Override
    public void onLocationUpdate(Location location) {

    }

    @Override
    public void onTripEnded(String trackingID) {
        changeStatus(null, false);
    }

    @Override
    public void onTripError(String reason) {

    }
    private void changeStatus(String id, boolean status) {
        currentTrips.clear();
        currentTrips.addAll(Teliver.getCurrentTrips());
        mAdapter.notifyDataSetChanged();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                try {

                    Teliver.stopTrip(v.getTag().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }
}
