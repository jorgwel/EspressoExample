package geoquiz.book.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by jorge.bautista on 11/09/15.
 */
public class CrimeListActivity extends SingleFragmentActivityAbstract {


    public static final String TAG = "CrimeListActivity";

    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "Creating CrimeListFragment from inside CrimeListActivity");
        return new CrimeListFragment();
    }
}
