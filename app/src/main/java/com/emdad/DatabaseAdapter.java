package com.emdad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Naveen on 10/24/2015.
 */
public class DatabaseAdapter {

    static final String TABLE_NEW_WINE="cart";
    static final String TABLE_NEW_WINE123="scandrs";
    static final String TABLE_NEW_WINE1="Sub_cart";
    static final String TABLE_NEW_LOGIN="login";
    static final String TABLE_CALL="call_log";
    static final String TABLE_VENDOR_LOGIN="vendor_login";
    static final String TABLE_DELIVERY_LOGIN="delivery_login";
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;


    public DatabaseAdapter(Context context) {
        this.context = context;
    }

    public DatabaseAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

public boolean exi(String sub_id)
{
    Cursor c=database.rawQuery("SELECT * FROM cart WHERE slip_no='"+sub_id+"'", null);
    if(c.moveToFirst())
    {
        return true;
    }
    else
    {
        return false;
    }
}
    public boolean exi123(String sub_id)
    {
        Cursor c=database.rawQuery("SELECT * FROM scandrs WHERE slip_no='"+sub_id+"'", null);
        if(c.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean exi12(String pid, String pro_id)
    {
        Cursor c=database.rawQuery("SELECT * FROM Sub_cart WHERE pid='"+pid+"' and pro_id='"+pro_id+"'", null);
        if(c.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean exi_call(String sub_id)
    {
        Cursor c=database.rawQuery("SELECT * FROM call_log WHERE pathofvoice='"+sub_id+"'", null);
        if(c.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public Cursor exi125(String pid, String pro_id)
    {
        System.out.println("SELECT * FROM Sub_cart WHERE reg_lar_ex_type_id='"+pro_id+"'");
        return database.rawQuery("SELECT * FROM Sub_cart WHERE reg_lar_ex_type_id='"+pro_id+"' ", null);
       //return c;
    }

    public Cursor exi123()
    {
        return database.rawQuery("SELECT * FROM Sub_cart", null);

    }
    public Cursor exi123_pro_sum(String pro_id)
    {
//        Cursor cur = database.rawQuery("SELECT * FROM Sub_cart where pro_id='"+pro_id+"'", null);
//        if(cur.moveToFirst())
//        {
//            return cur.getInt(3);
//        }

        return database.rawQuery("SELECT * FROM Sub_cart where pro_id='"+pro_id+"'", null);
    }
    public long DbSaveWine(
            String slip_no,
            String sender_name,
            String destination,

            String reciever_phone,
            String sender_phone,
            String sender_city,
            String sender_address,
            String reciever_city,
            String sub_id,
            String vendor_id


    ) {
        ContentValues values = new ContentValues();
        values.put("slip_no", slip_no);
        values.put("sender_name", sender_name);
        values.put("destination", destination);
        values.put("reciever_phone", reciever_phone);
        values.put("sender_phone", sender_phone);
        values.put("sender_city", sender_city);
        values.put("sender_address", sender_address);
        values.put("reciever_city", reciever_city);
        values.put("sub_id", sub_id);
        values.put("vendor_id", vendor_id);


        long id = database.insert(TABLE_NEW_WINE, null, values);
        return id;

    }
    public long Db_Save_Call_log(
            String pathofvoice,
            String starttime,
            String endtime,
            String lati,
            String longi,
            String type,
            String nom
    ) {
        ContentValues values = new ContentValues();
        values.put("pathofvoice", pathofvoice);
        values.put("starttime", starttime);
        values.put("endtime", endtime);
        values.put("lati", lati);
        values.put("longi", longi);
        values.put("type", type);
        values.put("nom", nom);
        long id = database.insert(TABLE_CALL, null, values);
        return id;
    }

    public long DbSaveWine123(
            String slip_no,
            String sender_name,
            String destination,

            String reciever_phone,
            String sender_phone,
            String sender_city,
            String sender_address,
            String reciever_city,
            String sub_id,
            String vendor_id


    ) {
        ContentValues values = new ContentValues();
        values.put("slip_no", slip_no);
        values.put("sender_name", sender_name);
        values.put("destination", destination);
        values.put("reciever_phone", reciever_phone);
        values.put("sender_phone", sender_phone);
        values.put("sender_city", sender_city);
        values.put("sender_address", sender_address);
        values.put("reciever_city", reciever_city);
        values.put("sub_id", sub_id);
        values.put("vendor_id", vendor_id);


        long id = database.insert(TABLE_NEW_WINE123, null, values);
        return id;

    }

    public long Db_Sub_Cart(
            String pid,
            String name,
            String price,
            String pro_id,
            String reg_lar_ex_type_id
    ) {
        ContentValues values = new ContentValues();
        values.put("pid", pid);
        values.put("name", name);
        values.put("price", price);
        values.put("pro_id", pro_id);
        values.put("reg_lar_ex_type_id", reg_lar_ex_type_id);
        long id = database.insert(TABLE_NEW_WINE1, null, values);
        return id;
    }

    public Cursor getAllcart() {
        return database.rawQuery("SELECT * FROM cart " , null);
    }
    public Cursor getAllcart123() {
        return database.rawQuery("SELECT * FROM scandrs " , null);
    }
    public Cursor getAllcart_call() {
        return database.rawQuery("SELECT * FROM call_log " , null);
    }
    public Cursor getAll_sub_cart() {
        return database.rawQuery("SELECT * FROM Sub_cart " , null);
    }
    public boolean Delwine(String sub_id) {
       // database.delete(TABLE_WINE_WITHDRAW, "wineid=" + wineId, null);
        return database.delete("cart", "slip_no='"+sub_id+"'", null) > 0;
    }
    public boolean Delwine123(String sub_id) {
        // database.delete(TABLE_WINE_WITHDRAW, "wineid=" + wineId, null);
        return database.delete("scandrs", "slip_no='"+sub_id+"'", null) > 0;
    }
    public boolean Del_sub_cart(String pro_id) {
        // database.delete(TABLE_WINE_WITHDRAW, "wineid=" + wineId, null);
        String sql="delete from Sub_cart where pro_id='"+pro_id+"'";
        System.out.println("sql is in : "+sql);
        return database.delete("Sub_cart", "pro_id=" + pro_id, null) > 0;
    }
    public boolean Del_sub_cart1(String pid, String pro_id) {
        // database.delete(TABLE_WINE_WITHDRAW, "wineid=" + wineId, null);
        return database.delete("Sub_cart", "pid=" + pid+" and pro_id="+pro_id, null) > 0;
    }
    public boolean DBupdate(
            String sub_id,
            String quantity
    ) {
        ContentValues values = new ContentValues();

       // values.put("sub_id",sub_id);
        values.put("quantity", quantity);
        return database.update("cart", values, "pid =" + sub_id, null) > 0;

    }

    public Cursor DeleteAll() {
        database.execSQL("DELETE FROM " + TABLE_NEW_WINE);

        return database.rawQuery("DELETE FROM "+TABLE_NEW_WINE, null);
    }
    public Cursor DeleteAll1234() {
        database.execSQL("DELETE FROM " + TABLE_NEW_WINE123);

        return database.rawQuery("DELETE FROM "+TABLE_NEW_WINE123, null);
    }
    public Cursor DeleteAll123() {
        database.execSQL("DELETE FROM " + TABLE_NEW_WINE1);

        return database.rawQuery("DELETE FROM "+TABLE_NEW_WINE1, null);
    }


    public Cursor Deletelogin() {
        database.execSQL("DELETE FROM " + TABLE_NEW_LOGIN);

        return database.rawQuery("DELETE FROM "+TABLE_NEW_LOGIN, null);
    }

    public Cursor cc()
    {
       return database.rawQuery("select * from cart",null);
    }


    public long DbSavelogin(
            String firstname,
            String emailid,
            String phone,
            String password,
            String address,
            String token

    ) {
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("emailid", emailid);
        values.put("phone", phone);
        values.put("password", password);
        values.put("address", address);
        values.put("token", token);
        long id = database.insert(TABLE_NEW_LOGIN, null, values);
        return id;

    }


    public long DbSavelogin_delivery(
            String name,
            String email,
            String phone,
            String address,
            String last_name,
            String destination,
            String delivery_comp_id


    ) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("address", address);
        values.put("last_name", last_name);
        values.put("destination", destination);
        values.put("delivery_comp_id", delivery_comp_id);

        long id = database.insert(TABLE_DELIVERY_LOGIN, null, values);
        return id;

    }




    public Cursor cclogin()
    {
        return database.rawQuery("select * from login",null);
    }
    public Cursor v_cclogin()
    {
        return database.rawQuery("select * from vendor_login",null);
    }

    public Cursor getlogin() {
        return database.rawQuery("SELECT * FROM login " , null);
    }

    public Cursor v_getlogin() {
        return database.rawQuery("SELECT * FROM vendor_login " , null);
    }
    public Cursor delivery_getlogin() {
        return database.rawQuery("SELECT * FROM delivery_login " , null);
    }

    public boolean Dbuplogin(
            String firstname,
            String emailid,
            String phone,
            String password,
            String address,
            String token

    ) {
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("emailid", emailid);
        values.put("phone", phone);
        values.put("password", password);
        values.put("address", address);
        values.put("token", token);

        return database.update("login", values, "emailid ='"+emailid+"'", null) > 0;

    }



    public boolean Dbuplogin_delivery(
            String name,
            String email,
            String phone,
            String address,
            String last_name,
            String destination,
            String delivery_comp_id

    ) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("address", address);
        values.put("last_name", last_name);
        values.put("destination", destination);
        values.put("delivery_comp_id", delivery_comp_id);
        return database.update(TABLE_DELIVERY_LOGIN, values, "email ='"+email+"'", null) > 0;

    }




    public long Db_Vendor_Savelogin(
            String name,
            String email,
            String password,
            String business_name,
            String address,
            String logo
    ) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("business_name", business_name);
        values.put("address", address);
        values.put("logo", logo);
        long id = database.insert(TABLE_VENDOR_LOGIN, null, values);
        return id;

    }



    public boolean Db_update_vendor(
            String name,
            String email,
            String password,

            String business_name,
            String address,
            String logo

    ) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("business_name", business_name);
        values.put("address", address);
        values.put("logo", logo);

        return database.update(TABLE_VENDOR_LOGIN, values, "email ='"+email+"'", null) > 0;

    }

    public Cursor cclogin_get_vendor()
    {
        return database.rawQuery("select * from vendor_login",null);
    }
    public Cursor Vendor_Deletelogin() {
        database.execSQL("DELETE FROM " + TABLE_VENDOR_LOGIN);

        return database.rawQuery("DELETE FROM "+TABLE_VENDOR_LOGIN, null);
    }

}
