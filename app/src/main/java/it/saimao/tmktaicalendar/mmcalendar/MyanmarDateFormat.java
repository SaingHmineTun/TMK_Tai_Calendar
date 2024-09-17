package it.saimao.tmktaicalendar.mmcalendar;

public class MyanmarDateFormat {

    public static final char SASANA_YEAR = 'S';

    public static final char BUDDHIST_ERA = 's';

    public static final char BURMESE_YEAR = 'B';

    public static final char MYANMAR_YEAR = 'y';

    public static final char KU = 'k';

    public static final char MONTH_IN_YEAR = 'M';

    public static final char MOON_PHASE = 'p';

    public static final char FORTNIGHT_DAY = 'f';

    public static final char DAY_NAME_IN_WEEK = 'E';

    public static final char NAY = 'n';

	public static final char YAT = 'r';

    public static final String SIMPLE_MYANMAR_DATE_FORMAT_PATTERN = "S s k, B y k, M p f r E n";


    /**
     * Don't let anyone instantiate this class.
     */
    private MyanmarDateFormat() {

    }
}
