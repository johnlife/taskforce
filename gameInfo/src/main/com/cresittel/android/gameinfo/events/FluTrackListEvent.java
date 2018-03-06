package com.cresittel.android.gameinfo.events;

import com.cresittel.android.gameinfo.data.FluTrack;

import java.util.List;

/**
 * Created by Encore on 2/25/2018.
 */

public class FluTrackListEvent {
    private List<FluTrack> data;

    public FluTrackListEvent(List<FluTrack> data) {
        this.data = data;
    }
    public List<FluTrack> getData() {
        return data;
    }
}
