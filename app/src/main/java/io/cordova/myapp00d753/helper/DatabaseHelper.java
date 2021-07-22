package io.cordova.myapp00d753.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: io.cordova.myapp00d753.helper.DatabaseHelper */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LONG = "longt";
    public static final String COLUMN_STATUS = "status";
    public static final String DB_NAME = "NamesDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "names";

    public DatabaseHelper(Context context) {
        super(context, "NamesDB", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE names(id INTEGER PRIMARY KEY AUTOINCREMENT, address VARCHAR, date VARCHAR,lat  VARCHAR,longt  VARCHAR,  status TINYINT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Persons");
        onCreate(db);
    }

    public boolean addName(String address, String date, String lat, String longt, int status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("date", date);
        contentValues.put("lat", lat);
        contentValues.put("longt", longt);
        contentValues.put("status", Integer.valueOf(status));
        db.insert("names", (String) null, contentValues);
        db.close();
        return true;
    }

    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", Integer.valueOf(status));
        db.update("names", contentValues, "id=" + id, (String[]) null);
        db.close();
        return true;
    }

    public Cursor getNames() {
        return getReadableDatabase().rawQuery("SELECT * FROM names ORDER BY id ASC;", (String[]) null);
    }

    public Cursor getUnsyncedNames() {
        return getReadableDatabase().rawQuery("SELECT * FROM names WHERE status = 0;", (String[]) null);
    }
}
