package it.saimao.pakpicalendar.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.fragments.NoteListFragment;
import it.saimao.pakpicalendar.utils.ShanDate;
import it.saimao.pakpicalendar.adapters.SwipeGestureListener;
import it.saimao.pakpicalendar.utils.Utils;
import it.saimao.pakpicalendar.database.AppDatabase;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.database.NoteDao;
import it.saimao.pakpicalendar.databinding.ActivityMainBinding;
import it.saimao.pakpicalendar.mmcalendar.CalendarType;
import it.saimao.pakpicalendar.mmcalendar.Config;
import it.saimao.pakpicalendar.mmcalendar.HolidayCalculator;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;

public class MainActivity extends AppCompatActivity implements SwipeGestureListener.OnSwipeListener {

    private ActivityMainBinding binding;
    private GestureDetector gestureDetector;
    private NoteDao noteDao;

    private static LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteDao = AppDatabase.getAppDatabase(this).noteDao();
        initUi();
        initNavigationDrawer();
        initListeners();
    }

    private ActionBarDrawerToggle toggle;

    private void initNavigationDrawer() {

        // Setup ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, binding.getRoot(), R.string.open_drawer, R.string.close_drawer);
        binding.getRoot().addDrawerListener(toggle);
        toggle.syncState();

        // Enable the Up button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.ibDrawer.setOnClickListener(v -> {
                binding.getRoot().openDrawer(GravityCompat.START);
        });

        // Handle navigation item clicks
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_notes) {
                    Intent it = new Intent(MainActivity.this, NoteListFragment.class);
                    startActivity(it);
                }
//                switch (item.getItemId()) {
//                    case R.id.nav_item_one:
//                        // Handle the item one action
//                        Toast.makeText(MainActivity.this, "Item one", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_item_two:
//                        // Handle the item two action
//                        Toast.makeText(MainActivity.this, "Item one", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_item_three:
//                        // Handle the item three action
//                        Toast.makeText(MainActivity.this, "Item one", Toast.LENGTH_SHORT).show();
//                        break;
//                }
                binding.getRoot().closeDrawers(); // Close the drawer
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildCalendar();
    }

    private void initUi() {
        Config.initDefault(new Config.Builder().setCalendarType(CalendarType.ENGLISH).setLanguage(Language.TAI).build());
        currentDate = LocalDate.now();
    }

    private void initListeners() {

        gestureDetector = new GestureDetector(this, new SwipeGestureListener(this));
        binding.glDate.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        binding.tvFullDate.setOnClickListener(view -> showDatePicker());

        binding.tvDetail.setOnClickListener(view -> goNoteDetail(currentDate));

        binding.tvDate.setOnClickListener(view -> {
            Snackbar.make(binding.getRoot(), "တေၵႂႃႇၶိုၼ်း ဝၼ်းမိူဝ်ႈၼႆႉႁိုဝ်ႉ?", Snackbar.LENGTH_LONG).setAction("တေၵႂႃႇ", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentDate = LocalDate.now();
                    buildCalendar();
                }
            }).show();
        });


    }


    private DatePickerDialog datePickerDialog;

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(this);
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

                View view = new View(this);
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
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
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

    public static void setLastSelectedDate(LocalDate date) {
        currentDate = date;
    }

}