package it.saimao.tmktaicalendar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import it.saimao.tmktaicalendar.databinding.FragmentHolidaysBinding;

public class HolidaysFragment extends Fragment {


    private FragmentHolidaysBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHolidaysBinding.inflate(inflater, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi() {
//        NoteListAdapter adapter = new NoteListAdapter(HolidayCalculator.getHoliday());
//        binding.rvHolidays.setAdapter(adapter);
//        binding.rvHolidays.setLayoutManager(new LinearLayoutManager(this));
    }
}