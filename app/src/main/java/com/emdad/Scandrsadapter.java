package com.emdad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Scandrsadapter extends RecyclerView.Adapter<Scandrsadapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Pickupmodel> rogerModelArrayList;
    Context mcontext;
    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    public Scandrsadapter(Context ctx, ArrayList<Pickupmodel> rogerModelArrayList){

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
        sampleDB = mcontext.openOrCreateDatabase("foodbite",mcontext.MODE_PRIVATE, null);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.scandrsadapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //Picasso.load(rogerModelArrayList.get(position).getImg()).into(holder.iv);
        holder.awbnumberno.setText(""+ Html.fromHtml(rogerModelArrayList.get(position).getName()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.Delwine(rogerModelArrayList.get(position).getId());
                Intent myactivity = new Intent(mcontext.getApplicationContext(),Scanpickup.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mcontext.getApplicationContext().startActivity(myactivity);
                ((Activity)mcontext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                ((Activity)mcontext).finish();
            }
        });
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

        TextView awbnumberno, name, city,id;
        ImageView iv;
         Button delete;
        public MyViewHolder(View itemView) {
            super(itemView);

            awbnumberno = (TextView) itemView.findViewById(R.id.awbnumberno);
            delete = (Button) itemView.findViewById(R.id.delete);

        }

    }

}
