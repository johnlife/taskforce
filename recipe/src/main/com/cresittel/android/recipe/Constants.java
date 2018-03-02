package com.cresittel.android.recipe;

import android.app.Activity;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;
import com.cresittel.android.recipe.service.BackgroundService;

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
    String REST_BASE = "http://www.recipepuppy.com/api/";
    String PARAM_TITLE = "title";
    String PARAM_URL = "url";
    String PARAM_INGREDIENTS = "ingredients";
    String PARAM_THUMB = "thumb";
}
