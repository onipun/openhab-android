package org.openhab.habdroid;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import database.LoginDbSchema.LoginBaseHelper;
import database.LoginDbSchema.LoginDbSchema;

import static database.LoginDbSchema.LoginBaseHelper.CREATE_REMINDER;
import static database.LoginDbSchema.LoginBaseHelper.CREATE_SWITCH;
import static database.LoginDbSchema.LoginBaseHelper.SQL_DELETE_REMINDER;
import static database.LoginDbSchema.LoginBaseHelper.SQL_DELETE_SWITCH;
import static database.LoginDbSchema.LoginDbSchema.Cols.CATEGORY_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.DATE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.ID_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.STATUS_SWITCH;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_ID;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_NAME;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_STATUS;
import static database.LoginDbSchema.LoginDbSchema.Cols.TIME_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.TITLE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.LoginTable.TABLE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.LoginTable.TABLE_SWITCH;

public class LoginDbInitiate {
    private static LoginDbInitiate sLoginDbInitiate;
    private Context mContext;

    //mDatabase == mDatabase
    private SQLiteDatabase mDatabase;
    private static final String TAG = LoginDbInitiate.class.getSimpleName();


    public static LoginDbInitiate getsLoginDbInitiate(Context context) {
        if (sLoginDbInitiate == null){
            sLoginDbInitiate = new LoginDbInitiate(context);
        }
        return sLoginDbInitiate;
    }

    private LoginDbInitiate(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LoginBaseHelper(mContext).getWritableDatabase();
    }

    public boolean authentication(String email, String password){
       boolean auth = false;

       String[] whereArgs = {email};
       String[] whereArgsP = {password};

        Cursor cursor = fetchUser("Login", LoginDbSchema.Cols.EMAIL + " = ?" ,whereArgs);


        if (cursor != null && cursor.getCount() == 1) {
            cursor = fetchUser("Login",LoginDbSchema.Cols.PASSWORD + " = ? ", whereArgsP);
            if (cursor != null && cursor.getCount() == 1) {
                auth = true;
            }
        } 

        return auth ;
    }

    private Cursor fetchUser(String tableName, String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(
                tableName,
                null, // columns - null selects all columns
                whereClause, // The columns for the WHERE clause
                whereArgs, // The values for the WHERE clause
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return cursor;
    }


    public void insertNewUser(String name, String password, String email){
        ContentValues values = getContentValues(name, password, email);
        try {
            long newRowId = mDatabase.insert(LoginDbSchema.LoginTable.NAME, null, values);
            Log.d(TAG, "insertNewUser Id: " + newRowId);
        }catch (Exception e) {
            Log.d(TAG, "insertNewUser Id: " + e);
        }
    }

    private static ContentValues getContentValues(String name, String password, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoginDbSchema.Cols.NAME, name);
        contentValues.put(LoginDbSchema.Cols.PASSWORD, password);
        contentValues.put(LoginDbSchema.Cols.EMAIL, email);

        return contentValues;
    }

    //====================Labels===================

    public List<String> getAllLabels(){
        List<String> sw = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_SWITCH;

        Cursor c = mDatabase.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                sw.add(c.getString(1));
            } while (c.moveToNext());
        }

        c.close();

        return sw;
    }

    //====================Switch===================

    public long insertSwitchData(String name, String status){
        ContentValues cv = new ContentValues();
        cv.put(SWITCH_NAME, name);
        cv.put(SWITCH_STATUS, status);
        long v = mDatabase.insert(TABLE_SWITCH, null, cv);
        return v;
    }

    public Cursor getSwitchData(){
        String[] cols = {SWITCH_ID,SWITCH_NAME};
        Cursor c = mDatabase.query(TABLE_SWITCH, cols, null,
                null, null, null, null);
        return c;
    }

    public Cursor getAllSwitchData(int rowId){
        Log.d(TAG, "getAllSwitchData: enter" );
        Cursor c = null;
        Log.d(TAG, "getAllSwitchData: " + c);
        try {

            String[] cols = {SWITCH_ID,
                    SWITCH_NAME,
                    SWITCH_STATUS};

            c = mDatabase.query(TABLE_SWITCH, cols,
                    SWITCH_ID + "= " + rowId, null, null, null, null);

            Log.d(TAG, "getAllSwitchData: " + c);
            return c;

        }catch (Exception e){
            Log.d(TAG, "getAllSwitchData: error getAllSwitch" + e);
        }

        return c = null;
    }

    public long updateSwitchData(int rowId, String status){
        ContentValues cv = new ContentValues();
        cv.put(SWITCH_STATUS, status);
        
        long v = mDatabase.update(TABLE_SWITCH, cv,
                SWITCH_ID + "=" + rowId, null);
        
        return v;
    }

    public int deleteSwitchData(int rowId){
        
        int v = mDatabase.delete(TABLE_SWITCH,
                SWITCH_ID + "=" + rowId, null);
        
        return v;
    }

    public void deleteAllSwitchData(){
        
        mDatabase.execSQL(SQL_DELETE_SWITCH);
        mDatabase.execSQL(CREATE_SWITCH);
        
    }

    //====================Reminder===================

    public long insertReminderData(String category, String date, String time, String title, String status){
        Log.d(TAG, "insertReminderData: entered");
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_REMINDER, category);
        cv.put(DATE_REMINDER, date);
        cv.put(TIME_REMINDER, time);
        cv.put(TITLE_REMINDER, title);
        cv.put(STATUS_SWITCH, status);
        
        long v = mDatabase.insert(TABLE_REMINDER, null, cv);
        
        return v;
    }

    public Cursor getReminderData(){
        String[] cols = {ID_REMINDER,TITLE_REMINDER};
        
        Cursor c = mDatabase.query(TABLE_REMINDER, cols, null,
                null, null, null, null);
        return c;
    }

    public Cursor getAllReminderData(int rowId){
        String[] cols = {ID_REMINDER,
                CATEGORY_REMINDER,
                DATE_REMINDER,
                TIME_REMINDER,
                TITLE_REMINDER,
                STATUS_SWITCH};
        
        Cursor c = mDatabase.query(TABLE_REMINDER, cols,
                ID_REMINDER + "=" + rowId, null, null, null, null);
        return c;
    }

    public long updateReminderData(int rowId, String category, String date, String time, String title, String status){
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_REMINDER, category);
        cv.put(DATE_REMINDER, date);
        cv.put(TIME_REMINDER, time);
        cv.put(TITLE_REMINDER, title);
        cv.put(STATUS_SWITCH, status);
        
        long v = mDatabase.update(TABLE_REMINDER, cv,
                ID_REMINDER + "=" + rowId, null);
        
        return v;
    }

    public int deleteReminderData(int rowId){
        
        int v = mDatabase.delete(TABLE_REMINDER,
                ID_REMINDER + "=" + rowId, null);
        
        return v;
    }

    public void deleteAllReminderData(){
        
        mDatabase.execSQL(SQL_DELETE_REMINDER);
        mDatabase.execSQL(CREATE_REMINDER);
        
    }
}
