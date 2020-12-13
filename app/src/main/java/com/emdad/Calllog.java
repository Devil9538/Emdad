package com.emdad;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Calllog extends AppCompatActivity {
    ArrayList<Model123> pickupmodelslist;
    private DatabaseAdapter dbHelper;
    SQLiteDatabase sampleDB;
    private RecyclerView recyclerView;
    Adapter_log adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler);
        pickupmodelslist= new ArrayList<>();
        dbHelper = new DatabaseAdapter(Calllog.this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        sampleDB = this.openOrCreateDatabase("drs",Calllog.this.MODE_PRIVATE, null);
        adapter=new Adapter_log(this,pickupmodelslist);
        dataset();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    public void dataset()
    {
        System.out.println("dataset");
        pickupmodelslist.clear();
        try {
            Cursor mCursor = dbHelper.getAllcart_call();
            Cursor mCursor1;
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {
                      System.out.println("m cursor 1"+mCursor.getString(1));
                    Model123 ii = new Model123();
                    ii.setPathofvoice(mCursor.getString(1));
                    ii.setStarttime(mCursor.getString(2));
                    ii.setEndtime(mCursor.getString(3));
                    ii.setNom(mCursor.getString(7));
                    ii.setType(mCursor.getString(6));

                    pickupmodelslist.add(ii);


                    mCursor.moveToNext();
                }
            }

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            adapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            System.out.println("dataset"+e);
        }
    }

}
