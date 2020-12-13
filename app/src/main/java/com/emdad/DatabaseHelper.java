package com.emdad;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Naveen on 10/24/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "drs";
    private static final int DATABASE_VERSION = 5;
    private SQLiteDatabase myDataBase1;
    static final String TABLE_WINE="cart";
    static final String TABLE_CALL="call_log";
    static final String TABLE_NEW_WINE123="scandrs";
    static final String TABLE_WINE1="Sub_cart";
    static final String TABLE_LOGIN="login";
    static final String TABLE_VENDOR_LOGIN="vendor_login";
    static final String TABLE_DELIVERY_LOGIN="delivery_login";

    private final Context myContext;
    private static String DB_PATH = "/data/data/com.agst.notjustpizzas/databases/";

    private static String DB_NAME = "drs";

    private final String SQL_NEW_KART="CREATE TABLE "+TABLE_WINE+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,slip_no VARCHAR,sender_name VARCHAR,destination VARCHAR ,reciever_phone " +
            "VARCHAR ,sender_phone VARCHAR,sender_city VARCHAR,sender_address VARCHAR,reciever_city VARCHAR,sub_id VARCHAR,vendor_id VARCHAR)";
    private final String SQL_NEW_KART123="CREATE TABLE "+TABLE_NEW_WINE123+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,slip_no VARCHAR,sender_name VARCHAR,destination VARCHAR,reciever_phone " +
            "VARCHAR,sender_phone VARCHAR,sender_city VARCHAR,sender_address VARCHAR,reciever_city VARCHAR,sub_id VARCHAR,vendor_id VARCHAR)";

    private final String SQL_NEW_LOGIN="CREATE TABLE "+TABLE_LOGIN+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,firstname VARCHAR,emailid VARCHAR,phone VARCHAR,password " +
            "VARCHAR ,address VARCHAR,token VARCHAR)";

    private final String SQL_NEW_KART1="CREATE TABLE "+TABLE_WINE1+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,pid VARCHAR,name VARCHAR,price VARCHAR,pro_id VARCHAR,reg_lar_ex_type_id VARCHAR)";

    private final String SQL_NEW_LOGIN_VENDOR="CREATE TABLE "+TABLE_VENDOR_LOGIN+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,name VARCHAR,email VARCHAR,password VARCHAR,business_name " +
            "VARCHAR,address VARCHAR,logo VARCHAR)";

    private final String SQL_NEW_LOGIN_DELIVERY="CREATE TABLE "+TABLE_DELIVERY_LOGIN+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,name VARCHAR,email VARCHAR,phone VARCHAR,address " +
            "VARCHAR,delivery_comp_id VARCHAR,last_name VARCHAR,destination VARCHAR)";
    private final String SQL_NEW_TABLE_CALL="CREATE TABLE "+TABLE_CALL+" (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,pathofvoice VARCHAR,starttime VARCHAR,endtime VARCHAR,lati " +
            "VARCHAR,longi VARCHAR,type VARCHAR,nom VARCHAR)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
        //Log.v("Database","Database Create"+DATABASE_NAME);
    }


    public void createdatabase(){

        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
            // check if we need to upgrade
            openDataBase();
            int cVersion = myDataBase1.getVersion();
            System.out.println("cVersion"+cVersion);
            if(cVersion != 2){
                onUpgrade(myDataBase1, myDataBase1.getVersion(), 2);}
            close();
        } else {


            this.getReadableDatabase();
            try {

                copyDataBase();

            } catch (IOException e) {
                Log.v("log",e.toString());
                throw new Error("Error copying database");

            }}
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase1 = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(SQL_NEW_KART);
        database.execSQL(SQL_NEW_KART1);
        database.execSQL(SQL_NEW_LOGIN);
        database.execSQL(SQL_NEW_LOGIN_VENDOR);
        database.execSQL(SQL_NEW_LOGIN_DELIVERY);
        database.execSQL(SQL_NEW_KART123);
        database.execSQL(SQL_NEW_TABLE_CALL);
        //database.execSQL(SQL_REGION);
        //database.execSQL(SQL_SUBREGION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS cart");

        database.execSQL("DROP TABLE IF EXISTS login");
        database.execSQL("DROP TABLE IF EXISTS Sub_cart");
        database.execSQL("DROP TABLE IF EXISTS vendor_login");
        database.execSQL("DROP TABLE IF EXISTS delivery_login");
        database.execSQL("DROP TABLE IF EXISTS scandrs");
        database.execSQL("DROP TABLE IF EXISTS call_log");
        onCreate(database);
    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
}
