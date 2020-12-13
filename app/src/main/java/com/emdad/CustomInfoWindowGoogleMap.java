package com.emdad;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView vname = view.findViewById(R.id.vname);


        TextView slipno = view.findViewById(R.id.slipno);




        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        name_tv.setText(infoWindowData.getDname());

        vname.setText(infoWindowData.getVname());
        slipno.setText(infoWindowData.getSlipno());



        return view;
    }
}
