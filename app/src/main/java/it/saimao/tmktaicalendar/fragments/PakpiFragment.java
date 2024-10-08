package it.saimao.tmktaicalendar.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.activities.HomeActivity;
import it.saimao.tmktaicalendar.activities.MainActivity;
import it.saimao.tmktaicalendar.adapters.SwipeGestureListener;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.FragmentPakpiBinding;
import it.saimao.tmktaicalendar.mmcalendar.CalendarType;
import it.saimao.tmktaicalendar.mmcalendar.Config;
import it.saimao.tmktaicalendar.mmcalendar.Language;
import it.saimao.tmktaicalendar.mmcalendar.LanguageTranslator;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;
import it.saimao.tmktaicalendar.utils.ShanDate;
import it.saimao.tmktaicalendar.utils.Utils;

public class PakpiFragment extends Fragment implements SwipeGestureListener.OnSwipeListener {

    private FragmentPakpiBinding binding;

    private GestureDetector gestureDetector;
    private NoteDao noteDao;

    public LocalDate currentDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPakpiBinding.inflate(inflater, container, false);
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

//        binding.tvFullDate.setOnClickListener(view -> showDatePicker());

        binding.tvDetail.setOnClickListener(view -> goNoteDetail(currentDate));

        binding.tvDate.setOnClickListener(view -> {
            Snackbar.make(binding.getRoot(), "တေၵႂႃႇၶိုၼ်း ဝၼ်းမိူဝ်ႈၼႆႉႁိုဝ်ႉ?", Snackbar.LENGTH_LONG).setAction("တေၵႂႃႇ", view1 -> {
                currentDate = LocalDate.now();
                buildCalendar();
            }).show();
        });


    }


    // Custom GestureListener for detecting swipes


    public void onSwipeRight() {

        // PREV MONTH
        currentDate = currentDate.minusDays(MyanmarDate.of(currentDate).getDayOfMonth());
        buildCalendar();
    }

    public void onSwipeLeft() {
        // NEXT
        MyanmarDate mDate = MyanmarDate.of(currentDate);
        if (currentDate.equals(LocalDate.now())) {
            currentDate = currentDate.plusDays(mDate.lengthOfMonth() - mDate.getDayOfMonth()).plusDays(1);
        } else {
            currentDate = currentDate.plusDays(mDate.lengthOfMonth() - mDate.getDayOfMonth() + 1);
        }
        buildCalendar();
    }

    public LocalDate getFirstDayOfMonth() {
        MyanmarDate myanmarDate = MyanmarDate.of(currentDate);

        return currentDate.minusDays(myanmarDate.getDayOfMonth() - 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buildCalendar() {
        binding.tvDate.setSelected(true);
        // Get first day of month
        LocalDate firstDayOfMonth = getFirstDayOfMonth();
        MyanmarDate firstDayOfMonthShan = MyanmarDate.of(firstDayOfMonth);
        int monthLength = firstDayOfMonthShan.lengthOfMonth();
        int dayToHide = ShanDate.getMePeeInt(firstDayOfMonth.toEpochDay());


        LocalDate date = firstDayOfMonth;
        int start = 0, end = 30;

        for (int i = start; i < end; i++) {

            RelativeLayout layout;
            layout = (RelativeLayout) binding.glDate.getChildAt(i - start);

            if (i < dayToHide) {
                layout.setVisibility(View.INVISIBLE);
            } else if (i < monthLength + dayToHide) {
                date = firstDayOfMonth.plusDays(i - dayToHide);
                customizeDate(layout, date);
            } else {
                layout.setVisibility(View.INVISIBLE);
            }
        }

        if (monthLength + dayToHide > end) {
            for (int ii = end, index = 0; ii < monthLength + dayToHide; ii++, index++) {

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
        TextView enText = (TextView) layout.getChildAt(1);
        TextView myText = (TextView) layout.getChildAt(2);

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

        myText.setText(LanguageTranslator.translate(myDate.getDayOfMonth(), Language.TAI));
        enText.setText(String.valueOf(date.getDayOfMonth()));
        mPhase.setText(ShanDate.getLukPee(date.toEpochDay()));


        if (date.isEqual(LocalDate.now())) {
            layout.setBackgroundResource(R.drawable.bg_today);
        } else {
            layout.setBackgroundResource(R.drawable.bg_item);
        }

        if (date.isEqual(currentDate)) {
            onDateClicked(layout);
        }

        if (ShanDate.getMePeeInt(date.toEpochDay()) % 5 == 0 || ShanDate.isHoliday(myDate)) {
            myText.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            myText.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
        }

        if (myDate.getMoonPhaseValue() == 1) {
            iv.setImageResource(R.drawable.full_moon);
        } else if (myDate.getMoonPhaseValue() == 3) {
            iv.setImageResource(R.drawable.new_moon);
        } else {
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
        binding.tvFullDate.setText(String.format(Locale.ENGLISH, "%02d/%02d/%04d", currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear()));

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

    public void onDateChanged(LocalDate currentDate) {
        this.currentDate = currentDate;
        buildCalendar();

    }
}
