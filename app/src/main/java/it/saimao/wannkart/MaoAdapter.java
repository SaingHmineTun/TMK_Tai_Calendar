package it.saimao.wannkart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MaoAdapter extends BaseAdapter {

    private final String[] stringsAsk;
    private final String[] stringsValues;
    private final int[] icons;
    private final LayoutInflater inflater;


    public MaoAdapter(Context context, String[] stringsAsk, String[] stringsValues, int[] icons) {
        this.stringsAsk = stringsAsk;
        this.stringsValues = stringsValues;
        this.icons = icons;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stringsValues.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_list_view, null);
        TextView txtAsk = view.findViewById(R.id.textAsk);
        TextView txtValue = view.findViewById(R.id.textValue);
        ImageView icon = view.findViewById(R.id.iconView);
        txtAsk.setText(stringsAsk[i]);
        txtValue.setText(stringsValues[i]);
        icon.setImageResource(icons[i]);
        return view;
    }
}
