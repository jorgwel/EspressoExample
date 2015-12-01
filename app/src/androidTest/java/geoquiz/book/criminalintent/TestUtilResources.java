package geoquiz.book.criminalintent;

import android.app.Instrumentation;

import geoquiz.book.criminalintent.database.CrimeBaseHelper;

/**
 * Created by jorge.bautista on 20/10/15.
 */
public class TestUtilResources {

    public static void deleteDatabase(Instrumentation instrumentation) {
        instrumentation.getTargetContext().deleteDatabase(CrimeBaseHelper.DATABASE_NAME);
    }

}
