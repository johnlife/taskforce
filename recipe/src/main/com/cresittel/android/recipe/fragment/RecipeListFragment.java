package com.cresittel.android.recipe.fragment;

import android.content.Context;
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
import com.cresittel.android.recipe.data.Recipe;
import com.cresittel.android.recipe.events.RecipeListEvent;
import com.cresittel.android.recipe.service.BackgroundService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.johnlife.lifetools.adapter.BaseAdapter;
import ru.johnlife.lifetools.fragment.BaseListFragment;

/**
 * Created by User on 02/23/2018.
 */

public class RecipeListFragment extends BaseListFragment<Recipe> {

    private View progress;
    private TextView ingridients;
    private TextView submit;

    @Override
    protected BaseAdapter<Recipe> instantiateAdapter(Context context) {
        return new BaseAdapter<Recipe>(R.layout.item_recipe) {
            @Override
            protected ViewHolder<Recipe> createViewHolder(View view) {
                return new ViewHolder<Recipe>(view) {
                    private ImageView thumbnail = view.findViewById(R.id.thumbnail);
                    private TextView title = view.findViewById(R.id.title);

                    {   //this code will be called once for a layout creation
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Recipe item = getItem();
                                changeFragment(
                                    new RecipeInfoFragment()
                                        .addParam(Constants.PARAM_TITLE, item.getTitle())
                                        .addParam(Constants.PARAM_URL, item.getHref())
                                        .addParam(Constants.PARAM_THUMB, item.getThumbnail())
                                        .addParam(Constants.PARAM_INGREDIENTS, item.getIngredients())
                                , true);
                            }
                        });
                    }

                    //this is called every time the data object inserted into the layout
                    @Override
                    protected void hold(Recipe item) {
                        title.setText(item.getTitle());
                        Picasso.with(thumbnail.getContext()).load(item.getThumbnail()).into(thumbnail);
                    }
                };
            }
        };
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //important to call super.createView to let the magic happen
        View view = super.createView(inflater, container, savedInstanceState);

        //important to register this fragment for Events from EventBus,
        // otherwise the fragment will not catch those data from Bussines Layer
        EventBus.getDefault().register(this);

        //Call for default list
        requestRecipes(view.getContext().getString(R.string.default_search));

        ingridients = view.findViewById(R.id.ingridients);
        progress = view.findViewById(R.id.progress);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                requestRecipes(ingridients.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void requestRecipes(String search) {
        requestService(new BackgroundService.Requester() {
            @Override
            public void requestService(BackgroundService service) {
                service.requestRecipes(search.split("(\\s|\\W)+"));
            }
        });
    }

    @Override
    protected String getTitle(Resources r) {
        return r.getString(R.string.app_name);
    }

    @Override
    protected AppBarLayout getToolbar(LayoutInflater inflater, ViewGroup container) {
        return defaultToolbar();
    }


    //override this if your list contains some additional layout
    //like a search field for example
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recipe_list;
    }

    //Sadly, this one has to be public to work :(
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRecipeList(RecipeListEvent event) {
        getAdapter().set(event.getData());
        if (null != progress && null != submit) {
            progress.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
    }
}
