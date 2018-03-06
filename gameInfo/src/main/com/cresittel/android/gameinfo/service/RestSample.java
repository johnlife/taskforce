package com.cresittel.android.gameinfo.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.cresittel.android.gameinfo.data.FluTrack;

import java.util.List;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public interface RestSample {
    @GET(".")
    Call<List<FluTrack>> getFevers(@Query("s") String fevers);

}
