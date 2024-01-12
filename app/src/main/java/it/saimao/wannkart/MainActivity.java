package it.saimao.wannkart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.threetenabp.AndroidThreeTen;
import mmcalendar.*;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity implements CardView.OnClickListener {

    private TextView tvDay, tvDate, tvMonth, tvWannKart;
    private TextView tvMyanmarDate;
    private GridLayout calendarLayout;
    private LocalDate currentDate;
    private OnSwipeTouchListener onSwipeTouchListener;
    private int index, emptyDateCounts;
    private View preSelectedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);
        LocalDate todayDate = LocalDate.now();
        Config.initDefault(new Config.Builder().setCalendarType(CalendarType.ENGLISH).setLanguage(Language.TAI).build());
        tvDay = findViewById(R.id.tvDay);
        tvDate = findViewById(R.id.tvDate);
        tvMonth = findViewById(R.id.tvMonth);
        tvWannKart = findViewById(R.id.tvWannKart);
        tvWannKart.setOnLongClickListener(v -> changeWannKartName());
        tvMyanmarDate = findViewById(R.id.tvMyanmarDate);
        calendarLayout = findViewById(R.id.calenderPane);
        onSwipeTouchListener = new OnSwipeTouchListener() {
            public boolean onSwipeRight() {
                calendarLayout.removeAllViews();
                currentDate = currentDate.minusMonths(1);
                setCalendarView(currentDate);
                if (!currentDate.isEqual(LocalDate.now()))
                    setDate(currentDate.withDayOfMonth(1));
                else
                    setDate(LocalDate.now());
                return true;
            }

            public boolean onSwipeLeft() {

                calendarLayout.removeAllViews();
                currentDate = currentDate.plusMonths(1);
                setCalendarView(currentDate);
                if (!currentDate.isEqual(LocalDate.now()))
                    setDate(currentDate.withDayOfMonth(1));
                else
                    setDate(LocalDate.now());
                return true;
            }
        };

        currentDate = LocalDate.now();

        setCalendarView(todayDate);

        setDate(currentDate);

    }

    private boolean changeWannKartName() {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        TextInputEditText editText = new TextInputEditText(MainActivity.this);
        editText.setText(Utils.getDayName(this, wannKartDay));
        editText.setSelectAllOnFocus(true);
        layout.addView(editText);

        LinearLayout.LayoutParams lp4layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(lp4layout);

        LinearLayout.LayoutParams lp4et = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp4et.setMarginStart(Utils.dpToPx(15));
        lp4et.setMarginEnd(Utils.dpToPx(15));

        editText.setLayoutParams(lp4et);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage("ဈေးနေ့ပြောင်းရန်ထည့်ပါ")
                .setView(layout)
                .setPositiveButton("ပြောင်းမည်", (dialog, whichButton) -> {
                    if (editText.length() > 0) {
                        Utils.setDayName(this, wannKartDay, editText.getText().toString());
                        tvWannKart.setText(Utils.getDayName(this, wannKartDay));
                    } else {
                        Toast.makeText(this, "ဈေးနေ့ရေးပေးပါ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("မလုပ်တော့ပါ", null)
                .show();
        return true;
    }


    private void setCalendarView(LocalDate todayDate) {
        index = 0;
        emptyDateCounts = todayDate.withDayOfMonth(1).getDayOfWeek().getValue();

        // Creating Empty Grid Cell
        if (emptyDateCounts > 0 && emptyDateCounts < 7) {
            index = emptyDateCounts;
            for (int i = 0; i < emptyDateCounts; i++) {
                createEmptyCell();
            }
        } else {
            emptyDateCounts = 0;
        }

        for (int i = 0; i < todayDate.getMonth().length(todayDate.getYear() % 4 == 0); i++) {

            index++;
            createCalendarView(i);
        }
    }


    private void createEmptyCell() {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
        param.height = 0;
        param.width = 0;
        calendarLayout.addView(new View(this), param);
    }

    private void createCalendarView(int day) {


        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
        param.height = 0;
        param.width = 0;

        RelativeLayout dateButton = createDateButton(currentDate.withDayOfMonth(1).plusDays(day));
        dateButton.setOnTouchListener(onSwipeTouchListener);
        if (index < 36) {
            calendarLayout.addView(dateButton, param);
        } else {
            /*
            If there's more than 36 days, rather than
            creating the sixth row,
            I just added the cell into the first row
             */
            int location = 36 - index;
            if (location < 0) location = location * (-1);
            if (calendarLayout.getChildAt(location) != null) {
                calendarLayout.removeViewAt(location);
                calendarLayout.addView(dateButton, location, param);
            }
        }
    }


    private RelativeLayout createDateButton(LocalDate day) {
        RelativeLayout dateButton = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.date_button, null);
        dateButton.setTag(day);
        dateButton.setOnClickListener(this);
        TextView tvMoonDate = dateButton.findViewById(R.id.tvMoonDate);
        TextView tvEngDate = dateButton.findViewById(R.id.tvEngDate);
        ImageView ivMoon = dateButton.findViewById(R.id.ivMoon);


        if (day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            tvEngDate.setTextColor(Color.parseColor("#D32F2F"));
        }
        tvEngDate.setText("" + day.getDayOfMonth());

        /*
        For Myanmar Date
         */
        MyanmarDate myanmarDate = MyanmarDate.create(day.getYear(), day.getMonthValue(), day.getDayOfMonth());

        int monthDay = myanmarDate.getDayOfMonth() > 15 ? myanmarDate.getDayOfMonth() - 15 : myanmarDate.getDayOfMonth();

        if (day.isEqual(LocalDate.now())) {
            dateButton.setBackgroundResource(R.drawable.date_today);
            preSelectedView = dateButton;
        }
        if (HolidayCalculator.isHoliday(myanmarDate)) {
            tvEngDate.setTextColor(Color.parseColor("#D32F2F"));
        }
        if (myanmarDate.getMoonPhaseValue() == 1) {
            ivMoon.setImageResource(R.drawable.full_moon);
        }

        if (myanmarDate.getMoonPhaseValue() == 3) {
            ivMoon.setImageResource(R.drawable.new_moon);
        }

        tvMoonDate.setText("" + monthDay);
        return dateButton;
    }


    private int wannKartDay;

    @SuppressLint("DefaultLocale")
    private void setDate(LocalDate localDate) {
        MyanmarDate myanmarDate = MyanmarDate.create(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        tvDay.setText(String.format("%d", localDate.getDayOfMonth()));
        tvMonth.setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tvMyanmarDate.setText(myanmarDate.format("S s k, B y k, M p f r En"));
        if (HolidayCalculator.isHoliday(myanmarDate)) {
            String holiday = HolidayCalculator.getHoliday(myanmarDate).get(0);
            tvDate.setText(holiday);
            tvDate.setTextColor(Color.parseColor("#D32F2F"));
            tvDay.setTextColor(Color.parseColor("#D32F2F"));
            tvMonth.setTextColor(Color.parseColor("#D32F2F"));
            tvMyanmarDate.setTextColor(Color.parseColor("#D32F2F"));
        } else {

            tvDate.setText(myanmarDate.format("En"));
            tvDate.setTextColor(Color.parseColor("#4E342E"));
            tvDay.setTextColor(Color.parseColor("#4E342E"));
            tvMonth.setTextColor(Color.parseColor("#4E342E"));
            tvMyanmarDate.setTextColor(Color.parseColor("#4E342E"));
        }

        long dayCounts = ChronoUnit.DAYS.between(LocalDate.of(1996, 1, 1), localDate);
        wannKartDay = (int) (dayCounts % 5);
//        tvWannKart.setText(wannKartStrings[wannkart]);
        String wannKartName = Utils.getDayName(this, wannKartDay);
        tvWannKart.setText(wannKartName);
        tvWannKart.setBackgroundColor(getWannKartColor());

        // SET Selected Background for the current date
        int tempIndex = localDate.getDayOfMonth() + emptyDateCounts - 1;
        if (tempIndex >= 35) {
            tempIndex = tempIndex - 35;
        }
        View thisView = calendarLayout.getChildAt(tempIndex);
        thisView.setBackgroundResource(R.drawable.date_selected);
        preSelectedView = thisView;
    }

    private int getWannKartColor() {
        switch (wannKartDay) {

            case 0:
                // RED
                return Color.parseColor("#DC3545");
            case 1:
                // BLUE
                return Color.parseColor("#0D6EFD");
            case 2:
                // BLACK
                return Color.parseColor("#333333");
            case 3:
                // YELLOW
                return Color.parseColor("#fd7e14");
            default:
                // GREEN
                return Color.parseColor("#198754");
        }
    }


    @Override
    public void onClick(View view) {
        LocalDate localDate = (LocalDate) view.getTag();
        resetBackground();
        setDate(localDate);
    }

    private void resetBackground() {
        LocalDate date = (LocalDate) preSelectedView.getTag();
        if (date.isEqual(LocalDate.now()))
            preSelectedView.setBackgroundResource(R.drawable.date_today);
        else
            preSelectedView.setBackgroundResource(R.drawable.date_bg);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuInfo) {
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        }
        return false;
    }
}