package database.LoginDbSchema;

public class LoginDbSchema {
    public static final class  LoginTable{
        //Login table
        public static final String NAME = "Login";

        //Switch table
        public static final String TABLE_SWITCH = "SwSwitch";

        //Reminder table
        public static final String TABLE_REMINDER = "Reminder";


    }

    public static final class Cols{
        //Login cols
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";


        //Reminder cols
        public static final String ID_REMINDER = "_id";
        public static final String CATEGORY_REMINDER = "Category";
        public static final String DATE_REMINDER = "Date";
        public static final String TIME_REMINDER = "Time";
        public static final String TITLE_REMINDER = "Title";
        public static final String STATUS_SWITCH = "Switch";

        //Table SwStatus
        public static final String SWITCH_ID = "_id";
        public static final String SWITCH_NAME = "name";
        public static final String SWITCH_STATUS = "status";
        public static final String SWITCH_TIMEON = "timeOn";
        public static final String SWITCH_TIMEOFF = "timeOff";
        public static final String SWITCH_DAYON = "dayOn";
        public static final String SWITCH_DAYOFF = "dayOff";
        public static final String SWITCH_DATE = "date";

    }
}
