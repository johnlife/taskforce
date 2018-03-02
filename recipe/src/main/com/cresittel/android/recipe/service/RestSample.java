package com.cresittel.android.recipe.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.johnlife.lifetools.tools.StringUtil;

import com.cresittel.android.recipe.data.Recipe;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public interface RestSample {
    @GET(".") //url of a call.     @Query("i") - is the parameter name in query part of the url, value will be set to string value
    Call<Recipe.ListWrapper> getRecipes(@Query("i") String ingridients);
}
