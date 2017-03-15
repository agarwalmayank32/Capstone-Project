package info.mayankag.parlorbeacon.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "movie.db";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " + DBContract.PB_DB_Entry.TABLE_NAME + " ( "
                + DBContract.PB_DB_Entry.COLUMN_ID + " integer primary key autoincrement,"
                + DBContract.PB_DB_Entry.SHOPEMAIL + " text not null, "
                + DBContract.PB_DB_Entry.CUSTEMAIL + " text not null,"
                + DBContract.PB_DB_Entry.CUSTNAME + " text not null, "
                + DBContract.PB_DB_Entry.CUSTPHONE + " text not null, "
                + DBContract.PB_DB_Entry.DATE + " text not null, "
                + DBContract.PB_DB_Entry.TIME + " text not null, "
                + DBContract.PB_DB_Entry.SERVICE + " text not null, "
                + DBContract.PB_DB_Entry.STATUS + " text not null);";
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.PB_DB_Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}