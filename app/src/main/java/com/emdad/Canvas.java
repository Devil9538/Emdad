package com.emdad;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kyanogen.signatureview.SignatureView;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Canvas extends AppCompatActivity {
    Button clear,save,takephoto,cancel;
    SignatureView signatureView;
    String path="";
    Bitmap bitmap;
    private static final String IMAGE_DIRECTORY = "/sign";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    String id="",messenger_code="",city="",name="",mobile="";
    String bid="",status="",drs_unique_id="",slipno="",city_id="",encodedString="",encodedString1="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static ProgressDialog mProgressDialog;
    EditText name1,mobile1;
    String picturePath="";
    File finalFile;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,  Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
    private boolean isSignatured = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        clear=(Button)findViewById(R.id.clear);
        save=(Button)findViewById(R.id.save);
        cancel=(Button)findViewById(R.id.cancel);
        takephoto=(Button)findViewById(R.id.takephoto);
        signatureView =  (SignatureView) findViewById(R.id.signature_view);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        id=pref.getString("id",null);
        messenger_code=pref.getString("messenger_code",null);
        city=pref.getString("city",null);
        name=pref.getString("messenger_name",null);
        mobile=pref.getString("mobile",null);
        Intent ii=getIntent();
        drs_unique_id=ii.getStringExtra("drs_unique_id");
        slipno=ii.getStringExtra("slipno");
        city_id=ii.getStringExtra("city_id");
        System.out.println("drs unique id is:"+drs_unique_id);
        name1=(EditText)findViewById(R.id.name);
        mobile1=(EditText)findViewById(R.id.mobile);
        System.out.println(slipno);
        System.out.println(drs_unique_id);
        System.out.println(mobile);
        System.out.println(name);
        System.out.println(city);
        System.out.println(messenger_code);
        System.out.println(id);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(isSignatured) {
                    signatureView.clearCanvas();
                //}
                mobile1.setText("");
                name1.setText("");
            }
        });
        signatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignatured=true;
            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_CAMERA_REQUEST_CODE);
                }
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }
                else
                {

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////                     //   startActivityForResult(takePictureIntent, 100);
////                    }
//
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, CAMERA_REQUEST);


                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = signatureView.getSignatureBitmap();
                path = saveImage(bitmap);
                if(name1.getText().toString().equals(""))
                {
                    name1.setError("Enter name.");
                }
                else if(mobile1.getText().toString().equals(""))
                {
                    mobile1.setError("Enter mobile number.");
                }
                else
                {

                    InputStream inputStream = null; //You can get an inputStream using any IO API

                    if(inputStream != null){

                        try {
                            inputStream = new FileInputStream(path);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    try {

                        if(inputStream != null){
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bytes = output.toByteArray();
                    encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
                    //  System.out.println(encodedString);
                    add();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".png");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(Canvas.this,
                    new String[]{f.getPath()},
                    new String[]{"image/png"}, null);
            fo.close();
            //  Toast.makeText(Canvas.this,"Save!",Toast.LENGTH_LONG).show();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                // Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }}//end onRequestPermi
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            try {
                Uri selectedImage = imageReturnedIntent.getData();

//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    picturePath = cursor.getString(columnIndex);
//                    System.out.println("naveen rajput"+picturePath);


                Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                //  speedView.setImageBitmap(photo);
                //  knop.setVisibility(Button.VISIBLE);


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));
                picturePath = String.valueOf(finalFile);
                System.out.println(finalFile);
            }catch (Exception e){}
        }
//        if(resultCode == RESULT_OK && requestCode==100){
//                    Uri selectedImage = imageReturnedIntent.getData();
//
////                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
////                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
////                    cursor.moveToFirst();
////                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                    picturePath = cursor.getString(columnIndex);
////                    System.out.println("naveen rajput"+picturePath);
//
//
//                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    //  speedView.setImageBitmap(photo);
//                    //  knop.setVisibility(Button.VISIBLE);
//
//
//                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                    Uri tempUri = getImageUri(getApplicationContext(), photo);
//
//                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    finalFile = new File(getRealPathFromURI(tempUri));
//                    picturePath= String.valueOf(finalFile);
//                    System.out.println(finalFile);
//                }
//
//
//                if(resultCode == RESULT_OK && requestCode==101){
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    picturePath = cursor.getString(columnIndex);
//                    System.out.println("naveen rajput"+picturePath);
//                }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void add()
    {
        try {
            System.out.println(Constant.URL + "?method_name=statusSubDetail&status=1");

//        mProgressDialog = ProgressDialog.show(this, "Status Change", "Loading..");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+"?method_name=take_sign&name="+name1.getText().toString().replaceAll(" ","%20")+"&mobile="+mobile1.getText().toString().replaceAll(" ","")+"&slip_no="+slipno+"&drs_unique_id="+drs_unique_id+"&user_id="+id+"", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mProgressDialog.dismiss();
//                try {
//
//                    System.out.println(""+response);
//
//                  Toast.makeText(Canvas.this,"Status Change",Toast.LENGTH_LONG).show();
////                    Intent intent = new Intent(Canvas.this, Drsdetails.class);
////                     intent.putExtra("uid", ""+drs_unique_id);
////                    startActivity(intent);
////                    finish();
////                     overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////                onBackPressed();
////                finish();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mProgressDialog.dismiss();
//                Toast.makeText(Canvas.this, error.toString(), Toast.LENGTH_LONG).show();
//
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                  params.put("sign_image", ""+encodedString);
//                  params.put("receiver_image", ""+encodedString1);
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constant.URL + "?method_name=take_sign&name=" + name1.getText().toString().replaceAll(" ", "%20") + "&mobile=" + mobile1.getText().toString().replaceAll(" ", "") + "&slip_no=" + slipno + "&drs_unique_id=" + drs_unique_id + "&user_id=" + id + "");
            MultipartEntity mpEntity = new MultipartEntity();
            //Path of the file to be uploaded
            String filepath = "";

            if(!path.equals("")) {
               // File file = new File(picturePath);
                // console.log("pic file " + file);
                mpEntity.addPart("sign_image", new FileBody(new File(path)));
                System.out.println("pic path is " + picturePath);
                if(!picturePath.equals("")) {
                    System.out.println("pic path is " + path);
                    mpEntity.addPart("receiver_image", new FileBody(finalFile));
                }
                System.out.println("pic path is " + path);
                post.setEntity(mpEntity);
                System.out.println("pic path is " + path);
            }
            HttpResponse response1 = client.execute(post);
            //Get the response from the server
            HttpEntity resEntity = response1.getEntity();
            String Response = EntityUtils.toString(resEntity);
          //  System.out.println("pic path is " + picturePath+""+Response);
            Log.d("Response:", Response);
            System.out.println("Response is " + Response);
            JSONObject jsonobject = new JSONObject(Response);
            if(jsonobject.getString("status").equals("1"))
            {
//            Toast.makeText(Editaccount.this,"Changed!",Toast.LENGTH_LONG).show();
//            editor.putString("name",username.getText().toString());
//            editor.putString("type",type);
//            editor.putString("sex",gender);
//            editor.putString("user_pic",jsonobject.getString("user_pic"));
//            editor.commit();
               // Toast.makeText(Canvas.this,"Status Update!",Toast.LENGTH_LONG).show();





                AlertDialog.Builder dialog = new AlertDialog.Builder(Canvas.this);
                dialog.setTitle( "Status" )

                        .setMessage("Shipment Updated as Delivered")
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                if(!path.equals("")) {
                                    File fdelete = new File(path);
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + path);
                                        } else {
                                            System.out.println("file not Deleted :" + path);
                                        }
                                    }
                                }
                                if(!picturePath.equals("")) {
                                    File fdelete = new File(picturePath);
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + picturePath);
                                        } else {
                                            System.out.println("file not Deleted :" + picturePath);
                                        }
                                    }
                                }
                                Intent ii = new Intent(Canvas.this, Drsdetails.class);

                                ii.putExtra("uid",""+drs_unique_id);
                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ii);
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                dialoginterface.cancel();
                            }
                        }).show();


            }
            else if(jsonobject.getString("status").equals("0"))
            {
                Toast.makeText(Canvas.this,"Error Try Again!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {

        }

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
}
