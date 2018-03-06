package com.cresittel.android.gameinfo.data;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Encore on 2/25/2018.
 */

public class FluTrack extends JsonData {

    private String user_name;
    private String tweet_text;
    private String latitude;
    private String longitude;
    private String tweet_date;
    private String aggravation;

    public String getUser_name() {
        return user_name;
    }

    public String getTweet_text() {
        return tweet_text;
    }

    public String getTweet_date() {
        return tweet_date;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAggravation() {
        return aggravation;
    }
}
