package com.emdad;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_log extends RecyclerView.Adapter<Adapter_log.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Model123> rogerModelArrayList;
    Context mcontext;
    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    String dura="";
    public Adapter_log(Context ctx, ArrayList<Model123> rogerModelArrayList){

        inflater = LayoutInflater.from(ctx);
        mcontext=ctx;
        this.rogerModelArrayList = rogerModelArrayList;
        dbHelper = new DatabaseAdapter(mcontext);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        sampleDB = mcontext.openOrCreateDatabase("drs",mcontext.MODE_PRIVATE, null);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.log, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //Picasso.load(rogerModelArrayList.get(position).getImg()).into(holder.iv);
        holder.awbnumberno.setText(rogerModelArrayList.get(position).getNom());
        holder.type.setText(rogerModelArrayList.get(position).getType());

        holder.awbnumberno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(rogerModelArrayList.get(position).getPathofvoice());
                    mPlayer.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlayer.start();
            }
        });



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");

        try {
            Date date1 = simpleDateFormat.parse(""+rogerModelArrayList.get(position).getStarttime());
            Date date2 = simpleDateFormat.parse(""+rogerModelArrayList.get(position).getEndtime());
          printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.duration.setText("Duration: "+dura);
    }

    @Override
    public int getItemCount() {
        return rogerModelArrayList.size();
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView awbnumberno, duration, type,id;
        ImageView iv;
        Button delete;
        public MyViewHolder(View itemView) {
            super(itemView);

            awbnumberno = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            duration = (TextView) itemView.findViewById(R.id.duration);


        }

    }
    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        dura=""+elapsedMinutes+"hour "+elapsedMinutes+"min "+elapsedSeconds+"sec";
    }
}

