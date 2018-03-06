package com.cresittel.android.ip2country.data;

import android.net.wifi.aware.SubscribeConfig;

import com.cresittel.android.ip2country.R;

import ru.johnlife.lifetools.data.AbstractData;
import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public class CountryInfo extends JsonData {
    private String status;
    private SubCountryInfo data;

    public SubCountryInfo getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public class SubCountryInfo extends JsonData
    {
        private String ipv4; // "8.8.8.8",
        private String continent_name; // "North America",
        private String country_name; // "United States",
        private String subdivision_1_name; // "California",
        private String subdivision_2_name; // ""
        private String city_name; // "Mountain View",
        private String latitude; // "37.38600",
        private String longitude; // "-122.08380"

        private String Unkwn = "Unknown";
        public String getContinent_name() {
            return (continent_name != null)? continent_name:Unkwn;
        }

        public String getCountry_name() {
            return (country_name != null)? country_name:Unkwn;
        }

        public String getSubdivision_1_name() {
            return (subdivision_1_name != null)? subdivision_1_name:Unkwn;
        }

        public String getCity_name() {
            return (city_name != null)? city_name:Unkwn;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getIpv4() {
            return  ipv4;
        }
    }
}



