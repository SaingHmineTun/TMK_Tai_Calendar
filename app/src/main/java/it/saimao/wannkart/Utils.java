package it.saimao.wannkart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class Utils {

    private static final String[] wannKartStrings = {"မူဆယ်ဈေးနေ့", "နမ့်ခမ်းဈေးနေ့", "စယ်လန့်ဈေးနေ့", "ပန်ခမ်းဈေးနေ့", "နမ့်စန့်ဈေးနေ့"};

    public static void setDayName(Activity activity, int day, String name) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(String.valueOf(day), name);
        editor.apply();
    }

    public static String getDayName(Activity activity, int day) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = wannKartStrings[day];
        return sharedPref.getString(String.valueOf(day), defaultValue);
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }




}
