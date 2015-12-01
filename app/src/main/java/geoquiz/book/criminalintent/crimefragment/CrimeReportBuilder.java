package geoquiz.book.criminalintent.crimefragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import java.util.Date;

import geoquiz.book.criminalintent.R;
import geoquiz.book.criminalintent.model.Crime;

public class CrimeReportBuilder {

    private Context mContext;

    private String buildReport(Context context, String solvedString, String dateString, String suspect, String title) {
        mContext = context;
        return mContext.getString(R.string.crime_report, title, dateString, solvedString, suspect);

    }

    private String buildSuspect(String suspect) {
        if (suspect == null)
            suspect = mContext.getString(R.string.crime_report_no_suspect);
        else
            suspect = mContext.getString(R.string.crime_report_suspect, suspect);

        return suspect;
    }

    @NonNull
    private String buildDate(Date date) {
        String dateFormat = "EEE, MMM dd";
        return DateFormat.format(dateFormat, date).toString();
    }

    private String buildSolvedString(boolean isSolved) {
        String solvedString = null;

        if (isSolved)
            solvedString = mContext.getString(R.string.crime_report_solved);
        else
            solvedString = mContext.getString(R.string.crime_report_unsolved);

        return solvedString;
    }

    public String buildReport(Context context, Crime crime) {

        if(context == null)
            throw new IllegalArgumentException("'context' parameter must be different from null");

        String solvedString = buildSolvedString(crime.isSolved());
        String dateString = buildDate(crime.getDate());
        String suspect = buildSuspect(crime.getSuspect());
        String title = crime.getTitle();

        return buildReport(context, solvedString, dateString, suspect, title);
    }

}