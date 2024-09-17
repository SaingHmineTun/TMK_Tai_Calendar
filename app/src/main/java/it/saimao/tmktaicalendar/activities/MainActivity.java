package it.saimao.tmktaicalendar.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import it.saimao.tmktaicalendar.OnSwipeTouchListener;
import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.ShanDate;
import it.saimao.tmktaicalendar.databinding.ActivityMainBinding;
import it.saimao.tmktaicalendar.mmcalendar.CalendarType;
import it.saimao.tmktaicalendar.mmcalendar.Config;
import it.saimao.tmktaicalendar.mmcalendar.Language;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GestureDetector gestureDetector;
    private OnSwipeTouchListener onSwipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListeners();
    }

    private void initUi() {
        Config.initDefault(
                new Config.Builder()
                        .setCalendarType(CalendarType.ENGLISH)
                        .setLanguage(Language.TAI)
                        .build());
        currentDate = LocalDate.now();
        buildCalendar();
    }

    private void initListeners() {

        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        binding.glDate.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        binding.tvDetail.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));

    }

    // Custom GestureListener for detecting swipes
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) { // Check for horizontal swipe
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    private void onSwipeRight() {
        currentDate = currentDate.minusMonths(1);
        buildCalendar();
    }

    private void onSwipeLeft() {
        currentDate = currentDate.plusMonths(1);
        buildCalendar();
    }

    private LocalDate currentDate;

    @SuppressLint("ClickableViewAccessibility")
    private void buildCalendar() {
        // Get first day of month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

        int val = firstDayOfMonth.getDayOfWeek().getValue(); // 1 for Monday
        int start = 0, end = 35;
        if (val == 7) {
            start += val;
            end += val;
        }

        LocalDate date = firstDayOfMonth;

        for (int i = start; i < end; i++) {

            RelativeLayout layout = (RelativeLayout) binding.glDate.getChildAt(i - start);

            if (i < val) {
                layout.setVisibility(View.INVISIBLE);
            } else if (i < firstDayOfMonth.lengthOfMonth() + val) {
                date = firstDayOfMonth.plusDays(i - val);
                customizeDate(layout, date);


            } else {
                layout.setVisibility(View.INVISIBLE);
            }
        }

        if (firstDayOfMonth.lengthOfMonth() + val > end) {
            for (int ii = end, index = 0; ii < firstDayOfMonth.lengthOfMonth() + val; ii++, index++) {

                RelativeLayout layout = (RelativeLayout) binding.glDate.getChildAt(index);
                date = date.plusDays(1);
                customizeDate(layout, date);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void customizeDate(RelativeLayout layout, LocalDate date) {
        if (layout.getVisibility() == View.INVISIBLE) layout.setVisibility(View.VISIBLE);

        MyanmarDate myDate = MyanmarDate.of(date);
        ImageView iv = (ImageView) layout.getChildAt(0);
        TextView myText = (TextView) layout.getChildAt(1);
        TextView enText = (TextView) layout.getChildAt(2);

        layout.setTag(date);
        layout.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return false;
        });
        layout.setOnClickListener(this::onDateClicked);
        TextView mPhase = (TextView) layout.getChildAt(3);

        myText.setText(myDate.getFortnightDay());
        enText.setText(String.valueOf(date.getDayOfMonth()));

        if (date.isEqual(LocalDate.now())) {
            layout.setBackgroundResource(R.drawable.bg_today);
            View view = new View(this);
            view.setTag(date);
            onDateClicked(view);
        }

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            enText.setTextColor(Color.parseColor("#850505"));
        }

        if (myDate.getMoonPhaseValue() == 1) {
            iv.setImageResource(R.drawable.full_moon);
            mPhase.setText(myDate.getMoonPhase());
        } else if (myDate.getMoonPhaseValue() == 3) {

            mPhase.setText(myDate.getMoonPhase());
            iv.setImageResource(R.drawable.new_moon);
        } else {
            mPhase.setText("");
            iv.setImageResource(0);
        }
    }

    private View prevSelectedDate;


    public void onDateClicked(View view) {
        LocalDate date = (LocalDate) view.getTag();
        MyanmarDate myanmarDate = MyanmarDate.of(date);

        if (prevSelectedDate != null) {
            LocalDate prevDate = (LocalDate) prevSelectedDate.getTag();
            if (prevDate.isEqual(LocalDate.now())) {
                prevSelectedDate.setBackgroundResource(R.drawable.bg_today);
            } else {
                prevSelectedDate.setBackgroundResource(R.drawable.bg_item);
            }
            prevSelectedDate = null;
        }
        view.setBackgroundResource(R.drawable.bg_selected);
        prevSelectedDate = view;
        binding.tvDay.setText(String.format(Locale.getDefault(), "%02d", date.getDayOfMonth()));
        binding.tvFullDate.setText(String.format(Locale.ENGLISH, "%02d/%02d/%04d", date.getDayOfMonth(), date.getMonthValue(), date.getYear()));
        binding.tvDate.setText("ဝၼ်း" + myanmarDate.getWeekDay());
        binding.tvDetail.setText(description(date));
    }

    private String description(LocalDate date) {
        MyanmarDate selectedMyanmarDate = MyanmarDate.of(date);
        ShanDate shanDate = new ShanDate(selectedMyanmarDate);
        StringBuilder sb = new StringBuilder();


        sb.append(ShanDate.translate("Sasana Year"))
                .append(" - ").
                append(selectedMyanmarDate.getBuddhistEra())
                .append("၊ ")
                .append(ShanDate.translate("Myanmar Year"))
                .append(" - ")
                .append(selectedMyanmarDate.getYear())
                .append("၊ ")
                .append("ပီႊတႆး - ")
                .append(shanDate.getShanYear()).append(" ၼီႈ၊ ");

        sb.append(ShanDate.translate(selectedMyanmarDate.getMonthName(Language.ENGLISH))).append(" ");
        if (selectedMyanmarDate.getMoonPhaseValue() == 1 || selectedMyanmarDate.getMoonPhaseValue() == 3) {
            sb.append(selectedMyanmarDate.getMoonPhase()).append("။");
        } else {
            sb.append(selectedMyanmarDate.getMoonPhase()).append(" ");
            sb.append(selectedMyanmarDate.getFortnightDay()).append(" ");
            sb.append(selectedMyanmarDate.getMoonPhaseValue() == 0 ? " ဝၼ်း" : "").append(selectedMyanmarDate.getMoonPhaseValue() == 2 ? " ၶမ်ႈ။" : "။");

        }

        return sb.toString();
    }

}