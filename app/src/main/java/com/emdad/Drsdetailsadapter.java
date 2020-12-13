package com.emdad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.MultiFormatWriter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Drsdetailsadapter extends RecyclerView.Adapter<Drsdetailsadapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Pickupmodel> rogerModelArrayList;
    Context mcontext;
    public Drsdetailsadapter(Context ctx, ArrayList<Pickupmodel> rogerModelArrayList){

        inflater = LayoutInflater.from(ctx);
        mcontext=ctx;
        this.rogerModelArrayList = rogerModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.drsdetail, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //Picasso.load(rogerModelArrayList.get(position).getImg()).into(holder.iv);
        if(rogerModelArrayList.get(position).getStatus().equals("N"))
        {
            holder.name.setText("Not Processed");
        }
        if(rogerModelArrayList.get(position).getStatus().equals("Y"))
        {
            holder.name.setText(" Delivered");
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        holder.country.setText(rogerModelArrayList.get(position).getIddd());
        holder.city.setText("DATE : "+parseDateToddMMyyyy(rogerModelArrayList.get(position).getDate()));
        holder.id.setText(rogerModelArrayList.get(position).getId());
        holder.refno.setText("Ref. No. : "+rogerModelArrayList.get(position).getRefno());
        holder.attempt.setText("Delivery Attempt : "+rogerModelArrayList.get(position).getAttempt());
        holder.mode.setText("Payment mode : "+rogerModelArrayList.get(position).getMode());
        holder.csa.setText(""+rogerModelArrayList.get(position).getCsa());
        Picasso.with(mcontext)
                .load("http://www.emdadsa.net/tamco/www/img/boxes.jpg")
                .into(holder.iv);
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(rogerModelArrayList.get(position).getDrs_unique_id(), BarcodeFormat.QR_CODE,300,250);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            holder.iv.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
        System.out.println("bid is"+rogerModelArrayList.get(position).getIddd());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Shipdetails.class);
                System.out.println("bid is"+rogerModelArrayList.get(position).getIddd());
                intent.putExtra("bid",""+rogerModelArrayList.get(position).getIddd());
                intent.putExtra("drs_unique_id",""+rogerModelArrayList.get(position).getDrs_unique_id());
                intent.putExtra("city_id",""+rogerModelArrayList.get(position).getPickup_delivered());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
                ((Activity)mcontext).finish();
                ((Activity)  mcontext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
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
    @Override
    public int getItemCount() {
        return rogerModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView country, name, city,id,attempt,refno,csa,mode;
        ImageView iv;
        Button button;
        public MyViewHolder(View itemView) {
            super(itemView);

            country = (TextView) itemView.findViewById(R.id.idddd);
            name = (TextView) itemView.findViewById(R.id.status);
            city = (TextView) itemView.findViewById(R.id.date);
            id = (TextView) itemView.findViewById(R.id.id);
            attempt = (TextView) itemView.findViewById(R.id.attempt);
            mode = (TextView) itemView.findViewById(R.id.mode);
            refno = (TextView) itemView.findViewById(R.id.refno);
            csa = (TextView) itemView.findViewById(R.id.csa);
            iv = (ImageView) itemView.findViewById(R.id.profile_image);
            button=(Button)itemView.findViewById(R.id.button);
        }

    }

}

