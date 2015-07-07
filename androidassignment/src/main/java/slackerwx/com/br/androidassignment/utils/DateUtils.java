package slackerwx.com.br.androidassignment.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import slackerwx.com.br.androidassignment.Constants;

/**
 * Created by slackerwx on 07/07/15.
 */
public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    public static String timestampToStringDate(Long timestamp) {
        String formattedDate = "";

        if (timestamp != null) {
            DateFormat df = new SimpleDateFormat(Constants.DATE_PATTERN);

            formattedDate = df.format(new Date(timestamp * 1000));

        } else {
            formattedDate = "";
        }

        return formattedDate;
    }
}
