package com.cresittel.android.recipe.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cresittel.android.recipe.Constants;
import com.cresittel.android.recipe.R;
import com.squareup.picasso.Picasso;

import ru.johnlife.lifetools.fragment.BaseAbstractFragment;

/**
 * Created by User on 02/26/2018.
 */

public class RecipeInfoFragment extends BaseAbstractFragment implements Constants {
    @Override
    protected String getTitle(Resources r) {
        return r.getString(R.string.fragment_info);
    }

    @Override
    protected AppBarLayout getToolbar(LayoutInflater inflater, ViewGroup container) {
        return defaultToolbar();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        final ImageView thumbnail = view.findViewById(R.id.thumbnail);
        final TextView title = view.findViewById(R.id.title);
        final TextView href = view.findViewById(R.id.href);
        final TextView ingredients = view.findViewById(R.id.ingredients);
        Bundle params = getParams();
        title.setText(params.getString(PARAM_TITLE));
        href.setText(params.getString(PARAM_URL));
        ingredients.setText(params.getString(PARAM_INGREDIENTS));
        Picasso.with(thumbnail.getContext()).load(params.getString(PARAM_THUMB)).into(thumbnail);
        return view;
    }
}
