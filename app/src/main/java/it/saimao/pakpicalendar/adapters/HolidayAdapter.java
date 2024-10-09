package it.saimao.pakpicalendar.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import it.saimao.pakpicalendar.databinding.AdapterHolidayBinding;
import it.saimao.pakpicalendar.utils.ShanDate;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder> {

    public interface EditHolidayListener {
        void onEditHoliday(LocalDate localDate);
    }

    public interface ViewHolidayListener {
        void onViewHoliday(LocalDate localDate);
    }

    private List<Holiday> holidays;
    private final EditHolidayListener editListener;
    private final ViewHolidayListener viewListener;

    public HolidayAdapter(EditHolidayListener editListener, ViewHolidayListener viewListener) {
        this.editListener = editListener;
        this.viewListener = viewListener;
        this.holidays = new ArrayList<>();
    }

    @NonNull
    @Override
    public HolidayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = AdapterHolidayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HolidayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayViewHolder holder, int position) {

        Holiday holiday = holidays.get(position);
        holder.binding.tvEngDate.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(holiday.engDate()));
        holder.binding.tvMyDate.setText(ShanDate.format(holiday.myanmarDate()));
        String day = ShanDate.translate(holiday.holiday());
        if (day == null) day = holiday.holiday();
        holder.binding.tvText.setText(day);
        holder.binding.ibAddNote.setOnClickListener(view -> editListener.onEditHoliday(holiday.engDate()));
        holder.binding.ibViewDay.setOnClickListener(view -> viewListener.onViewHoliday(holiday.engDate()));

    }

    public void updateHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    public class HolidayViewHolder extends RecyclerView.ViewHolder {

        public AdapterHolidayBinding binding;

        public HolidayViewHolder(AdapterHolidayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
