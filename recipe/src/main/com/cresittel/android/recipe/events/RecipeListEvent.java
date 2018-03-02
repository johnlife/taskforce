package com.cresittel.android.recipe.events;

import com.cresittel.android.recipe.data.Recipe;

import java.util.List;

/**
 * Created by User on 02/23/2018.
 */

public class RecipeListEvent {
    private List<Recipe> data;

    public RecipeListEvent(List<Recipe> data) {
        this.data = data;
    }

    public List<Recipe> getData() {
        return data;
    }
}
