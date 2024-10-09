package it.saimao.tmktaicalendar.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import it.saimao.tmktaicalendar.activities.HomeActivity;
import it.saimao.tmktaicalendar.adapters.Holiday;
import it.saimao.tmktaicalendar.adapters.HolidayAdapter;
import it.saimao.tmktaicalendar.databinding.FragmentHolidaysBinding;
import it.saimao.tmktaicalendar.mmcalendar.Language;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;
import it.saimao.tmktaicalendar.utils.ShanDate;
import it.saimao.tmktaicalendar.utils.YearOnlyDatePickerDialog;

public class HolidaysFragment extends Fragment implements HolidayAdapter.EditHolidayListener, HolidayAdapter.ViewHolidayListener, DatePickerDialog.OnDateSetListener {


    private FragmentHolidaysBinding binding;
    private HolidayAdapter holidayAdapter;
    private int year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHolidaysBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        initListeners();
    }

    private void initListeners() {
        binding.tvYear.setOnClickListener(view -> showYearOnlyDatePicker());
        binding.ibPrevYear.setOnClickListener(view -> {
            year--;
            updateUi();
        });
        binding.ibNextYear.setOnClickListener(view -> {
            year++;
            updateUi();
        });
    }

    private YearOnlyDatePickerDialog datePickerDialog;

    private void showYearOnlyDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new YearOnlyDatePickerDialog();
            datePickerDialog.setListener(this);
        }
        datePickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
    }

    private void initUi() {

        holidayAdapter = new HolidayAdapter(this, this);
        binding.rvHolidays.setAdapter(holidayAdapter);
        binding.rvHolidays.setLayoutManager(new LinearLayoutManager(getContext()));
        year = LocalDate.now().getYear();
        updateUi();

    }

    private void updateUi() {
        List<Holiday> holidays = new ArrayList<>();
        LocalDate start = LocalDate.of(year, 1, 1);
        while (start.getYear() == year) {
            MyanmarDate md = MyanmarDate.of(start);
            for (String str : ShanDate.getHolidays(MyanmarDate.of(start))) {
                holidays.add(new Holiday(str, start, md));
            }
            start = start.plusDays(1);
        }
        holidayAdapter.updateHolidays(holidays);
        binding.tvYear.setText(String.valueOf(year));
    }


    @Override
    public void onEditHoliday(LocalDate localDate) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.goNoteDetail(localDate);
    }

    @Override
    public void onViewHoliday(LocalDate localDate) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.viewHoliday(localDate);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        updateUi();
    }
}