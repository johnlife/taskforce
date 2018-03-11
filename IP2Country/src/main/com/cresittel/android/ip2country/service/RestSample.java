package com.cresittel.android.ip2country.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import com.cresittel.android.ip2country.data.CountryInfo;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public interface RestSample {
    @GET()
    Call<CountryInfo> getCountryInfo(@Url() String url);
}
