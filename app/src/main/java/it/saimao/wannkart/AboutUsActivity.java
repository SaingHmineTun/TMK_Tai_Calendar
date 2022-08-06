package it.saimao.wannkart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private final String[] stringsAsk = {"E-mail:", "Facebook:"};
    private final String[] stringsValue = {"saimao.muse@gmail.com", "Sai Mao Technologies"};
    private final int[] icons = {R.drawable.ic_gmail, R.drawable.ic_facebook};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ListView simpleListView = findViewById(R.id.simpleListView);
        MaoAdapter adapter = new MaoAdapter(getBaseContext(), stringsAsk, stringsValue, icons);
        simpleListView.setOnItemClickListener(this);
        simpleListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        if (i == 1) {
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/111363950487569"));
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/111363950487569"));
            }
            startActivity(intent);
        } else if (i == 0) {
            String to = stringsValue[0];
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    }
}
