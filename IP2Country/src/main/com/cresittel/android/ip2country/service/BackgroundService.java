package com.cresittel.android.ip2country.service;

import android.util.Log;

import com.cresittel.android.ip2country.Constants;
import com.cresittel.android.ip2country.data.CountryInfo;
import com.cresittel.android.ip2country.events.CountryInfoEvent;
import com.cresittel.android.ip2country.events.InvalidRequestEvent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import ru.johnlife.lifetools.service.RestService;
/**
 * Created by yanyu on 5/16/2016.
 */
public class BackgroundService extends BaseBackgroundService {
    public interface Requester extends BaseBackgroundService.Requester<BackgroundService> {}
    private static BackgroundService instance = null;
    private RestSample restService = RestService.create(Constants.REST_BASE, RestSample.class);

    public static BackgroundService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected ClassConstantsProvider getClassConstants() {
        return Constants.CLASS_CONSTANTS;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    //------------------------------------------------------------------------------------------------
    //TODO: your logic here

    public void requestCountryName(String ipaddress) {
        restService.getCountryInfo(Constants.REST_BASE  + ipaddress).enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful()) {
                    CountryInfo data = response.body();
                    if ("success".equals(data.getStatus())) {
                        postData(data);
                    } else {
                        postError();
                    }
                } else {
                    postError();
                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {
                Log.e("REST", "Error: " + t);
                postError();
            }
        });
    }

    private void postData(CountryInfo data) {
        EventBus.getDefault().postSticky(new CountryInfoEvent(data));
    }

    private void postError(){
        EventBus.getDefault().post(new InvalidRequestEvent());
    }
}
