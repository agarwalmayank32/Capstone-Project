package info.mayankag.parlorbeacon.Database;

import android.provider.BaseColumns;

public class DBContract {

    public static final class PB_DB_Entry implements BaseColumns {

        static final String TABLE_NAME="pb_cust";
        static final String COLUMN_ID="id";
        public static final String SHOPEMAIL="shopemail";
        public static final String CUSTNAME="custname";
        public static final String CUSTEMAIL="custemail";
        public static final String CUSTPHONE="custphone";
        public static final String DATE="date";
        public static final String TIME="time";
        public static final String SERVICE="service";
        public static final String STATUS="status";
    }
}
