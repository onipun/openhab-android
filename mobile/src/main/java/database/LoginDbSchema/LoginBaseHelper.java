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
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_DATE;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_DAYOFF;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_DAYON;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_ID;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_NAME;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_STATUS;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_TIMEOFF;
import static database.LoginDbSchema.LoginDbSchema.Cols.SWITCH_TIMEON;
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
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SWITCH_NAME + " TEXT NOT NULL, " + SWITCH_STATUS + " TEXT NOT NULL, "
            + SWITCH_TIMEON + " TEXT, " + SWITCH_TIMEOFF + " TEXT, " + SWITCH_DAYON + " INTEGER NOT NULL, " + SWITCH_DAYOFF
            + " INTEGER, " + SWITCH_DATE + " TEXT);";

    //Create Table SwStatus
    /*public static final String CREATE_SWITCH = "CREATE TABLE " + TABLE_SWITCH + "(" + SWITCH_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SWITCH_NAME + " TEXT NOT NULL, " + SWITCH_STATUS
            + " TEXT NOT NULL);";*/

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

            /* DEPRECATED */
            /* THIS TABLE FOR DEV*/
            /*db.execSQL("INSERT INTO " + TABLE_SWITCH + " (" + SWITCH_NAME + ", "+ SWITCH_STATUS +") VALUES" +
                    "('LED 1', 'ON')," +
                    "('LED 2', 'ON')," +
                    "('FAN 1', 'OFF');");*/

            db.execSQL("INSERT INTO " + TABLE_SWITCH + " (" + SWITCH_NAME + ", " + SWITCH_STATUS +
                    ", " + SWITCH_TIMEON + ", " + SWITCH_TIMEOFF + ", " +  SWITCH_DAYON + ", " + SWITCH_DAYOFF + ", " + SWITCH_DATE + ") VALUES" +

                    "('LED 1', 'OFF', '0603', '0610', 'monday', 'monday', '03/12/2018')," +
                    "('LED 1', 'OFF', '1402', '1405', 'monday', 'monday', '03/12/2018')," +
                    "('LED 1', 'OFF', '1905', '2310', 'monday', 'monday', '03/12/2018')," +
                    "('LED 1', 'OFF', '0610', '0615', 'tuesday', 'tuesday', '04/12/2018')," +
                    "('LED 1', 'OFF', '1430', '1435', 'tuesday', 'tuesday', '04/12/2018')," +
                    "('LED 1', 'OFF', '1910', '2305', 'tuesday', 'tuesday', '04/12/2018')," +
                    "('LED 1', 'OFF', '0705', '0710', 'wednesday', 'wednesday', '05/12/2018')," +
                    "('LED 1', 'OFF', '1514', '1518', 'wednesday', 'wednesday', '05/12/2018')," +
                    "('LED 1', 'OFF', '1901', '2302', 'wednesday', 'wednesday', '05/12/2018')," +
                    "('LED 1', 'OFF', '0703', '0713', 'thursday', 'thursday', '06/12/2018')," +
                    "('LED 1', 'OFF', '1302', '1307', 'thursday', 'thursday', '06/12/2018')," +
                    "('LED 1', 'OFF', '1905', '2310', 'thursday', 'thursday', '06/12/2018')," +
                    "('LED 1', 'OFF', '0607', '0612', 'friday', 'friday', '07/12/2018')," +
                    "('LED 1', 'OFF', '1530', '1535', 'friday', 'friday', '07/12/2018')," +
                    "('LED 1', 'OFF', '1850', '0150', 'friday', 'saturday', '08/12/2018')," +
                    "('LED 1', 'OFF', '0240', '0246', 'saturday', 'saturday', '08/12/2018')," +
                    "('LED 1', 'OFF', '0715', '0718', 'saturday', 'saturday', '08/12/2018')," +
                    "('LED 1', 'OFF', '1340', '1345', 'saturday', 'saturday', '08/12/2018')," +
                    "('LED 1', 'OFF', '1915', '0110', 'saturday', 'sunday', '09/12/2018')," +
                    "('LED 1', 'OFF', '0325', '0330', 'sunday', 'sunday', '09/12/2018')," +
                    "('LED 1', 'OFF', '0730', '0735', 'sunday', 'sunday', '09/12/2018')," +
                    "('LED 1', 'OFF', '1624', '1628', 'sunday', 'sunday', '09/12/2018')," +
                    "('LED 1', 'OFF', '1901', '2230', 'sunday', 'sunday', '09/12/2018')," +
                    "('LED 1', 'OFF', '0705', '0810', 'monday', 'monday', '10/12/2018')," +
                    "('LED 1', 'OFF', '0925', '1010', 'monday', 'monday', '10/12/2018')," +
                    "('LED 1', 'OFF', '1402', '1405', 'monday', 'monday', '10/12/2018')," +
                    "('LED 1', 'OFF', '1905', '2310', 'monday', 'monday', '10/12/2018')," +
                    "('LED 2', 'ON', '1900', null, 'monday', null, '10/12/2018')," +
                    "('FAN 1', 'ON', '1900', null, 'monday', null, '10/12/2018');");


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
