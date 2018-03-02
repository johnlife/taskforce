package ru.johnlife.lifetools.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.List;

import ru.johnlife.lifetools.R;
import ru.johnlife.lifetools.fragment.BaseAbstractFragment;

/**
 * Created by yanyu on 5/12/2016.
 */
public abstract class BaseTwoPaneActivity extends BaseMainActivity {

    BaseAbstractFragment detailFragment = null;
    View masterView;
    View detailView;

    private View.OnLayoutChangeListener detailAppearingLstener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (v.getWidth() > 0) {
                v.setTranslationX(-v.getWidth());
                v.setAlpha(1);
                v.setScaleX(1);
                v.setScaleY(1);
                v.animate().setInterpolator(new DecelerateInterpolator()).translationX(0).setDuration(150).setListener(null).start();
                v.removeOnLayoutChangeListener(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterView = findViewById(getContentId());
        int detailId = getDetailId();
        detailView = findViewById(detailId);
        detailFragment = (BaseAbstractFragment) getSupportFragmentManager().findFragmentById(detailId);
        if (null != detailFragment) {
            showDetail();
        }
    }

    @Override
    protected int getMainLayoutId() {
        return R.layout.activity_two_pane;
    }

    @Override
    protected int getContentId() {
        return R.id.master;
    }

    private int getDetailId() {
        return R.id.detail;
    }

    @Override
    public void changeFragment(final BaseAbstractFragment fragment, boolean addToBack) {
        if (!addToBack) {
            super.changeFragment(fragment, false);
        } else {
            if (isDetailVisible() && isMasterVisible()) {
                detailView.animate().alpha(0).scaleX(0.6f).scaleY(0.6f).setDuration(100).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showFragmentInDetail(fragment);
                    }
                }).start();
            } else {
                showFragmentInDetail(fragment);
            }
        }
    }

    private void showFragmentInDetail(BaseAbstractFragment fragment) {
        showDetail();
        detailFragment = fragment;
        fragmentManager().beginTransaction().replace(getDetailId(), fragment).commit();
        if (isMasterVisible()) {
            detailView.addOnLayoutChangeListener(detailAppearingLstener);
        }
    }

    private void showDetail() {
        if (getWeight(detailView) == 0) {
            setWeight(detailView, 1);
            setWeight(masterView, 0);
        }
    }

    private boolean hideDetail() {
        if (getWeight(masterView) == 0) {
            setWeight(detailView, 0);
            setWeight(masterView, 1);
            fragmentManager().beginTransaction().remove(detailFragment).commit();
            detailFragment = null;
            return true;
        } else {
            return false;
        }
    }

    public boolean isMasterVisible() {
        return getWeight(masterView) == 1;
    }
    private boolean isDetailVisible() {
        return getWeight(detailView) == 1;
    }

    @Override
    protected List<BackHandler> initBackHandlers() {
        List<BackHandler> handlers = super.initBackHandlers();
        handlers.add(this::hideDetail);
        return handlers;
    }

    private float getWeight(View v) {
        LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) v.getLayoutParams();
        return layout.weight;
    }

    private void setWeight(View v, int value) {
        LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) v.getLayoutParams();
        layout.weight = value;
        v.setLayoutParams(layout);
    }

    @Override
    protected boolean shouldBeLoggedIn() {
        return false;
    }

}
