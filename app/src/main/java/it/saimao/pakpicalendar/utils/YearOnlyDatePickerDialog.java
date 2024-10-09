package it.saimao.pakpicalendar.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import it.saimao.pakpicalendar.R;

public class YearOnlyDatePickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2100;
    private static final int MIN_YEAR = 1900;
    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout inflater
        var inflater = getActivity().getLayoutInflater();

        var cal = Calendar.getInstance();

        var dialog = inflater.inflate(R.layout.dialog_year_only_picker, null);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        var builder = new AlertDialog.Builder(getActivity()).setView(dialog);
        var dialogPicker = builder.create();

        var btCancel = dialog.findViewById(R.id.btCancelDialog);
        btCancel.setOnClickListener(view -> dialogPicker.cancel());
        var btSearch = dialog.findViewById(R.id.btSearchDialog);
        btSearch.setOnClickListener(view -> {
            listener.onDateSet(null, yearPicker.getValue(), 0, 0);
            dialogPicker.cancel();
        });
        return dialogPicker;
    }
}