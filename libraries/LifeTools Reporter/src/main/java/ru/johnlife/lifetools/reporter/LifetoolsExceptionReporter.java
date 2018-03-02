package ru.johnlife.lifetools.reporter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LifetoolsExceptionReporter extends ExceptionReporter {
    private final static String url = "https://crashes-7efeb.firebaseio.com/";
    private static final String TYPE_CRASHES = "/crashes";
    private static final String TYPE_LOGS = "/logs";
    private static LifetoolsExceptionReporter instance = null;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
    static {
        SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    private String project;
    private String version;
    private final RequestQueue q;

    public static LifetoolsExceptionReporter getInstance(Context context) {
        if (null == instance) {
            instance = new LifetoolsExceptionReporter(context.getApplicationContext());
        }
        return instance;
    }

    private LifetoolsExceptionReporter(Context context) {
        q = Volley.newRequestQueue(context);
        this.project = context.getPackageName();
        try {
            this.version = context.getPackageManager().getPackageInfo(project, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

    }

    /**
     * Please, log only unexpected critical severe exceptions
     * @param e
     */
    public void logException(Exception e) {
        report(e, TYPE_LOGS);
    }

    public static void logIfAvailable(Exception e) {
        if (null == instance) return;
        instance.report(e, TYPE_LOGS);
    }

    @Override
    protected void report(Throwable ex) {
        report(ex, TYPE_CRASHES);
    }


    protected void report(Throwable ex, String type) {
        String url = LifetoolsExceptionReporter.url + URLEncoder.encode(project.replace('.', '˖'))+type+".json";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(getServiceParams(ex)), future, future);
        q.add(request);
        try {
            JSONObject response = future.get(5, TimeUnit.SECONDS); // this will block
        } catch (InterruptedException | ExecutionException e) {
            Log.wtf(getClass().getSimpleName(), "Cannot report crash", e);
        } catch (TimeoutException e) {}

    }

    private Map<String, String> getServiceParams(Throwable ex) {
        Map<String, String> value = new HashMap<>();
        value.put("project", project);
        value.put("version", version);
        value.put("when", SDF.format(new Date()));
        value.put("message", (ex.getMessage() == null || ex.getMessage().trim().length()<1) ? ex.getClass().getSimpleName() : ex.getMessage());
        value.put("trace", getStackTrace(ex));
        return value;
    }
}