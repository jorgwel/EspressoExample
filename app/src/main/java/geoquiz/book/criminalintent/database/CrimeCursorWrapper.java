package geoquiz.book.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import geoquiz.book.criminalintent.database.CrimeDbSchema.CrimeTable;
import geoquiz.book.criminalintent.model.Crime;

/**
 * Created by jorge.bautista on 29/09/15.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {

        String uuidString = getString(getColumnIndex(CrimeTable.Columns.UUID));
        String title = getString(getColumnIndex(CrimeTable.Columns.TITLE));
        long dateAsLong = getLong(getColumnIndex(CrimeTable.Columns.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Columns.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Columns.SUSPECT));

        return fillCrimeData(uuidString, title, dateAsLong, isSolved, suspect);
    }

    @NonNull
    private Crime fillCrimeData(String uuidString, String title, long dateAsLong, int isSolved, String suspect) {
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(dateAsLong));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }

}
