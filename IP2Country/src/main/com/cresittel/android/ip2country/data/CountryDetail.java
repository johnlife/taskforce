package com.cresittel.android.ip2country.data;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Hengly on 09-Mar-18.
 */

public class CountryDetail extends JsonData {
    private String capital;
    private String population;
    private String area;
    private String borders[];
    private String flag;
    private String pngFlag;

    public String getCapital() {
        return capital;
    }

    public String getPopulation() {
        return population;
    }

    public String getArea() {
        return area;
    }

    public String[] getBorders() {
        return borders;
    }

    public String getFlag() {
        return flag;
    }

    public String getPngFlag(){
        return "https://process.filestackapi.com/A4oUzLoieQuG6FLSnHwBfz/output=format:png/" + flag; // Return inline image
    }

    public  String getOneLineBorder(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getBorders().length; i++) {
            stringBuilder.append(getBorders()[i]);
            if(i != getBorders().length-1){
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
