package slackerwx.com.br.androidassignment.utils;

import android.content.SharedPreferences;

import slackerwx.com.br.androidassignment.Constants;
import slackerwx.com.br.androidassignment.config.MyApplication;

/**
 * Created by slackerwx on 07/07/15.
 */
public class SharedPreferencesUtils {

    public static void putLatLngAtual(String latLngAtual) {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.LAT_LNG_ATUAL, latLngAtual);
        editor.commit();
    }

    public static String getLatLngAtual() {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);

        String latLngAtual = prefs.getString(Constants.LAT_LNG_ATUAL, "0 0");

        return latLngAtual;
    }
}
