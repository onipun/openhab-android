package database.LoginDbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.openhab.habdroid.LoginDbInitiate;

import static database.LoginDbSchema.LoginDbSchema.Cols.CATEGORY_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.DATE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.ID_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.NAME;
import static database.LoginDbSchema.LoginDbSchema.Cols.STATUS_SWITCH;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_ID;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_NAME;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_STATUS;
import static database.LoginDbSchema.LoginDbSchema.Cols.TIME_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.Cols.TITLE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.LoginTable.TABLE_REMINDER;
import static database.LoginDbSchema.LoginDbSchema.LoginTable.TABLE_SWITCH;


public class LoginBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "openHab.db";
    private static final String TAG = LoginBaseHelper.class.getSimpleName();

    //Create table Login
    private static final String SQL_CREATE_LOGIN =
            "create table " + LoginDbSchema.LoginTable.NAME + "(" +
            "id integer primary key autoincrement, " +
            LoginDbSchema.Cols.NAME + " TEXT," +
            LoginDbSchema.Cols.EMAIL + " TEXT," +
            LoginDbSchema.Cols.PASSWORD + " TEXT)";

    //Create Table SwStatus
    public static final String CREATE_SWITCH = "CREATE TABLE " + TABLE_SWITCH + "(" + SWITCH_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SWITCH_NAME + " TEXT NOT NULL, " + SWITCH_STATUS
            + " TEXT NOT NULL);";

    //Create Table Reminder
    public static final String CREATE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "(" + ID_REMINDER
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "  + CATEGORY_REMINDER + " TEXT NOT NULL, " + DATE_REMINDER
            + " TEXT NOT NULL, " + TIME_REMINDER + " TEXT NOT NULL, " + TITLE_REMINDER + " TEXT NOT NULL, " + STATUS_SWITCH + " TEXT NOT NULL);";


    //insert admin  data into Login table
    private  static final String SQL_ADMIN = "INSERT INTO " + LoginDbSchema.LoginTable.NAME +
            " ( 'name', 'email', 'password') VALUES ('admin'" +
            ", 'admin@gmail.com','admin123')";


    private  static final String SQL_DELETE_LOGIN =
            "DROP TABLE IF EXISTS " + LoginDbSchema.LoginTable.NAME;

    //Drop Table SwStatus
    public static final String SQL_DELETE_SWITCH =
            "DROP TABLE IF EXISTS " + TABLE_SWITCH;

    //Drop Table Reminder
    public static final String SQL_DELETE_REMINDER =
            "DROP TABLE IF EXISTS " + TABLE_REMINDER;



    public LoginBaseHelper(Context context){

        super(context, DATABASE_NAME, null, VERSION);
        Log.d(TAG, "LoginBaseHelper: constructor run");


    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG, "onCreate: execSQL run");
        try {
            //Create table
            db.execSQL(SQL_CREATE_LOGIN);
            db.execSQL(CREATE_SWITCH);
            db.execSQL(CREATE_REMINDER);

            //add default data into table
            db.execSQL(SQL_ADMIN);
            db.execSQL("INSERT INTO " + TABLE_SWITCH + " (" + SWITCH_NAME + ", "+ SWITCH_STATUS +") VALUES" +
                    "('LED 1', 'ON')," +
                    "('LED 2', 'ON')," +
                    "('FAN 1', 'OFF');");
        }catch (Exception e){
            Log.d(TAG, "onCreate: " + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        Log.d(TAG, "onUpgrade: onUpgrade called");
        try{
            //Delete table if exist
            db.execSQL(SQL_DELETE_LOGIN);
            db.execSQL(SQL_DELETE_REMINDER);
            db.execSQL(SQL_DELETE_SWITCH);

            //insert data
            onCreate(db);
        }catch (Exception e){
            Log.d(TAG, "onUpgrade: "+ e);
        }

    }

}
