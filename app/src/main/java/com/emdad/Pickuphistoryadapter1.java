package com.emdad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.MultiFormatWriter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pickuphistoryadapter1 extends RecyclerView.Adapter<Pickuphistoryadapter1.MyViewHolder> {

  private LayoutInflater inflater;
  private ArrayList<Pickupmodel> rogerModelArrayList;
  Context mcontext;


  public Pickuphistoryadapter1(Context ctx, ArrayList<Pickupmodel> rogerModelArrayList) {

    inflater = LayoutInflater.from(ctx);
    mcontext = ctx;
    this.rogerModelArrayList = rogerModelArrayList;



  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.pickuphistor, parent, false);
    MyViewHolder holder = new MyViewHolder(view);

    return holder;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    //Picasso.load(rogerModelArrayList.get(position).getImg()).into(holder.iv);
    if (rogerModelArrayList.get(position).getStatus().equals("N")) {
      holder.name.setText("Not Delivered");
    }
    if (rogerModelArrayList.get(position).getStatus().equals("Y")) {
      holder.name.setText(" Delivered");
    }
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    holder.country.setText(rogerModelArrayList.get(position).getIddd());
    holder.city.setText(parseDateToddMMyyyy(rogerModelArrayList.get(position).getDate()));
    holder.id.setText(rogerModelArrayList.get(position).getId());
    Picasso.with(mcontext)
            .load("http://www.emdadsa.net/tamco/www/img/boxes.jpg")
            .into(holder.iv);
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(rogerModelArrayList.get(position).getDrs_unique_id(), BarcodeFormat.CODE_39, 200, 150);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            holder.iv.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
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

  class MyViewHolder extends RecyclerView.ViewHolder {

    TextView country, name, city, id;
    ImageView iv;

    public MyViewHolder(View itemView) {
      super(itemView);

      country = (TextView) itemView.findViewById(R.id.idddd);
      name = (TextView) itemView.findViewById(R.id.status);
      city = (TextView) itemView.findViewById(R.id.date);
      id = (TextView) itemView.findViewById(R.id.id);
      iv = (ImageView) itemView.findViewById(R.id.profile_image);
    }

  }
  @Override
  public int getItemViewType(int position) {
    return rogerModelArrayList.get(position) != null ? 1 : 0;
  }


}