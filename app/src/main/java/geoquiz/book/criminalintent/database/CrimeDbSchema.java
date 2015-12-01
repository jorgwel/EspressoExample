package geoquiz.book.criminalintent.database;

/**
 * Created by jorge.bautista on 24/09/15.
 */
public class CrimeDbSchema {
    public static final class CrimeTable {

        public static final String NAME = "Crimes";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }

    }
}
