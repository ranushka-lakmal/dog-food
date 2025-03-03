package com.example.dogfoodapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_USERS = "users";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PAYMENT_TYPE = "payment_type";

    private static final String TABLE_CATEGORIES = "categories";
    public static final String KEY_CAT_ID = "cat_id";
    public static final String KEY_CAT_NAME = "cat_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_USER_TYPE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_PAYMENT_TYPE + " TEXT" + ")";


        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Add this line
                + KEY_CAT_ID + " TEXT,"
                + KEY_CAT_NAME + " TEXT" + ")";

        db.execSQL(CREATE_CATEGORIES_TABLE);


        //add execute
        db.execSQL(CREATE_USERS_TABLE);
//        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Add this line
            + KEY_CAT_ID + " TEXT,"
            + KEY_CAT_NAME + " TEXT" + ")";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public boolean addUser(String email, String password, String userType, String location, String paymentType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_USER_TYPE, userType);
        values.put(KEY_LOCATION, location);
        values.put(KEY_PAYMENT_TYPE, paymentType);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_EMAIL};
        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public String getUserType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_TYPE},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String userType = cursor.getString(0);
            cursor.close();
            return userType;
        }
        return "";
    }

//    public boolean addCategory(String catId, String catName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_CAT_ID, catId);
//        values.put(KEY_CAT_NAME, catName);
//
//        long result = db.insert(TABLE_CATEGORIES, null, values);
//        return result != -1;
//    }

    public boolean addCategory(String catId, String catName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, catId);
        values.put(KEY_CAT_NAME, catName);

        try {
            long result = db.insertOrThrow(TABLE_CATEGORIES, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting category: " + e.getMessage());
            return false;
        }
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _id, cat_id, cat_name FROM " + TABLE_CATEGORIES, null);
    }


    public boolean updateCategory(String oldCatId, String newCatId, String newCatName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, newCatId);
        values.put(KEY_CAT_NAME, newCatName);

        int result = db.update(TABLE_CATEGORIES, values, KEY_CAT_ID + " = ?", new String[]{oldCatId});
        return result > 0;
    }

    public boolean deleteCategory(String catId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CATEGORIES, KEY_CAT_ID + " = ?", new String[]{catId});
        return result > 0;
    }

}