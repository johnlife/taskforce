package com.cresittel.android.gameinfo.activity;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.activity.BaseChildActivity;
import com.cresittel.android.gameinfo.Constants;

/**
 * Created by Yan Yurkin
 * 15 June 2016
 */
public class ChildActivity extends BaseChildActivity {
    @Override
    protected boolean shouldBeLoggedIn() {
        return false;
    }

    @Override
    protected ClassConstantsProvider getClassConstants() {
        return Constants.CLASS_CONSTANTS;
    }
}
