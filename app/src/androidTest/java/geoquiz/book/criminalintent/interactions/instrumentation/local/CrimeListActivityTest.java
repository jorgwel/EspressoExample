package geoquiz.book.criminalintent.interactions.instrumentation.local;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import geoquiz.book.criminalintent.CrimeListActivity;
import geoquiz.book.criminalintent.CrimePagerActivity;
import geoquiz.book.criminalintent.R;
import geoquiz.book.criminalintent.TestUtilResources;
import geoquiz.book.criminalintent.model.CrimeLab;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by jorge.bautista on 08/10/15.
 */
public class CrimeListActivityTest extends ActivityInstrumentationTestCase2<CrimeListActivity> {

    private final static String TAG = "CrimeListActivityTest";
    private final UtilMethods mTestUtilMethods = new UtilMethods();

    private RecyclerView mCrimeRecyclerView;
    private ActionBar actionBar;


    public CrimeListActivityTest() {
        super(CrimeListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    /**
     * Test: The application opens in the CrimeListActivity as the Launcher Activity
     * - Preconditions: None
     * - Expected: the first activity shown is CrimeListActivity
     */
    public void testCrimeListActivity_being_Shown_AsTheLauncherActivity() {
        String activityThatShouldBeLaunched = "geoquiz.book.criminalintent.CrimeListActivity";
        String activityThatIsBeingLaunched = mTestUtilMethods.getNameOfLaunchedActivity();
        assertEquals("The launcher activity is wrong", activityThatShouldBeLaunched, activityThatIsBeingLaunched);
    }

    /**
     * Test: When application starts, the number of crimes is not being shown
     * - Preconditions: None
     * - Expected: The number of crimes is not being shown at start
     */
    public void testSubtitleNotShown_AtAppStart() {
        TestUtilResources.deleteDatabase(getInstrumentation());
        assertFalse("The number of crimes is being shown at startup", mTestUtilMethods.isNumberOfCrimesShown());
    }

    /**
     * Test: User clicks on "Show subtitle" when the number of crimes is being shown
     * - Preconditions: App is showing the main activity
     * - Expected: The number of crimes is hidden
     * <p/>
     * Useful resources: https://developer.android.com/training/activity-testing/activity-functional-testing.html
     */
    public void testUserClicksOnShowSubtitleButton_TheNumberOfCrimes_IsShown() {
        TouchUtils.clickView(this, getActivity().findViewById(R.id.menu_item_show_subtitle));
        assertTrue("The number of crimes is NOT being shown at startup", mTestUtilMethods.isNumberOfCrimesShown());
    }

    /**
     * Test: User clicks on "Show subtitle" when the number of crimes is being shown
     * - Preconditions: App is showing the main activity
     * - Expected: The number of crimes is hidden
     */
    public void testUserClicksOnShowSubtitleButton_WhenNumberOfCrimesAreBeingShown_TheNumberOfCrimesAreNot_BeingShown() {
        testUserClicksOnShowSubtitleButton_TheNumberOfCrimes_IsShown();
        TouchUtils.clickView(this, getActivity().findViewById(R.id.menu_item_show_subtitle));
        assertFalse("The number of crimes is being shown after clicking twice the button \"Show subtitle\"", mTestUtilMethods.isNumberOfCrimesShown());
    }

    /**
     * Test: App is opened and has NOT registered crimes
     * - Preconditions: None
     * - Expected: the application should show the empty view of the list
     */
    public void testCrimeList_Empty_AtAppStart() {
        TestUtilResources.deleteDatabase(getInstrumentation());
        mTestUtilMethods.assignRecyclerView();
        mTestUtilMethods.verifyNumberOfItemsInListOfCrimes(0);
    }

    /**
     * Test: Clicking on the button "Create"
     * - Preconditions: None
     * - Expected: The activity CrimePagerActivity is launched
     */
    public void testOpensCrimePagerActivity_When_UserCreatesACrime() {

        Instrumentation.ActivityMonitor crimePagerMonitor = getInstrumentation().addMonitor(CrimePagerActivity.class.getName(), null, false);
        TouchUtils.clickView(this, getActivity().findViewById(R.id.menu_item_new_crime));
        mTestUtilMethods.validateThatCrimePagerActivityWasOpened(crimePagerMonitor);
        getInstrumentation().removeMonitor(crimePagerMonitor);


    }

    /**
     * Test: Clicking on a Crime in the list
     * - Preconditions: A crime or more are already created
     * - Expected: The activity CrimePagerActivity is launched
     */
    public void testUserOpensACrime() {

        TestUtilResources.deleteDatabase(getInstrumentation());
        testOpensCrimePagerActivity_When_UserCreatesACrime();
        Instrumentation.ActivityMonitor crimePagerMonitor = getInstrumentation().addMonitor(CrimePagerActivity.class.getName(), null, false);

        onView(
                withId(R.id.crime_recycler_view)
        ).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        mTestUtilMethods.validateThatCrimePagerActivityWasOpened(crimePagerMonitor);
        getInstrumentation().removeMonitor(crimePagerMonitor);

    }

    /**
     * Test: App is opened and has registered crimes
     * - Preconditions: Some crimes have been registered
     * - Expected: the application should show the list with the registered crimes
     */
    public void testCrimeList_HasRecords_AtApStart() {
        assertTrue(true);
    }


    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        getActivity().finish();
    }


    private class UtilMethods {

        boolean isNumberOfCrimesShown() {
            assignActionBar();
            return actionBar.getSubtitle() != null;
        }

        void assignActionBar() {
            actionBar = getActivity().getSupportActionBar();
        }

        void assignRecyclerView() {
            mCrimeRecyclerView = (RecyclerView) getActivity().findViewById(R.id.crime_recycler_view);
        }

        String getNameOfLaunchedActivity() {
            PackageManager pm = getInstrumentation().getTargetContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("geoquiz.book.criminalintent");
            return intent.getComponent().getClassName();
        }

        void verifyNumberOfItemsInListOfCrimes(int numberOfItemsToValidate) {
            int currentNumberOfItems = CrimeLab.get(getActivity()).getCrimes().size();
            CrimeListActivityTest.assertEquals("The database is not empty at start", numberOfItemsToValidate, currentNumberOfItems);
        }

        void validateThatCrimePagerActivityWasOpened(Instrumentation.ActivityMonitor crimePagerMonitor) {
            CrimePagerActivity crimePagerActivity = (CrimePagerActivity) crimePagerMonitor.waitForActivityWithTimeout(1000);
            CrimeListActivityTest.assertNotNull("CrimePagerActivity is null", crimePagerActivity);
            CrimeListActivityTest.assertEquals("Monitor for CrimePagerActivity has not been called", 1, crimePagerMonitor.getHits());
            CrimeListActivityTest.assertEquals("Activity is of wrong type", CrimePagerActivity.class, crimePagerActivity.getClass());
            crimePagerActivity.finish();
        }
    }


}
