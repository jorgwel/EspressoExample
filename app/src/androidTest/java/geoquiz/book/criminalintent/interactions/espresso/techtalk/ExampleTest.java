package geoquiz.book.criminalintent.interactions.espresso.techtalk;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;

import geoquiz.book.criminalintent.CrimeListActivity;
import geoquiz.book.criminalintent.R;
import geoquiz.book.criminalintent.database.CrimeBaseHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class ExampleTest extends ActivityInstrumentationTestCase2<CrimeListActivity> {

    private final static String TAG = "ExampleTest";

    public ExampleTest() {
        super(CrimeListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testClickOn_CreateNewCrime_Button() {

        onView(
                withId(R.id.menu_item_new_crime)
        ).perform(
                click()
        );

        onView(
                withId(R.id.crime_field_title)
        ).check(
                matches(isDisplayed())
        );

        pressBack();


    }


    public void testClickOn_SelectDate() {

        onView(
                withId(R.id.menu_item_new_crime)
        ).perform(
                click()
        );

        onView(
                withId(R.id.crime_date)
        ).perform(
                click()
        );


        onView(
                withId(R.id.dialog_date_date_picker)
        ).check(
                matches(isDisplayed())
        );

    }


    public void testSolvedChecked(){
        onView(
                withId(R.id.menu_item_new_crime)
        ).perform(click());

        onView(
            withId(R.id.crime_solved)
        ).perform(
                click()
        );

        onView(
                withId(R.id.crime_solved)
        ).check(
                matches(isChecked())
        );
    }



    public void testNewCrimeIsCreated(){

        onView(
                withId(R.id.menu_item_new_crime)
        ).perform(
                click()
        );

        onView(
                withId(R.id.crime_title)
        ).perform(
                typeText("New crime has been reported!!")
        );

        pressBack();

        onView(
                withId(R.id.crime_recycler_view)
        ).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        onView(
                withId(R.id.crime_field_title)
        ).check(
                matches(isDisplayed())
        );
    }


    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        deleteDabase();
    }

    private void deleteDabase() {
        getInstrumentation().getTargetContext().deleteDatabase(CrimeBaseHelper.DATABASE_NAME);
    }

}
