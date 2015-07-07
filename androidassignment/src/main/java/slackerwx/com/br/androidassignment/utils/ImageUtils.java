package slackerwx.com.br.androidassignment.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * Created by slackerwx on 07/07/15.
 */
public class ImageUtils {

    public static void imageViewToGrayScale(ImageView image) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        image.setColorFilter(filter);
    }
}
