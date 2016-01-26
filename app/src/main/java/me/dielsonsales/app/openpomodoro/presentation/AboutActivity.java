package me.dielsonsales.app.openpomodoro.presentation;

import android.os.Bundle;

import me.dielsonsales.app.openpomodoro.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setToolbar(R.id.toolbar, R.string.title_activity_about);
        setDisplayHomeAsUpEnabled(true);
    }

}