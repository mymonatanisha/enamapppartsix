package com.enamnotes.enamappparttwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DBHandler extends SQLiteOpenHelper {

    // 🔹 DB Info
    private static final String DATABASE_NAME = "participantdb";
    private static final int DATABASE_VERSION = 4;
    private static final String TAG = "DBHandler";

    // 🔹 Example Table & Columns
    public static final String TABLE_NAME = "participantlist";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    // Step 2: Add GENDER column
    public static final String COLUMN_GENDER = "gender";
;

    // 🔹 Create Table SQL
    private static final String QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_GENDER + " TEXT) ";

    // Constructor
    public DBHandler(Context context) {
        super(context, getCustomDatabasePath(context), null, DATABASE_VERSION);
    }


    private static String getCustomDatabasePath(Context context) {
        File directory = context.getExternalFilesDir(null);
        String customDbPath = directory.getAbsolutePath() + "/participantdb.db";
        Log.d(TAG, "Custom Database Path: " + customDbPath);
        return customDbPath;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        // Called only once → when DB is created
        db.execSQL(QUERY);
    }

    // now creating a method to save data to database

    public void addParticipant(String participantName, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, participantName);
        values.put(COLUMN_GENDER, gender);
        db.insert(TABLE_NAME, null, values);

        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Called when DB version changes (drop old → create new)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
