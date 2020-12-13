package com.emdad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.UserBuilder;


public class ActivityDriver extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private Toolbar toolbar;

    private FragmentManager fragmentManager;

    private View rootView;

    private Snackbar snackbar;

    private FragmentDriver fragmentDriver;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String awbno="",rname="",rmail="",rphone="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.ic_toolbar);
        setSupportActionBar(toolbar);
        setTitle("On the way");
      //  Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.app_name));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootView = findViewById(R.id.view_root);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Intent ii=getIntent();
        awbno=ii.getStringExtra("slipno");
        rname=ii.getStringExtra("rname");
        rmail=ii.getStringExtra("rmail");
        rphone=ii.getStringExtra("rphone");
        editor.putString("awbno",awbno);
        editor.putString("rname",rname);
        editor.putString("rphone",rphone);
        editor.putString("rmail",rmail);
        editor.commit();
        changeFragment(0);
        Teliver.identifyUser(new UserBuilder(""+pref.getString("messenger_name",null))
                .setUserType(UserBuilder.USER_TYPE.OPERATOR).build());
    }

    private void changeFragment(int caseValue) {
        if (caseValue == 0) {
            if (fragmentDriver == null)
                fragmentDriver = new FragmentDriver();
            switchView(fragmentDriver, "On the way");
        }
    }

    private void switchView(final Fragment fragment, final String title) {
        try {
            toolbar.setTitle(title);

            FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
            Fragment mTempFragment = fragmentManager.findFragmentById(R.id.view_container);

            if (!fragment.equals(mTempFragment)) {
                String className = fragment.getClass().getName();
                boolean isAdded = fragmentManager.popBackStackImmediate(className, 0);
                if (!isAdded) {
                    mFragmentTransaction.addToBackStack(className);
                    mFragmentTransaction.add(R.id.view_container, fragment, title);
                   // mTempFragment.setArguments(data);
                }
            }
            mFragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackStackChanged() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.view_container);
        if (fragment == null)
            return;
        String tag = fragment.getTag();
        toolbar.setTitle(tag);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1)
            fragmentManager.popBackStackImmediate();
        else if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            finish();
        } else {
           // snackbar = Snackbar.make(rootView, R.string.txt_press_back, 3000);
           // snackbar.show();
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode != 1)
            return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (fragmentDriver != null)
                fragmentDriver.validateTrip();
        } else{}
            //CustomToast.showToast(this, getString(R.string.text_location_permission));
    }
}
