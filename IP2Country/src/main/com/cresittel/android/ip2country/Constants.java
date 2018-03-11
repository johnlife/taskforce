package com.cresittel.android.ip2country;

import android.app.Activity;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import com.cresittel.android.ip2country.service.BackgroundService;

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
    String REST_BASE = "https://usercountry.com/v1.0/json/";
    String COUNTRY_DETAIL_REST_BASE = "https://restcountries.eu/rest/v2/alpha/";
    String COUNTRY_CODE = "codes";
    String COUNTRY_NAME = "name";
    //String SAMPLE_METHOD = "getSample";
}
