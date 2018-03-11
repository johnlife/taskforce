package com.cresittel.android.ip2country.service;

/**
 * Created by Hengly on 09-Mar-18.
 */
import com.cresittel.android.ip2country.data.CountryDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface CountryDetailRestSample {
    @GET()
    Call<CountryDetail> getCountryDetail(@Url() String url);
}
