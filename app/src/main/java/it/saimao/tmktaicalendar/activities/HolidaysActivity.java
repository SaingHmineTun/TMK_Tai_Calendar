package it.saimao.tmktaicalendar.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.adapters.NoteListAdapter;
import it.saimao.tmktaicalendar.databinding.ActivityHolidaysBinding;
import it.saimao.tmktaicalendar.mmcalendar.HolidayCalculator;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;

public class HolidaysActivity extends AppCompatActivity {


    private ActivityHolidaysBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHolidaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
//        NoteListAdapter adapter = new NoteListAdapter(HolidayCalculator.getHoliday());
//        binding.rvHolidays.setAdapter(adapter);
//        binding.rvHolidays.setLayoutManager(new LinearLayoutManager(this));
    }
}