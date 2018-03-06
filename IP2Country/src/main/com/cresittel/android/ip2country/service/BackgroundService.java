package com.cresittel.android.ip2country.service;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import com.cresittel.android.ip2country.Constants;
import com.cresittel.android.ip2country.data.CountryInfo;
import com.cresittel.android.ip2country.events.CountryInfoEvent;
import com.cresittel.android.ip2country.events.InvalidRequestEvent;
import com.cresittel.android.ip2country.fragment.CountryInfoFragment;
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
        //TODO: Uncomment if you need to install play services HTTPS handler
//        try {
//            ProviderInstaller.installIfNeeded(getApplicationContext());
//        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//            Log.w(getClass().getSimpleName(), "Play Services Error", e);
//        }
        OkHttpClient client = new OkHttpClient.Builder()
            .followSslRedirects(true)
            .followRedirects(true)
            //TODO: Uncomment if you need any custom headers
//            .addInterceptor(chain -> {
//                Request request = chain.request().newBuilder().addHeader("User-Agent", Constants.USER_AGENT).build();
//                return chain.proceed(request);
//            })
            .build();
        restService = new Retrofit.Builder()
            .baseUrl(Constants.REST_BASE)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(RestSample.class);
    }

    //------------------------------------------------------------------------------------------------
    //TODO: your logic here

    public void requestCountryName(String ipaddress) {
        restService.getCountryInfo(Constants.REST_BASE  + ipaddress).enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                if (response.isSuccessful()) {
                    CountryInfo data = response.body();
                    if(data.getStatus().equals("success")) {
                        postData(data); }
                    else {
                        postError(); }
                }
                else {
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
