package it.saimao.pakpicalendar.fragments;

import static it.saimao.pakpicalendar.utils.Utils.description;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.activities.HomeActivity;
import it.saimao.pakpicalendar.adapters.SwipeGestureListener;
import it.saimao.pakpicalendar.database.AppDatabase;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.database.NoteDao;
import it.saimao.pakpicalendar.databinding.FragmentCalendarBinding;
import it.saimao.pakpicalendar.mmcalendar.CalendarType;
import it.saimao.pakpicalendar.mmcalendar.Config;
import it.saimao.pakpicalendar.mmcalendar.HolidayCalculator;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;
import it.saimao.pakpicalendar.utils.ShanDate;
import it.saimao.pakpicalendar.utils.Utils;

public class CalendarFragment extends Fragment implements SwipeGestureListener.OnSwipeListener {

    private FragmentCalendarBinding binding;

    private GestureDetector gestureDetector;
    private NoteDao noteDao;

    public LocalDate currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        noteDao = AppDatabase.getAppDatabase(getContext()).noteDao();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        initListeners();
    }

    @Override
    public void onStart() {
        super.onStart();

        buildCalendar();
    }


    private void initUi() {
        Config.initDefault(new Config.Builder().setCalendarType(CalendarType.ENGLISH).setLanguage(Language.TAI).build());
        if (currentDate == null) currentDate = LocalDate.now();
    }

    private void initListeners() {

        gestureDetector = new GestureDetector(getContext(), new SwipeGestureListener(this));
        binding.glDate.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });


        binding.tvDetail.setOnClickListener(view -> goNoteDetail(currentDate));


        binding.cvFullDate.setOnClickListener(view -> {
            HomeActivity activity = (HomeActivity) getActivity();
            if (activity != null) activity.showDatePicker();
        });
        binding.cvDate.setOnClickListener(view -> {
            Snackbar.make(binding.getRoot(), "တေၵႂႃႇၶိုၼ်း ဝၼ်းမိူဝ်ႈၼႆႉႁိုဝ်ႉ?", Snackbar.LENGTH_LONG).setAction("တေၵႂႃႇ", view1 -> {
                currentDate = LocalDate.now();
                buildCalendar();
            }).show();
        });


    }


    // Custom GestureListener for detecting swipes


    public void onSwipeRight() {
        currentDate = currentDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        buildCalendar();
    }

    public void onSwipeLeft() {
        currentDate = currentDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        buildCalendar();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buildCalendar() {
        binding.tvDate.setSelected(true);
        // Get first day of month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

        int dayToHide = firstDayOfMonth.getDayOfWeek().getValue(); // 1 for Monday
        int start = 0, end = 35;
        if (dayToHide == 7) {
            start += dayToHide;
            end += dayToHide;
        }

        LocalDate date = firstDayOfMonth;

        for (int i = start; i < end; i++) {

            RelativeLayout layout = (RelativeLayout) binding.glDate.getChildAt(i - start);

            if (i < dayToHide) {
                layout.setVisibility(View.INVISIBLE);
            } else if (i < firstDayOfMonth.lengthOfMonth() + dayToHide) {
                date = firstDayOfMonth.plusDays(i - dayToHide);
                customizeDate(layout, date);


            } else {
                layout.setVisibility(View.INVISIBLE);
            }
        }

        if (firstDayOfMonth.lengthOfMonth() + dayToHide > end) {
            for (int ii = end, index = 0; ii < firstDayOfMonth.lengthOfMonth() + dayToHide; ii++, index++) {

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
        TextView mPhase = (TextView) layout.getChildAt(3);

        layout.setTag(date);
        layout.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                v.performClick();
            }
            return false;
        });
        layout.setOnClickListener(this::onDateClicked);
        layout.setOnLongClickListener(this::onLongDateClicked);

        myText.setText(myDate.getFortnightDay());
        enText.setText(String.valueOf(date.getDayOfMonth()));


        if (date.isEqual(LocalDate.now())) {
            layout.setBackgroundResource(R.drawable.bg_today);

        } else {
            layout.setBackgroundResource(R.drawable.bg_item);
        }

        if (date.isEqual(currentDate)) {
            onDateClicked(layout);
        }

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || ShanDate.isHoliday(myDate)) {
            enText.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            enText.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
        }

        if (myDate.getMoonPhaseValue() == 1) {
            iv.setImageResource(R.drawable.full_moon);
            mPhase.setText(myDate.getMonthName() + myDate.getMoonPhase());
        } else if (myDate.getMoonPhaseValue() == 3) {
            mPhase.setText(myDate.getMonthName() + myDate.getMoonPhase());

            iv.setImageResource(R.drawable.new_moon);
        } else {
            mPhase.setText("");
            iv.setImageResource(0);
        }

        if (!Utils.getTodayEvents(noteDao, date).isEmpty()) {
            if (layout.getChildCount() <= 4) {

                View view = new View(getContext());
                view.setMinimumWidth(Utils.dpToPx(8));
                view.setMinimumHeight(Utils.dpToPx(8));
                view.setBackgroundResource(R.drawable.rounded_button);

                var params = new RelativeLayout.LayoutParams(Utils.dpToPx(8), Utils.dpToPx(8));

                params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                params.setMargins(Utils.dpToPx(4), 0, 0, Utils.dpToPx(8));

                layout.addView(view, params);
            }
        } else {
            if (layout.getChildCount() > 4) {
                layout.removeViewAt(layout.getChildCount() - 1);
            }
        }

    }

    private boolean onLongDateClicked(View view) {
        onDateClicked(view);
        LocalDate date = (LocalDate) view.getTag();
        goNoteDetail(date);
        return true;
    }

    private void goNoteDetail(LocalDate date) {

        HomeActivity activity = (HomeActivity) getActivity();
        activity.goNoteDetail(date);
    }


    private View prevSelectedDate;


    public void onDateClicked(View view) {
        showToastForInstruction();
        currentDate = (LocalDate) view.getTag();
        MyanmarDate myanmarDate = MyanmarDate.of(currentDate);

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
        binding.tvDay.setText(String.format(Locale.getDefault(), "%02d", currentDate.getDayOfMonth()));
        binding.tvFullDate.setText(String.format(Locale.ENGLISH, "%02d/%02d/%04d", currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear()));
        binding.enMonth.setText(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        List<Note> notes = Utils.getTodayEvents(noteDao, currentDate);
        if (!notes.isEmpty()) {
            binding.tvDate.setText(notes.get(0).getTitle());
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_error));

        } else if (ShanDate.isHoliday(myanmarDate)) {
            binding.tvDate.setText(ShanDate.getHoliday(myanmarDate));
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            binding.tvDate.setText("ဝၼ်း" + myanmarDate.getWeekDay());
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
        }

        binding.tvDetail.setText(description(currentDate));
    }

    private void showToastForInstruction() {
        if (((HomeActivity) requireActivity()).isShowLongPressToastUsage()) {
            Toast.makeText(getContext(), "ၼဵၵ်ႉပၼ်ဝၼ်းႁိုင်ႁိုင် တႃႇတေတႅမ်ႈ တီႈမၢႆတွင်းလႄႈ", Toast.LENGTH_LONG).show();
            ((HomeActivity) requireActivity()).setShowLongPressToastUsage(false);
        }
    }


    public void onDateChanged(LocalDate currentDate) {
        this.currentDate = currentDate;
        buildCalendar();
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
