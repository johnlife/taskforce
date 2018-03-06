package com.cresittel.android.gameinfo.activity;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.activity.BaseMainActivity;
import ru.johnlife.lifetools.fragment.BaseAbstractFragment;
import com.cresittel.android.gameinfo.Constants;
import com.cresittel.android.gameinfo.fragment.FluTrackListFragment;

/**
 * Created by yanyu on 5/12/2016.
 */
public class MainActivity extends BaseMainActivity {
    @Override
    protected BaseAbstractFragment createInitialFragment() {
        return new FluTrackListFragment();
    }

    @Override
    protected boolean shouldBeLoggedIn() {
        return false;
    }

    @Override
    protected ClassConstantsProvider getClassConstants() {
        return Constants.CLASS_CONSTANTS;
    }
}
