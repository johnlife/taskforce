package com.cresittel.android.gameinfo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cresittel.android.gameinfo.Constants;
import com.cresittel.android.gameinfo.R;
import com.cresittel.android.gameinfo.data.FluTrack;
import com.cresittel.android.gameinfo.events.FluTrackListEvent;
import com.mapbox.mapboxsdk.maps.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.johnlife.lifetools.adapter.BaseAdapter;
import ru.johnlife.lifetools.fragment.BaseListFragment;

/**
 * Created by Encore on 2/25/2018.
 */

public class FluTrackListFragment extends BaseListFragment<FluTrack> {

    @Override
    protected BaseAdapter<FluTrack> instantiateAdapter(Context context) {
        return new BaseAdapter<FluTrack>(R.layout.item_flu) {
            @Override
            protected ViewHolder<FluTrack> createViewHolder(View view) {
                return new ViewHolder<FluTrack>(view) {
                    private TextView userName = view.findViewById(R.id.userName);
                    private TextView tweetText = view.findViewById(R.id.tweetText);

                    {   //this code will be called once for a layout creation
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FluTrack item = getItem();
                                changeFragment(
                                        new FluTrackInfoFragment()
                                                .addParam(Constants.PARAM_USERNAME, item.getUser_name())
                                                .addParam(Constants.PARAM_TWEET_TEXT, item.getTweet_text())
                                                .addParam(Constants.PARAM_LATITUDE, item.getLatitude())
                                                .addParam(Constants.PARAM_LONGITUDE, item.getLongitude())

                                        , true);
                            }
                        });
                    }


                    @Override
                    protected void hold(FluTrack item) {
                        userName.setText(item.getUser_name());
                        tweetText.setText(item.getTweet_text());
                    }
                };
            }
        };
    }


    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //important to call super.createView to let the magic happen
        View view = super.createView(inflater, container, savedInstanceState);

        //important to register this fragment for Events from EventBus,
        // otherwise the fragment will not catch those data from Business Layer
        EventBus.getDefault().register(this);

        return view;
    }



    @Override
    public void onDestroyView() {

        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
    @Override
    protected String getTitle(Resources r) {
        return r.getString(R.string.app_name);
    }

    @Override
    protected AppBarLayout getToolbar(LayoutInflater inflater, ViewGroup container) {
        return defaultToolbar();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRecipeList(FluTrackListEvent event) {
        getAdapter().set(event.getData());
         }
}
