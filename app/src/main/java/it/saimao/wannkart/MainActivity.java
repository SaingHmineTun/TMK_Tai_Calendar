package it.saimao.wannkart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import mmcalendar.HolidayCalculator;
import mmcalendar.LanguageCatalog;
import mmcalendar.MyanmarDate;
import mmcalendar.MyanmarDateConverter;

public class MainActivity extends AppCompatActivity implements CardView.OnClickListener {

    private TextView tvDay, tvDate, tvMonth, tvWannKart;
    private TextView tvMyanmarDate;
    private GridLayout calendarLayout;
    private LocalDate currentDate;
    private OnSwipeTouchListener onSwipeTouchListener;
    private int index, emptyDateCounts;
    private View preSelectedView;

    private static final String[] wannKartStrings = {"မူဆယ်ဈေးနေ့", "နမ့်ခမ်းဈေးနေ့", "စယ်လန့်ဈေးနေ့", "ပန်ခမ်းဈေးနေ့", "နမ့်စန့်ဈေးနေ့"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalDate todayDate = LocalDate.now();
        tvDay = findViewById(R.id.tvDay);
        tvDate = findViewById(R.id.tvDate);
        tvMonth = findViewById(R.id.tvMonth);
        tvWannKart = findViewById(R.id.tvWannKart);
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
        MyanmarDate myanmarDate = MyanmarDateConverter.convert(day.getYear(), day.getMonthValue(), day.getDayOfMonth());

        int monthDay = myanmarDate.getMonthDay() > 15 ? myanmarDate.getMonthDay() - 15 : myanmarDate.getMonthDay();

        if (day.isEqual(LocalDate.now())) {
            dateButton.setBackgroundResource(R.drawable.date_today);
            preSelectedView = dateButton;
        } else if (HolidayCalculator.isHoliday(myanmarDate)) {
            tvEngDate.setTextColor(Color.parseColor("#D32F2F"));
        }

        if (myanmarDate.getMoonPhase().equals("လပြည့်")) {
            ivMoon.setImageResource(R.drawable.full_moon);
        }

        if (myanmarDate.getMoonPhase().equals("လကွယ်")) {
            ivMoon.setImageResource(R.drawable.new_moon3);
        }

        tvMoonDate.setText("" + monthDay);
        return dateButton;
    }


    private void setDate(LocalDate localDate) {
        MyanmarDate myanmarDate = MyanmarDateConverter.convert(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());

        tvDay.setText(String.format("%d", localDate.getDayOfMonth()));
        tvMonth.setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tvMyanmarDate.setText(myanmarDate.format("S s k, B y k, M p f r"));
        if (HolidayCalculator.isHoliday(myanmarDate)) {
            String holiday = HolidayCalculator.getHoliday(myanmarDate).get(0);
            tvDate.setText(LanguageCatalog.getInstance().translate(holiday));
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
        int wannkart = (int) (dayCounts % 5);
        tvWannKart.setText(wannKartStrings[wannkart]);

        // SET Selected Background for the current date
        int tempIndex = localDate.getDayOfMonth() + emptyDateCounts - 1;
        if (tempIndex >= 35) {
            tempIndex = tempIndex - 35;
        }
        View thisView = calendarLayout.getChildAt(tempIndex);
        thisView.setBackgroundResource(R.drawable.date_selected);
        preSelectedView = thisView;
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