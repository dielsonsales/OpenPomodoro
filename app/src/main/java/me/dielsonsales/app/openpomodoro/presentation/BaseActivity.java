package me.dielsonsales.app.openpomodoro.presentation;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by dielson on 24/01/16.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * Adds a toolbar from a view resource.
     * @param toolbarId the toolbar view id
     * @param titleId the string resource to be displayed as the title
     */
    protected void setToolbar(int toolbarId, int titleId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        toolbar.setTitle(getResources().getString(titleId));
        setSupportActionBar(toolbar);
    }

    protected void setDisplayHomeAsUpEnabled(boolean displayHomeAsUpEnabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
        }
    }
}
