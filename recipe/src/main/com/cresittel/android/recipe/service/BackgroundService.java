package com.cresittel.android.recipe.service;

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
import ru.johnlife.lifetools.tools.StringUtil;

import com.cresittel.android.recipe.Constants;
import com.cresittel.android.recipe.data.Recipe;
import com.cresittel.android.recipe.events.RecipeListEvent;

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
    }

    //------------------------------------------------------------------------------------------------
    //TODO: your logic here

    public void requestRecipes(String[] ingridients) {
        restService.getRecipes(StringUtil.implode(ingridients, ",")).enqueue(new Callback<Recipe.ListWrapper>() {
            @Override
            public void onResponse(Call<Recipe.ListWrapper> call, Response<Recipe.ListWrapper> response) {
                if (response.isSuccessful()) {
                    Recipe.ListWrapper data = response.body();
                    postData(data.getList());
                }
            }

            @Override
            public void onFailure(Call<Recipe.ListWrapper> call, Throwable t) {
                Log.e("REST", "Error: " + t);
            }
        });
    }

    private void postData(List<Recipe> data) {
        EventBus.getDefault().postSticky(new RecipeListEvent(data));
    }

}
