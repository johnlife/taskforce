package ru.johnlife.lifetools.reporter;

import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class ExceptionReporter implements Thread.UncaughtExceptionHandler {
    private static final String NN = "\n\n";
    private Thread.UncaughtExceptionHandler deh;

    public ExceptionReporter() {
        this.deh = Thread.getDefaultUncaughtExceptionHandler();
    }

    protected String getTag() {
        return getClass().getSimpleName();
    }

    @Override
    public synchronized void uncaughtException(final Thread thread, final Throwable ex) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Log.wtf(getTag(), "Crash detected in UI Thread");
        } else {
            Log.wtf(getTag(), "Crash detected in background Thread");
        }
        report(ex);
        deh.uncaughtException(thread, ex);
    }

    protected abstract void report(Throwable ex);

    public static String getStackTrace(final Throwable ex) {
        StringWriter swr = new StringWriter();
        PrintWriter pwr = new PrintWriter(swr);
        ex.printStackTrace(pwr);
        Throwable cause = ex;
        while ((cause = cause.getCause()) != null) {
            pwr.print(NN+"Caused By:\n");
            cause.printStackTrace(pwr);
        }
        String value = swr.toString();
        pwr.close();
        try {
            swr.close();
        } catch (IOException e) {
            Log.e(ExceptionReporter.class.getSimpleName(), "Exception with close swr.", e);
        }
        return value;
    }

}
