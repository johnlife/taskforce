package com.cresittel.android.ip2country.data;

import java.text.DecimalFormat;
import java.util.Locale;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Hengly on 09-Mar-18.
 */

public class CountryDetail extends JsonData {
    private String capital;
    private long population;
    private long area;
    private String borders[];
    private String flag;
    private String pngFlag;

    private static DecimalFormat myFormatter = new DecimalFormat("###,###,###,###");
    public String getCapital() {
        return capital;
    }

    public String getPopulation() {
        return myFormatter.format(population);
    }

    public String getArea() {
        return myFormatter.format(area);
    }

    public String[] getBorders() {
        return borders;
    }

    public String getFlag() {
        return flag;
    }

    public String getPngFlag(){
        return "https://process.filestackapi.com/A4oUzLoieQuG6FLSnHwBfz/output=format:png/resize=width:625/" + flag; // Return inline image
    }

    public  String getOneLineBorder(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < borders.length; i++) {
            stringBuilder.append(borders[i]);
            if(i != borders.length-1){
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
