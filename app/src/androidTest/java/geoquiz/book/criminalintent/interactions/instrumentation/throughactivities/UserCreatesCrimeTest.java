package geoquiz.book.criminalintent.interactions.instrumentation.throughactivities;

import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import geoquiz.book.criminalintent.CrimeListActivity;
import geoquiz.book.criminalintent.CrimePagerActivity;
import geoquiz.book.criminalintent.R;
import geoquiz.book.criminalintent.TestUtilResources;
import geoquiz.book.criminalintent.database.CrimeBaseHelper;
import geoquiz.book.criminalintent.interactions.instrumentation.local.CrimeListActivityTest;
import geoquiz.book.criminalintent.model.CrimeLab;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jorge.bautista on 16/10/15.
 */
public class UserCreatesCrimeTest extends ActivityInstrumentationTestCase2<CrimeListActivity> {

    private static final String TAG = "UserCreatesCrimeTest";
    private UtilMethods mTestUtilMethods;


    public UserCreatesCrimeTest() {
        super(CrimeListActivity.class);

        mTestUtilMethods = new UtilMethods();

    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        getInstrumentation().getTargetContext().deleteDatabase(CrimeBaseHelper.DATABASE_NAME);
    }




    /**
     * Test: User creates a crime
     * - Preconditions: None
     * - Expected: a user has created a new Crime and the crime list in the main view is already showing it
     */
    public void testUserCreatesACrime() {
        mTestUtilMethods.verifyNumberOfItemsInListOfCrimes(0);
        TestUtilResources.deleteDatabase(getInstrumentation());
        mTestUtilMethods
                .verifyThatClickingOnCreateButton_YouAreTakenToCrimePagerActivity(this);
        mTestUtilMethods
                .verifyThatCommingBackFrom_CreateCrime_TheresOneMoreCrime();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private class UtilMethods {

        void verifyThatCommingBackFrom_CreateCrime_TheresOneMoreCrime() {
            Instrumentation.ActivityMonitor crimePagerMonitor = getInstrumentation()
                    .addMonitor(
                            CrimePagerActivity.class.getName(),
                            null,
                            false
                    );
            onView(
                    withId(R.id.crime_recycler_view)
            ).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click())
            );
            mTestUtilMethods.validateThatCrimePagerActivityWasOpened(crimePagerMonitor);
            getInstrumentation().removeMonitor(crimePagerMonitor);
        }

        void verifyThatClickingOnCreateButton_YouAreTakenToCrimePagerActivity(UserCreatesCrimeTest userCreatesCrimeTest) {
            Instrumentation.ActivityMonitor crimePagerMonitor = getInstrumentation()
                    .addMonitor(
                            CrimePagerActivity.class.getName(),
                            null,
                            false
                    );
            TouchUtils.clickView(userCreatesCrimeTest, getActivity().findViewById(R.id.menu_item_new_crime));
            mTestUtilMethods.validateThatCrimePagerActivityWasOpened(crimePagerMonitor);
            getInstrumentation().removeMonitor(crimePagerMonitor);
        }

        void verifyNumberOfItemsInListOfCrimes(int numberOfItemsToValidate) {
            int currentNumberOfItems = CrimeLab.get(getActivity()).getCrimes().size();
            CrimeListActivityTest.assertEquals(
                    "The database is not empty at start",
                    numberOfItemsToValidate,
                    currentNumberOfItems
            );
        }

        void validateThatCrimePagerActivityWasOpened(Instrumentation.ActivityMonitor crimePagerMonitor) {
            CrimePagerActivity crimePagerActivity = (CrimePagerActivity) crimePagerMonitor.waitForActivityWithTimeout(1000);
            assertNotNull("CrimePagerActivity is null", crimePagerActivity);
            assertEquals("Monitor for CrimePagerActivity has not been called", 1, crimePagerMonitor.getHits());
            assertEquals("Activity is of wrong type", CrimePagerActivity.class, crimePagerActivity.getClass());
            crimePagerActivity.finish();
        }

    }


}
