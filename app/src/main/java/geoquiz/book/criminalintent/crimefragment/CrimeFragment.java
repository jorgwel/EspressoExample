package geoquiz.book.criminalintent.crimefragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import geoquiz.book.criminalintent.R;
import geoquiz.book.criminalintent.model.Crime;
import geoquiz.book.criminalintent.model.CrimeLab;

/**
 * Created by jorge.bautista on 10/09/15.
 */
public class CrimeFragment extends Fragment{

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE= "DialogDate";
    private static final int REQUEST_DATE = 0;
    public static final String TAG = "CrimeFragment";

    private static final int DELETE_CRIME = R.id.menu_item_delete_crime;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        return initializeFragmentArguments(crimeId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFragmentData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case DELETE_CRIME:
                return deleteCrime(mCrime.getId());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        initializeView(v, mCrime);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_DATE) {
            Date d = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(d);
            updateDate(mDateButton, mCrime.getDate().toString());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @NonNull
    private static CrimeFragment initializeFragmentArguments(UUID crimeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment cf = new CrimeFragment();
        cf.setArguments(bundle);
        return cf;
    }

    private void initializeFragmentData() {
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    private void initializeView(View v, Crime crime) {
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);

        initializeTitle(mTitleField, crime);
        initializeDate(mDateButton, crime);
        initializeSolvedCheckBox(mSolvedCheckBox, crime);
    }

    private void initializeTitle(EditText titleField, final Crime crime) {
        //region On "text changed" listener for title field
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion
    }

    private void initializeDate(Button dateButton, final Crime crime) {
        String fDate = DateFormat.format("MMM d, yyyy", crime.getDate()).toString();
        updateDate(dateButton, fDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(crime);
            }
        });
    }

    private void initializeSolvedCheckBox(CheckBox solvedCheckBox, final Crime crime) {
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });
    }

    private void showDatePicker(Crime crime) {
        FragmentManager f = getFragmentManager();
        DatePickerFragment d = DatePickerFragment.newInstance(crime.getDate());
        d.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        d.show(f, DIALOG_DATE);
    }

    private void updateDate(Button dateButton, String text) {
        dateButton.setText(text);
    }

    private boolean deleteCrime(UUID uuid) {
        CrimeLab.get(getActivity()).deleteCrime(uuid);
        getActivity().finish();
        return true;
    }

    private String getCrimeReport() {
        return new CrimeReportBuilder().buildReport(getActivity(), mCrime);
    }

}
