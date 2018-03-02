package ru.johnlife.lifetools.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by Yan Yurkin
 * 19 November 2017
 */

public class ScaleBitmap {
    public static Bitmap maintainAspect(Bitmap source, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, source.getWidth(), source.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
    }

    public static Bitmap toLongest(Bitmap source, int longest) {
        return maintainAspect(source, longest, longest);
    }

    public static Bitmap toWidth(Bitmap source, int width) {
        return maintainAspect(source, width, Integer.MAX_VALUE);
    }

    public static Bitmap toHeight(Bitmap source, int height) {
        return maintainAspect(source, Integer.MAX_VALUE, height);
    }
}
