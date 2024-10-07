package it.saimao.tmktaicalendar.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.activities.HomeActivity;
import it.saimao.tmktaicalendar.activities.NoteActivity;
import it.saimao.tmktaicalendar.adapters.SwipeGestureListener;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.FragmentCalendarBinding;
import it.saimao.tmktaicalendar.databinding.FragmentHolidaysBinding;
import it.saimao.tmktaicalendar.mmcalendar.CalendarType;
import it.saimao.tmktaicalendar.mmcalendar.Config;
import it.saimao.tmktaicalendar.mmcalendar.HolidayCalculator;
import it.saimao.tmktaicalendar.mmcalendar.Language;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;
import it.saimao.tmktaicalendar.utils.ShanDate;
import it.saimao.tmktaicalendar.utils.Utils;

public class CalendarFragment extends Fragment implements SwipeGestureListener.OnSwipeListener {

    private FragmentCalendarBinding binding;

    private GestureDetector gestureDetector;
    private NoteDao noteDao;

    private static LocalDate currentDate;

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
        currentDate = LocalDate.now();
    }

    private void initListeners() {

        gestureDetector = new GestureDetector(getContext(), new SwipeGestureListener(this));
        binding.glDate.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        binding.tvFullDate.setOnClickListener(view -> showDatePicker());


        binding.tvDetail.setOnClickListener(view -> goNoteDetail(currentDate));

        binding.tvDate.setOnClickListener(view -> {
            Snackbar.make(binding.getRoot(), "တေၵႂႃႇၶိုၼ်း ဝၼ်းမိူဝ်ႈၼႆႉႁိုဝ်ႉ?", Snackbar.LENGTH_LONG).setAction("တေၵႂႃႇ", view1 -> {
                currentDate = LocalDate.now();
                buildCalendar();
            }).show();
        });


    }

    private DatePickerDialog datePickerDialog;

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                currentDate = LocalDate.of(year, month + 1, day);
                buildCalendar();

            });
        }
        if (currentDate != null)
            datePickerDialog.updateDate(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
        datePickerDialog.show();
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
        TextView mPhase = (TextView) layout.getChildAt(3);

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

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || HolidayCalculator.isHoliday(myDate)) {
            enText.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            enText.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
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

        if (!Utils.getTodayEvents(noteDao, date).isEmpty()) {
            if (layout.getChildCount() <= 4) {

                View view = new View(getContext());
                view.setMinimumWidth(Utils.dpToPx(8));
                view.setMinimumHeight(Utils.dpToPx(8));
                view.setBackgroundResource(R.drawable.rounded_button);

                var params = new RelativeLayout.LayoutParams(Utils.dpToPx(8), Utils.dpToPx(8));

                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                params.setMargins(0, 0, Utils.dpToPx(4), Utils.dpToPx(4));

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

        List<Note> notes = Utils.getTodayEvents(noteDao, currentDate);
        if (!notes.isEmpty()) {
            binding.tvDate.setText(notes.get(0).getTitle());
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_error));

        } else if (HolidayCalculator.isHoliday(myanmarDate)) {
            binding.tvDate.setText(ShanDate.translate(HolidayCalculator.getHoliday(myanmarDate).get(0)));
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            binding.tvDate.setText("ဝၼ်း" + myanmarDate.getWeekDay());
            binding.tvDate.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
        }

        binding.tvDetail.setText(description(currentDate));
    }

    private String description(LocalDate date) {
        MyanmarDate selectedMyanmarDate = MyanmarDate.of(date);
        ShanDate shanDate = new ShanDate(selectedMyanmarDate);
        StringBuilder sb = new StringBuilder();


        sb.append(ShanDate.translate("Sasana Year")).append(" ").append(selectedMyanmarDate.getBuddhistEra()).append("၊ ").append(ShanDate.translate("Myanmar Year")).append(" ").append(selectedMyanmarDate.getYear()).append("၊ ").append("ပီႊတႆး ").append(shanDate.getShanYear()).append(" ၼီႈ၊ ");

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
