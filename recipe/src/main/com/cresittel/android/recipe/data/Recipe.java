package com.cresittel.android.recipe.data;

import java.util.Collections;
import java.util.List;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public class Recipe extends JsonData {
    /**
     * I've moved it here to remove it from top-level, since it will never used from anywhere else
     * It's just a wrapper for a network call, so no need to expose it
     *
     * Important: the class should be _public_ and _static_ to work
     */
    public static class ListWrapper extends JsonData {
        private String title;//: "Recipe Puppy",
        private String version;//: 0.1,
        private String href;//: "http://www.recipepuppy.com/",
        private List<Recipe> results;

        public List<Recipe> getList() {
            return Collections.unmodifiableList(results);
        }
    }



    private String title; //"Antipasto Salad ",
    private String href; //"http://www.kraftfoods.com/kf/recipes/antipasto-salad-54860.aspx",
    private String ingredients; //"ranch dressing, dijon mustard, lettuce, ham, swiss cheese, salami, tomato, olives",
    private String thumbnail; // "http://img.recipepuppy.com/632448.jpg"

    public String getTitle() {
        return title.trim();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getHref() {
        return href;
    }

    public String getIngredients() {
        return ingredients;
    }
}
