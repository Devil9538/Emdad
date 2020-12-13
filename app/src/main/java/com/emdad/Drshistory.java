package com.emdad;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Drshistory extends AppCompatActivity {
  WebView w1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drshistory);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle("");
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    w1=(WebView)findViewById(R.id.w1);
    w1.loadUrl("http://google.com/maps");
    w1.getSettings().setJavaScriptEnabled(true);
    w1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
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
