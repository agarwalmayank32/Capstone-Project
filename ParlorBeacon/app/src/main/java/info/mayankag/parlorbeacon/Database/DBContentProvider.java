package info.mayankag.parlorbeacon.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static info.mayankag.parlorbeacon.Database.DBContract.PB_DB_Entry.COLUMN_ID;
import static info.mayankag.parlorbeacon.Database.DBContract.PB_DB_Entry.TABLE_NAME;

public class DBContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private static final int BOOKINGS=100;
    private static final int BOOKING_ID=110;

    private static final String AUTHORITY="info.mayankag.parlorbeacon.DBContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, BOOKINGS);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", BOOKING_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());

        db=dbHelper.getWritableDatabase();
        return db!=null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case BOOKING_ID:
                queryBuilder.appendWhere(COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case BOOKINGS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        long row = db.insert(TABLE_NAME,"",contentValues);

        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        int rowsAffected;
        switch (uriType) {
            case BOOKINGS:
                rowsAffected = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKING_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsAffected = db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
                } else {
                    rowsAffected = db.delete(TABLE_NAME, selection + " and " + COLUMN_ID + "=" + id, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        return 0;
    }
}
