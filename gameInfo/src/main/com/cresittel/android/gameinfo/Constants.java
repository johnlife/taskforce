package com.cresittel.android.gameinfo;

import android.app.Activity;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import com.cresittel.android.gameinfo.service.BackgroundService;
import com.mapbox.mapboxsdk.maps.MapView;

public interface Constants {
    ClassConstantsProvider CLASS_CONSTANTS = new ClassConstantsProvider() {

        @Override
        public Class<? extends Activity> getLoginActivityClass() {
            throw new RuntimeException("Not yet created");
        }

        @Override
        public Class<? extends BaseBackgroundService> getBackgroundServiceClass() {
            return BackgroundService.class;
        }
    };
    String REST_BASE = "http://api.flutrack.org/";
    String PARAM_USERNAME ="user_name";
    String PARAM_TWEET_TEXT ="tweet_text";
    String PARAM_LATITUDE ="latitude";
    String PARAM_LONGITUDE ="longitude";
    String PARAM_TWEET_DATE="tweet_date";
    String PARAM_AGGRAVATION="aggravation";



   // String SAMPLE_METHOD = "getSample";

    // http://api.flutrack.org/?s=feverANDcoughORfever
}
