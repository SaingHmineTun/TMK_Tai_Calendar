package it.saimao.tmktaicalendar.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.time.LocalDate;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.ShanDate;
import it.saimao.tmktaicalendar.databinding.ActivityNoteBinding;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;
    private LocalDate todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        Serializable ser;
        if (getIntent() != null && (ser = getIntent().getSerializableExtra("date")) != null) {
            todayDate = (LocalDate) ser;
            initData(todayDate);
        }
    }

    private void initData(LocalDate todayDate) {
        /*
        lbBuddhistYear.setText(
                "ပီႊတႆး - " + shanDate.getShanYear() + " ၼီႈ" +
                        "\nပီႊမိူင်း - " + ShanDate.getPeeMurng(shanDate.getShanYearValue()) +
                        "\nပီႊထမ်း - " + ShanDate.getPeeHtam(selectedDate.getYear())
        );
        lbYear.setText(ShanDate.translate("Sasana Year") + " - " + selectedMyanmarDate.getBuddhistEra() + "\n" +
                ShanDate.translate("Myanmar Year") + " - " + selectedMyanmarDate.getYear() + "\n" +
                ShanDate.translate("English Year") + " - " + (selectedDate.getYear() < 0 ? (Math.abs(selectedDate.getYear()) + 1) + " BC" : selectedDate.getYear()));

         */
        MyanmarDate myanmarDate = MyanmarDate.of(todayDate);
        ShanDate shanDate = new ShanDate(myanmarDate);
        binding.pTai.setText(shanDate.getShanYear());
        binding.pMurng.setText(ShanDate.getPeeMurng(shanDate.getShanYearValue()));
        binding.pHtam.setText(ShanDate.getPeeHtam(todayDate.getYear()));
        binding.pSasana.setText(myanmarDate.getBuddhistEra());
        binding.pKawza.setText(myanmarDate.getYear());
        binding.pEnglish.setText(String.valueOf(todayDate.getYear()));
        binding.description.setText(ShanDate.toString(todayDate, myanmarDate).trim());

    }
}