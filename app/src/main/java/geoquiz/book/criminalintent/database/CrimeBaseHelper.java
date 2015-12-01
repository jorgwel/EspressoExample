package geoquiz.book.criminalintent.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import geoquiz.book.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by jorge.bautista on 24/09/15.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CrimeBaseHelper";

    private static final int VERSION = 2;
    public static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "On method CrimeBaseHelper.onCreate");
        StringBuilder qb = new StringBuilder(150);
        qb.append("create table ").append(CrimeTable.NAME).append("(")
          .append("_id integer primary key autoincrement, ")
          .append(CrimeTable.Columns.UUID).append(", ")
          .append(CrimeTable.Columns.TITLE).append(", ")
          .append(CrimeTable.Columns.DATE).append(", ")
          .append(CrimeTable.Columns.SOLVED).append(", ")
          .append(CrimeTable.Columns.SUSPECT).append(") ");

        db.execSQL(qb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "On method CrimeBaseHelper.onUpgrade");

        Log.d(TAG, "Adding the new SUSPECT column, for chapter 15");
        db.execSQL("ALTER TABLE crimes ADD COLUMN suspect");

    }
}
