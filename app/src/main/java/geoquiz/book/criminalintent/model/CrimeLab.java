package geoquiz.book.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import geoquiz.book.criminalintent.database.CrimeBaseHelper;
import geoquiz.book.criminalintent.database.CrimeCursorWrapper;
import geoquiz.book.criminalintent.database.CrimeDbSchema;
import geoquiz.book.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by jorge.bautista on 11/09/15.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private static Context mContext;
    private static SQLiteDatabase mDataBase;

    public static CrimeLab get(Context context) {

        mContext = context.getApplicationContext();
        mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {

    }

    public void addCrime(Crime c) {
        ContentValues v = getContentValues(c);
        mDataBase.insert(CrimeTable.NAME, null, v);
    }

    public void updateCrime(Crime crime) {
        String uuidString =  crime.getId().toString();
        ContentValues v = getContentValues(crime);
        mDataBase.update(CrimeTable.NAME, v, CrimeTable.Columns.UUID + " = ?", new String[]{uuidString});
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return crimes;

    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        if(crime.getId() != null)
            values.put(CrimeTable.Columns.UUID, crime.getId().toString());
        if(crime.getTitle() != null)
            values.put(CrimeTable.Columns.TITLE, crime.getTitle().toString());
        if(crime.getDate() != null)
            values.put(CrimeTable.Columns.DATE, crime.getDate().getTime());
        if(crime.getSuspect() != null)
            values.put(CrimeTable.Columns.SUSPECT, crime.getSuspect());

        values.put(CrimeTable.Columns.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,//Group by
                null,//Having
                null);//Order by
        return new CrimeCursorWrapper(cursor);
    }


    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Columns.UUID + " = ?", new String[]{id.toString()});

        try {

            if(cursorWrapper.getCount() == 0){
                return null;
            }

            cursorWrapper.moveToFirst();

            return cursorWrapper.getCrime();

        } finally {
            cursorWrapper.close();
        }

    }


    public void deleteCrime(UUID uuid) {
        mDataBase.delete(CrimeTable.NAME, CrimeTable.Columns.UUID + " = ?", new String[]{uuid.toString()});
    }
}
