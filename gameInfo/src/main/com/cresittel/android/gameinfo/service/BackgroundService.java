package com.cresittel.android.gameinfo.service;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import com.cresittel.android.gameinfo.Constants;
import com.cresittel.android.gameinfo.data.FluTrack;
import com.cresittel.android.gameinfo.events.FluTrackListEvent;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.List;

/**
 * Created by yanyu on 5/16/2016.
 */


public class BackgroundService extends BaseBackgroundService {

    public interface Requester extends BaseBackgroundService.Requester<BackgroundService> {}
    private static BackgroundService instance = null;
    private RestSample restService;

    public static BackgroundService getInstance() {
        return instance;
    }

        @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        initializeRestService();
        requestFlu("feverANDcoughORfever");
    }



    @Override
    protected ClassConstantsProvider getClassConstants() {
        return Constants.CLASS_CONSTANTS;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    private void initializeRestService() {

        OkHttpClient client = new OkHttpClient.Builder()
            .followSslRedirects(true)
            .followRedirects(true)
            .build();
        restService = new Retrofit.Builder()
            .baseUrl(Constants.REST_BASE)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(RestSample.class);
       Mapbox.getInstance(this,"pk.eyJ1IjoiZW5jb3JlMzY1IiwiYSI6ImNqZTZ4c2JzMTA1NXAycHFuM2xidjZ6eWcifQ.76qtIzZrxWDMi7r70Mkq9w");

    }

    //------------------------------------------------------------------------------------------------
    //TODO: your logic here

    public void requestFlu(String fever) {
        restService.getFevers(fever).enqueue(new Callback<List<FluTrack>>() {
            @Override
            public void onResponse(Call<List<FluTrack>> call, Response<List<FluTrack>> response) {
                if (response.isSuccessful()) {
                    List<FluTrack> data = response.body();
                    postData(data);
                }
            }

            @Override
            public void onFailure(Call<List<FluTrack>> call, Throwable t) {
                Log.e("REST", "Error: " + t);
            }
        });
    }

    private void postData(List<FluTrack> data) {
        EventBus.getDefault().postSticky(new FluTrackListEvent(data));
    }

}
