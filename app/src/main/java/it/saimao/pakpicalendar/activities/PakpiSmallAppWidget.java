package it.saimao.pakpicalendar.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.RemoteViews;

import androidx.core.content.res.ResourcesCompat;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.database.AppDatabase;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.fragments.CalendarFragment;
import it.saimao.pakpicalendar.mmcalendar.HolidayCalculator;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;
import it.saimao.pakpicalendar.utils.ShanDate;
import it.saimao.pakpicalendar.utils.Utils;

/**
 * Implementation of App Widget functionality.
 */
public class PakpiSmallAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        LocalDate today = LocalDate.now();
        MyanmarDate myToday = MyanmarDate.of(today);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pakpi_small_app_widget);

        // Click on RemoteViews will go to calendar view
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.wg_main, pendingIntent);

        // For english full date
        final String fullDate = String.format(Locale.ENGLISH, "%02d/%02d/%04d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());
        views.setImageViewBitmap(R.id.wg_full_date, getFontBitmap(context, fullDate, 20));

        // for myDate
        final String myDate;
        List<Note> notes = Utils.getTodayEvents(AppDatabase.getAppDatabase(context).noteDao(), today);
        if (!notes.isEmpty())
            myDate = notes.get(0).getTitle();
        else if (HolidayCalculator.isHoliday(myToday))
            myDate = ShanDate.translate(HolidayCalculator.getHoliday(myToday).get(0));
        else
            myDate = "ဝၼ်း" + myToday.getWeekDay();

        views.setImageViewBitmap(R.id.wg_date, getFontBitmap(context, myDate, 20));

        // for detail text
        String detail = getDesc1(today);
        views.setImageViewBitmap(R.id.wg_detail, getFontBitmap(context, detail, 20));
        String detail2 = getDesc2(today);
        views.setImageViewBitmap(R.id.wg_detail2, getFontBitmap(context, detail2, 20));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String getDesc1(LocalDate date) {
        MyanmarDate selectedMyanmarDate = MyanmarDate.of(date);
        ShanDate shanDate = new ShanDate(selectedMyanmarDate);
        StringBuilder sb = new StringBuilder();

        sb.append(ShanDate.translate("Sasana Year")).append(" ").append(selectedMyanmarDate.getBuddhistEra()).append(" ဝႃႇ၊ ").append(ShanDate.translate("Myanmar Year")).append(" ").append(selectedMyanmarDate.getYear()).append(" ၶု၊ ").append("ပီႊတႆး ").append(shanDate.getShanYear()).append(" ၼီႈ။");

        return sb.toString();
    }

    private static String getDesc2(LocalDate date) {
        MyanmarDate selectedMyanmarDate = MyanmarDate.of(date);
        StringBuilder sb = new StringBuilder();


        sb.append(ShanDate.translate(selectedMyanmarDate.getMonthName(Language.ENGLISH))).append(" ");
        if (selectedMyanmarDate.getMoonPhaseValue() == 1 || selectedMyanmarDate.getMoonPhaseValue() == 3) {
            sb.append(selectedMyanmarDate.getMoonPhase()).append("။");
        } else {
            sb.append(selectedMyanmarDate.getMoonPhase()).append(" ");
            sb.append(selectedMyanmarDate.getFortnightDay()).append(" ");
            sb.append(selectedMyanmarDate.getMoonPhaseValue() == 0 ? " ဝၼ်း" : "").append(selectedMyanmarDate.getMoonPhaseValue() == 2 ? " ၶမ်ႈ၊ " : "၊ ");
        }
        if (!ShanDate.getWannTun(selectedMyanmarDate).isEmpty())
            sb.append("ဝၼ်းထုၼ်း").append("၊ ");
        if (!ShanDate.getWannPyaat(selectedMyanmarDate).isEmpty()) {
            sb.append("ဝၼ်းပျၢတ်ႈ").append("၊ ");
        }
        sb.append("ႁူဝ်ၼၵႃး ").append(ShanDate.getHoNagaa(selectedMyanmarDate)).append("။");

        return sb.toString();
    }

    public static Bitmap getFontBitmap(Context context, String text, float fontSizeSP) {
        int fontSizePX = convertDiptoPix(context, fontSizeSP);
        int pad = (fontSizePX / 9);
        Paint paint = new Paint();
        Typeface typeface = ResourcesCompat.getFont(context, R.font.aj_kunheing_etm08);
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSizePX);
        int textWidth = (int) (paint.measureText(text) + pad * 2);
        int height = (int) (fontSizePX / 0.75);
        Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float xOriginal = pad;
        canvas.drawText(text, xOriginal, fontSizePX, paint);
        return bitmap;
    }

    public static int convertDiptoPix(Context context, float dip) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return value;
    }

}