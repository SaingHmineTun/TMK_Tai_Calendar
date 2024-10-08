package it.saimao.tmktaicalendar.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.databinding.ActivityHomeBinding;
import it.saimao.tmktaicalendar.databinding.DialogSearchByWanTaiBinding;
import it.saimao.tmktaicalendar.fragments.CalendarFragment;
import it.saimao.tmktaicalendar.fragments.HolidaysFragment;
import it.saimao.tmktaicalendar.fragments.NoteListFragment;
import it.saimao.tmktaicalendar.fragments.PakpiFragment;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;
import it.saimao.tmktaicalendar.utils.ShanDate;

public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding binding;
    private Fragment calendarFragment, holidaysFragment, noteListFragment, pakpiFragment, activeFragment;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initNavigationDrawer();
    }

    private ActionBarDrawerToggle toggle;

    private void initNavigationDrawer() {

        // Setup ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, binding.getRoot(), R.string.open_drawer, R.string.close_drawer);
        binding.getRoot().addDrawerListener(toggle);
        toggle.syncState();

        binding.ibDrawer.setOnClickListener(v -> {
            binding.getRoot().openDrawer(GravityCompat.START);
        });


        // Handle navigation item clicks
        binding.navView.setNavigationItemSelectedListener(item -> {
            Fragment fragment;
            if (item.getItemId() == R.id.nav_notes) {
                if (noteListFragment == null) noteListFragment = new NoteListFragment();
                fragment = noteListFragment;
                replaceFragment(fragment);
            } else if (item.getItemId() == R.id.nav_pakpi) {
                if (pakpiFragment == null) pakpiFragment = new PakpiFragment();
                fragment = pakpiFragment;
                replaceFragment(fragment);
            } else if (item.getItemId() == R.id.nav_holidays) {
                if (holidaysFragment == null) holidaysFragment = new HolidaysFragment();
                fragment = holidaysFragment;
                replaceFragment(fragment);
            } else if (item.getItemId() == R.id.nav_home) {
                if (calendarFragment == null) calendarFragment = new CalendarFragment();
                fragment = calendarFragment;
                replaceFragment(fragment);
            } else if (item.getItemId() == R.id.nav_byEng) {
                showDatePicker();
                binding.getRoot().closeDrawer(GravityCompat.START);

            } else if (item.getItemId() == R.id.nav_byShan) {
                showShanDatePicker();
                binding.getRoot().closeDrawer(GravityCompat.START);
            }


            return true;
        });


        binding.getRoot().closeDrawer(GravityCompat.START);

        calendarFragment = new CalendarFragment();
        replaceFragment(calendarFragment);

    }

    private DialogSearchByWanTaiBinding datePickerBinding;
    private AlertDialog shanDatePickerDialog;

    private void showShanDatePicker() {
        if (datePickerBinding == null || shanDatePickerDialog == null) {


            datePickerBinding = DialogSearchByWanTaiBinding.inflate(LayoutInflater.from(this));
            datePickerBinding.ibSubYear.setOnClickListener(view -> {
                int year = Integer.parseInt(datePickerBinding.spYear.getText().toString());
                year--;
                datePickerBinding.spYear.setText(String.valueOf(year));
            });
            datePickerBinding.ibAddYear.setOnClickListener(view -> {
                int year = Integer.parseInt(datePickerBinding.spYear.getText().toString());
                year++;
                datePickerBinding.spYear.setText(String.valueOf(year));
            });
            datePickerBinding.ibSubMonth.setOnClickListener(view -> {
                String month = datePickerBinding.spMonth.getText().toString();
                int monthInt = ShanDate.getShanMonthValueByKey(month);
                if (monthInt == 1) monthInt = 13;
                monthInt--;
                datePickerBinding.spMonth.setText(ShanDate.getShanMonthByKey(monthInt));
            });
            datePickerBinding.ibAddMonth.setOnClickListener(view -> {
                String month = datePickerBinding.spMonth.getText().toString();
                int monthInt = ShanDate.getShanMonthValueByKey(month);
                if (monthInt == 12) monthInt = 0;
                monthInt++;
                datePickerBinding.spMonth.setText(ShanDate.getShanMonthByKey(monthInt));
            });
            datePickerBinding.ibSubDay.setOnClickListener(view -> {
                int day = Integer.parseInt(datePickerBinding.spDay.getText().toString());
                day--;
                datePickerBinding.spDay.setText(String.valueOf(day));
            });
            datePickerBinding.ibAddDay.setOnClickListener(view -> {
                int day = Integer.parseInt(datePickerBinding.spDay.getText().toString());
                day++;
                datePickerBinding.spDay.setText(String.valueOf(day));
            });


            var dialogBuilder = new AlertDialog.Builder(this)
                    .setView(datePickerBinding.getRoot());
            shanDatePickerDialog = dialogBuilder.create();
        }

        LocalDate currentDate = getCurrentDate();
        ShanDate shanDate = new ShanDate(MyanmarDate.of(currentDate));

        initDatePickerUi(shanDate);


        shanDatePickerDialog.show();

    }

    private void initDatePickerUi(ShanDate shanDate) {

        datePickerBinding.spYear.setText(String.valueOf(shanDate.getShanYear()));


        datePickerBinding.spMonth.setText(String.valueOf(shanDate.getShanMonthString()));


        datePickerBinding.spDay.setText(String.valueOf(shanDate.getShanDay()));


    }

    private LocalDate getCurrentDate() {
        if (activeFragment instanceof CalendarFragment frg)
            return CalendarFragment.currentDate;
        else if (activeFragment instanceof PakpiFragment frg)
            return CalendarFragment.currentDate;
        else
            return LocalDate.now();
    }

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                LocalDate currentDate = LocalDate.of(year, month + 1, day);
                if (activeFragment instanceof CalendarFragment frg) {
                    frg.onDateChanged(currentDate);
                } else if (activeFragment instanceof PakpiFragment frg) {
                    frg.onDateChanged(currentDate);
                }

            });
        }
//        if (currentDate != null)
//            datePickerDialog.updateDate(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
        datePickerDialog.show();
    }

    private void replaceFragment(@NotNull Fragment fragment) {
        activeFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.content_frame, fragment);
        transaction.commit();
        transaction.runOnCommit(() -> {
            binding.getRoot().closeDrawer(GravityCompat.START);
        });
    }

    public void goNoteDetail(LocalDate date) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void goNoteDetailByNote(Note note) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    public void goAddNote(LocalDate date) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }

}