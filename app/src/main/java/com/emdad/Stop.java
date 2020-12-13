package com.emdad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Stop extends AppCompatActivity {
    EditText edittext;
    ImageView speedView1, speedView;
    Button button;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String picturePath = "",id="";
    File finalFile;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,  Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        String rationale = "Please provide permissions";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                // do your task.
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//
//            }
//        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edittext = (EditText) findViewById(R.id.edittext);
        speedView1 = (ImageView) findViewById(R.id.speedView1);
        speedView = (ImageView) findViewById(R.id.speedView);
        button = (Button) findViewById(R.id.button);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        speedView1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext.getText().toString().equals("")) {
                    edittext.setError("Enter Start KM");
                } else if (picturePath.equals("")) {
                    Toast.makeText(Stop.this, "Pick Image", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        upload();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);

            } else {

                // Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }//end onRequestPermi


    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

                if (resultCode == RESULT_OK && requestCode==100) {
                    Uri selectedImage = imageReturnedIntent.getData();

//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    picturePath = cursor.getString(columnIndex);
//                    System.out.println("naveen rajput"+picturePath);


                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    speedView.setImageBitmap(photo);
                    //  knop.setVisibility(Button.VISIBLE);


                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    finalFile = new File(getRealPathFromURI(tempUri));
                    picturePath = String.valueOf(finalFile);
                    System.out.println(finalFile);
                }


                if (resultCode == RESULT_OK && requestCode==101) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    System.out.println("naveen rajput" + picturePath);
                }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void upload() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(Constant.URL+"?method_name=add_stop_km&user_id="+id+"&start_km_add=" + edittext.getText().toString() + "");
        MultipartEntity mpEntity = new MultipartEntity();
        //Path of the file to be uploaded
        String filepath = "";
        System.out.println("Response is " + picturePath);
        //   File file = new File(picturePath);
        mpEntity.addPart("startImg", new FileBody(finalFile));
        post.setEntity(mpEntity);
        HttpResponse response1 = client.execute(post);
        //Get the response from the server
        HttpEntity resEntity = response1.getEntity();
        String Response = EntityUtils.toString(resEntity);
        Log.d("Response:", Response);
        System.out.println("Response is " + Response);
        JSONObject jsonobject = new JSONObject(Response);
        if (jsonobject.getString("status").equals("1")) {
//            Toast.makeText(Editaccount.this,"Changed!",Toast.LENGTH_LONG).show();
//            editor.putString("name",username.getText().toString());
//            editor.putString("type",type);
//            editor.putString("sex",gender);
//            editor.putString("user_pic",jsonobject.getString("user_pic"));
//            editor.commit();
            AlertDialog.Builder dialog = new AlertDialog.Builder(Stop.this);
            dialog.setTitle( "Odo Meter" )
                    .setMessage("End Km Added Successfully.")
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //Toast.makeText(Status.this,"Update!", Toast.LENGTH_LONG).show();
                            Intent ii=new Intent(Stop.this, Dashboard.class);
                            startActivity(ii);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialoginterface.cancel();
                        }
                    }).show();

        } else if (jsonobject.getString("status").equals("0")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Stop.this);
            dialog.setTitle( "Odo Meter" )
                    .setMessage("Add Start km First!")
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //Toast.makeText(Status.this,"Update!", Toast.LENGTH_LONG).show();
                            Intent ii=new Intent(Stop.this, Dashboard.class);
                            startActivity(ii);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialoginterface.cancel();
                        }
                    }).show();
        }

        // System.out.print(jsonobject.getString("status"));

    }
}