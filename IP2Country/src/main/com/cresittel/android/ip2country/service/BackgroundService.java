package com.cresittel.android.ip2country.service;

import android.util.Log;

import com.cresittel.android.ip2country.Constants;
import com.cresittel.android.ip2country.data.CountryDetail;
import com.cresittel.android.ip2country.data.CountryInfo;
import com.cresittel.android.ip2country.events.CountryDetailEvent;
import com.cresittel.android.ip2country.events.CountryInfoEvent;
import com.cresittel.android.ip2country.events.InvalidRequestEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    private CountryDetailRestSample countryDetailRestService = RestService.create(Constants.COUNTRY_DETAIL_REST_BASE, CountryDetailRestSample.class);
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

    // Start country info request
    public void requestCountryInfo(String ipaddress) {
        restService.getCountryInfo(Constants.REST_BASE  + ipaddress + "?token=8f67425b151f8c3be00d04964f8973c5b82987f11d5ff727").enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful()) {
                    CountryInfo data = response.body();
                    postData(data);
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

    // End country info request

    // Start country detail request
    public void requestCountryDetail(String alpha3code){
        countryDetailRestService.getCountryDetail(Constants.COUNTRY_DETAIL_REST_BASE + alpha3code).enqueue(new Callback<CountryDetail>() {
            @Override
            public void onResponse(Call<CountryDetail> call, Response<CountryDetail> response) {
                if(response.isSuccessful()){
                    CountryDetail data = response.body();
                    postData(data);
                }
                else{
                    postError();
                }
            }

            @Override
            public void onFailure(Call<CountryDetail> call, Throwable t) {
                Log.e("REST", "Error: " + t);
                postError();
            }
        });
    }

    private void postData(CountryDetail data){
        EventBus.getDefault().postSticky(new CountryDetailEvent(data));
    }
    // End country detail request

    private void postError(){
        EventBus.getDefault().post(new InvalidRequestEvent());
    }
}
