package ru.johnlife.lifetools.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by User on 01/02/2018.
 */

public class EditTextPersistent extends android.support.design.widget.TextInputEditText {
    public EditTextPersistent(Context context) {
        super(context);
        init(context);
    }

    public EditTextPersistent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextPersistent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setText(pref.getString(getIdName(), ""));
        postDelayed(()-> addTextChangedListener(new TextWatcher() {
            private Runnable saver = () -> save();

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                removeCallbacks(saver);
                postDelayed(saver, 500);
            }
        }), 100);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) {
            save();
        }
    }

    private void save() {
        PreferenceManager.getDefaultSharedPreferences(getContext())
            .edit()
            .putString(getIdName(), getText().toString())
            .apply();
    }

    @SuppressLint("ResourceType")
    public String getIdName() {
        if (getId() == 0xffffffff) return "no-id";
        else return getResources().getResourceName(getId());
    }

}
