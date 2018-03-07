package ru.johnlife.lifetools.service;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestService {

    private RestService() {
        //prevent creation of instances
    }

    public static <RestInterface> RestInterface create(String baseUrl, Class<RestInterface> restInterfaceClass, Interceptor... interceptors) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .followSslRedirects(true)
            .followRedirects(true);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                clientBuilder.addInterceptor(interceptor);
            }
        }

        OkHttpClient client = clientBuilder.build();
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(restInterfaceClass);
    }
}
