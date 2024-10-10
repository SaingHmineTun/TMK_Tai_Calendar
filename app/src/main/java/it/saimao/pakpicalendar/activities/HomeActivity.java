package it.saimao.pakpicalendar.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.databinding.ActivityHomeBinding;
import it.saimao.pakpicalendar.databinding.DialogSearchByWanTaiBinding;
import it.saimao.pakpicalendar.fragments.CalendarFragment;
import it.saimao.pakpicalendar.fragments.HolidaysFragment;
import it.saimao.pakpicalendar.fragments.NoteListFragment;
import it.saimao.pakpicalendar.fragments.PakpiFragment;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.LanguageTranslator;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;
import it.saimao.pakpicalendar.utils.CalendarType;
import it.saimao.pakpicalendar.utils.PrefManager;
import it.saimao.pakpicalendar.utils.ShanDate;

public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding binding;
    private CalendarFragment calendarFragment;
    private HolidaysFragment holidaysFragment;
    private NoteListFragment noteListFragment;
    private PakpiFragment pakpiFragment;
    private Fragment activeFragment;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initNavigationDrawer();
        initListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(this, PakpiAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Get all widget IDs to update
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, PakpiAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        // Send broadcast to update the widget
        sendBroadcast(intent);
    }

    private void initListener() {
        binding.ibAbout.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        });
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
            LocalDate date = getCurrentDate();
            Fragment fragment;
            // Go to Note list fragment
            if (item.getItemId() == R.id.nav_notes) {
                if (noteListFragment == null) noteListFragment = new NoteListFragment();
                fragment = noteListFragment;
                replaceFragment(fragment);
            }
            // Go to Pakpi Fragment
            else if (item.getItemId() == R.id.nav_pakpi) {
                if (pakpiFragment == null) pakpiFragment = new PakpiFragment();
                fragment = pakpiFragment;
                replaceFragment(fragment);
                pakpiFragment.setCurrentDate(date);
                PrefManager.saveCalendarType(this, CalendarType.PAKPI);
            }
            // Go to Holidays Fragment
            else if (item.getItemId() == R.id.nav_holidays) {
                if (holidaysFragment == null) holidaysFragment = new HolidaysFragment();
                fragment = holidaysFragment;
                replaceFragment(fragment);
            }
            // Go to Calendar Fragment
            else if (item.getItemId() == R.id.nav_home) {
                if (calendarFragment == null) calendarFragment = new CalendarFragment();
                fragment = calendarFragment;
                replaceFragment(fragment);
                calendarFragment.setCurrentDate(date);
                PrefManager.saveCalendarType(this, CalendarType.NORMAL);
            }
            // Search by english date
            else if (item.getItemId() == R.id.nav_byEng) {
                if (activeFragment instanceof CalendarFragment || activeFragment instanceof PakpiFragment) {
                    showDatePicker();
                    binding.getRoot().closeDrawer(GravityCompat.START);
                } else {
                    Toast.makeText(this, "လွင်ႈၶူၼ်ႉႁႃၼႆႉ ၸႂ်ႉလႆႈတီႈ ပၵ်းပီႊၵူၺ်း", Toast.LENGTH_SHORT).show();
                }

            }
            // Search by myanmar date
            else if (item.getItemId() == R.id.nav_byShan) {
                if (activeFragment instanceof CalendarFragment || activeFragment instanceof PakpiFragment) {
                    showShanDatePicker();
                    binding.getRoot().closeDrawer(GravityCompat.START);
                } else {
                    Toast.makeText(this, "လွင်ႈၶူၼ်ႉႁႃၼႆႉ ၸႂ်ႉလႆႈတီႈ ပၵ်းပီႊၵူၺ်း", Toast.LENGTH_SHORT).show();
                }
            }
            // Go to TMK facebook page
            else if (item.getItemId() == R.id.nav_facebook) {
                Intent intent;
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100377671433172"));
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/100377671433172"));
                }
                startActivity(intent);
                binding.getRoot().closeDrawer(GravityCompat.START);
            }
            // Go to TMK email
            else if (item.getItemId() == R.id.nav_email) {

                Intent intent;

                String to = "tmk.muse@gmail.com";
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                binding.getRoot().closeDrawer(GravityCompat.START);
            }
            // Go to IT Sai Mao website
            else if (item.getItemId() == R.id.nav_website) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://itsaimao.wordpress.com/")));
                binding.getRoot().closeDrawer(GravityCompat.START);
            }
            // Rate this app
            else if (item.getItemId() == R.id.nav_rating) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=it.saimao.pakpicalendar")));
                binding.getRoot().closeDrawer(GravityCompat.START);
            }


            return true;
        });


        binding.getRoot().closeDrawer(GravityCompat.START);

        if (PrefManager.getCalendarType(this) == CalendarType.NORMAL) {
            calendarFragment = new CalendarFragment();
            replaceFragment(calendarFragment);
        } else if (PrefManager.getCalendarType(this) == CalendarType.PAKPI) {
            pakpiFragment = new PakpiFragment();
            replaceFragment(pakpiFragment);
        }

    }


    private DialogSearchByWanTaiBinding datePickerBinding;
    private AlertDialog shanDatePickerDialog;

    private MyanmarDate getMyanmarDate() {
        int year = (int) datePickerBinding.spYear.getTag();
        int month = (int) datePickerBinding.spMonth.getTag();
        int day = (int) datePickerBinding.spDay.getTag();
        return MyanmarDate.create(year, ShanDate.getMyanmarMonth(month), day);
    }

    public void showShanDatePicker() {
        if (datePickerBinding == null || shanDatePickerDialog == null) {


            datePickerBinding = DialogSearchByWanTaiBinding.inflate(LayoutInflater.from(this));
            datePickerBinding.ibSubYear.setOnClickListener(view -> {
                int year = (int) datePickerBinding.spYear.getTag();
                year--;
                datePickerBinding.spYear.setText(LanguageTranslator.translate(year, Language.TAI));
                datePickerBinding.spYear.setTag(year);
                updateShanDatePickerDialogTitle();
            });
            datePickerBinding.ibAddYear.setOnClickListener(view -> {
                int year = (int) datePickerBinding.spYear.getTag();
                year++;
                datePickerBinding.spYear.setText(LanguageTranslator.translate(year, Language.TAI));
                datePickerBinding.spYear.setTag(year);
                updateShanDatePickerDialogTitle();
            });
            datePickerBinding.ibSubMonth.setOnClickListener(view -> {
                int month = (int) datePickerBinding.spMonth.getTag();
                if (month == 1) month = 12;
                else month--;
                datePickerBinding.spMonth.setText(ShanDate.getShanMonthByKey(month));
                datePickerBinding.spMonth.setTag(month);
                updateShanDatePickerDialogTitle();
            });
            datePickerBinding.ibAddMonth.setOnClickListener(view -> {
                int month = (int) datePickerBinding.spMonth.getTag();
                if (month == 12) month = 1;
                else month++;
                datePickerBinding.spMonth.setText(ShanDate.getShanMonthByKey(month));
                datePickerBinding.spMonth.setTag(month);
                if ((int) datePickerBinding.spDay.getTag() > getMyanmarDate().lengthOfMonth()) {
                    datePickerBinding.spDay.setText(String.valueOf(getMyanmarDate().lengthOfMonth()));
                    datePickerBinding.spDay.setTag(getMyanmarDate().lengthOfMonth());
                }
                updateShanDatePickerDialogTitle();
            });
            datePickerBinding.ibSubDay.setOnClickListener(view -> {
                int day = (int) datePickerBinding.spDay.getTag();

                if (day <= 1) day = getMyanmarDate().lengthOfMonth();
                else day--;
                datePickerBinding.spDay.setText(LanguageTranslator.translate(day, Language.TAI));
                datePickerBinding.spDay.setTag(day);
                updateShanDatePickerDialogTitle();
            });
            datePickerBinding.ibAddDay.setOnClickListener(view -> {
                int day = (int) datePickerBinding.spDay.getTag();

                if (day >= getMyanmarDate().lengthOfMonth()) day = 1;
                else day++;
                datePickerBinding.spDay.setText(LanguageTranslator.translate(day, Language.TAI));
                datePickerBinding.spDay.setTag(day);
                updateShanDatePickerDialogTitle();
            });

            datePickerBinding.btSearch.setOnClickListener(view -> {
                int year = (int) datePickerBinding.spYear.getTag();
                int month = (int) datePickerBinding.spMonth.getTag();
                int day = (int) datePickerBinding.spDay.getTag();
                // Create MyanmarDate from current year, month and day
                MyanmarDate myDate = MyanmarDate.create(year, ShanDate.getMyanmarMonth(month), day);
                LocalDate currentDate = myDate.toMyanmarLocalDate();
                // Convert MyanmarDate to LocalDate
                if (activeFragment instanceof CalendarFragment frg) {
                    frg.onDateChanged(currentDate);
                } else if (activeFragment instanceof PakpiFragment frg) {
                    frg.onDateChanged(currentDate);
                }
                shanDatePickerDialog.cancel();

            });

            var dialogBuilder = new AlertDialog.Builder(this)
                    .setView(datePickerBinding.getRoot());
            shanDatePickerDialog = dialogBuilder.create();
        }

        LocalDate currentDate = getCurrentDate();
        MyanmarDate myanmarDate = MyanmarDate.of(currentDate);

        initShanDatePickerUi(myanmarDate);

        shanDatePickerDialog.show();

    }

    private void updateShanDatePickerDialogTitle() {
        MyanmarDate myanmarDate = getMyanmarDate();
        String title = ShanDate.format(myanmarDate);

        datePickerBinding.tvTitle.setText(title);
    }

    private void initShanDatePickerUi(MyanmarDate myanmarDate) {

        datePickerBinding.spYear.setText(myanmarDate.getYear());
        datePickerBinding.spYear.setTag(myanmarDate.getYearValue());

        ShanDate shanDate = new ShanDate(myanmarDate);
        datePickerBinding.spMonth.setText(shanDate.getShanMonthString());
        datePickerBinding.spMonth.setTag(shanDate.getShanMonth());

        datePickerBinding.spDay.setText(LanguageTranslator.translate(myanmarDate.getDayOfMonth(), Language.TAI));
        datePickerBinding.spDay.setTag(myanmarDate.getDayOfMonth());

        updateShanDatePickerDialogTitle();


    }

    private LocalDate getCurrentDate() {
        if (activeFragment instanceof CalendarFragment frg)
            return frg.currentDate;
        else if (activeFragment instanceof PakpiFragment frg)
            return frg.currentDate;
        else
            return LocalDate.now();
    }

    public void showDatePicker() {
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
        updateAppTitle();
    }

    private void updateAppTitle() {

        if (activeFragment == calendarFragment) {
            binding.appTitle.setText(R.string.pakpi_tai);
        } else if (activeFragment == pakpiFragment) {
            binding.appTitle.setText(R.string.pakpi_tai60);
        } else if (activeFragment == holidaysFragment) {
            binding.appTitle.setText(R.string.holidays);
        } else if (activeFragment == noteListFragment) {
            binding.appTitle.setText(R.string.notes);
        }
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

    public void viewHoliday(LocalDate localDate) {
        calendarFragment.setCurrentDate(localDate);
        replaceFragment(calendarFragment);
    }
}