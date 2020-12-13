package com.emdad;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TripListener;
import com.teliver.sdk.models.PushData;
import com.teliver.sdk.models.Trip;
import com.teliver.sdk.models.TripBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class FragmentDriver extends Fragment implements TripListener, View.OnClickListener {

    private Activity context;

    private LocationManager manager;

    private View viewRoot;

    private Dialog dialogBuilder;

    //private MPreference mPreference;

    private TripsAdapter mAdapter;

    private List<Trip> currentTrips;
    int PERMISSION_REQ_CODE = 1;
    TextView awbno;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String awbno1="",rname="",rmail="",rphone="";
    private String trackingId="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        Teliver.setTripListener(this);
       // mPreference = new MPreference(context);
        viewRoot = view.findViewById(R.id.view_root);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        awbno1= pref.getString("awbno",null);
        rname= pref.getString("rname",null);
        rmail= pref.getString("rmail",null);
        rphone= pref.getString("rphone",null);
        System.out.println("rname is:"+rphone);
        manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        TextView txtTripStatus = (TextView) view.findViewById(R.id.trip_status);
        awbno= (TextView) view.findViewById(R.id.awbno);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
       // String strtext = getArguments().getString("awbno");
        awbno.setText(awbno1);
        currentTrips = new ArrayList<>();
        currentTrips.addAll(Teliver.getCurrentTrips());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new TripsAdapter(context);
        mAdapter.setData(currentTrips, this);
        recyclerView.setAdapter(mAdapter);

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS &&
                apiAvailability.isUserResolvableError(resultCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(context, resultCode,
                    900);
            dialog.setCancelable(false);
            dialog.show();
        }
        txtTripStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    validateTrip();
            }
        });
    }

    public void validateTrip() {
        try {
            String provider = manager.getBestProvider(new Criteria(), true);
            if ((!TextUtils.isEmpty(provider)) &&
                    LocationManager.PASSIVE_PROVIDER.equals(provider)){}
               // Utils.showLocationAlert(context);
            else {
                trackingId=awbno1;
                sendEventPush( "2","Order out to delivery,track your order");
                startTrip("Tracking","Your order deliver track now.",
                        ""+rphone, ""+awbno1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendEventPush(final String pushMessage, String tag) {
        String[] users = new String[]{""+rphone};
        PushData pushData = new PushData(users);
        pushData.setMessage(tag);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", pushMessage);
            jsonObject.put("operator", ""+pref.getString("messenger_name",null));
            pushData.setPayload(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Teliver.sendEventPush(trackingId, pushData, tag);
    }
    private void startTrip(String title, String msg, String userId, String trackingId) {
        try {
            if (trackingId.isEmpty()){}
              //  Utils.showSnack(viewRoot, getString(R.string.text_enter_valid_id));

              //  Utils.showSnack(viewRoot, getString(R.string.text_no_internet));
            else {
             //   dialogBuilder.dismiss();
                TripBuilder builder = new TripBuilder(trackingId);
                if (!userId.isEmpty()) {
                    PushData pushData = new PushData(userId.split(","));
                    pushData.setPayload(msg);
                    pushData.setMessage(title);
                    builder.withUserPushObject(pushData);
                }
                Teliver.startTrip(builder.build());
               // Utils.showSnack(viewRoot, getString(R.string.txt_wait_start_trip));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onTripEnded(String trackingId) {
        Log.d("Driver:", "Trip Ended::" + trackingId);
        changeStatus(null, false);
    }


    private void changeStatus(String id, boolean status) {
//        mPreference.storeBoolean(Constants.IS_TRIP_ACTIVE, status);
//        mPreference.storeString(Constants.TRACKING_ID, id);
        currentTrips.clear();
        currentTrips.addAll(Teliver.getCurrentTrips());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTripError(String reason) {
        Log.d("Driver:", "Trip error: Reason: " + reason);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode != 1){
            return;}
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            validateTrip();}
        else{}
           // CustomToast.showToast(context, getString(R.string.text_location_permission));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                try {

                    Teliver.stopTrip(v.getTag().toString());
                    //Utils.showSnack(viewRoot,getString(R.string.txt_wait_stop_trip));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }


}
