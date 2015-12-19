package me.dielsonsales.app.openpomodoro;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroListener;

public class MainActivity extends AppCompatActivity implements ClockFragment.OnFragmentInteractionListener {

    Button mPlayButton;
    PomodoroController mPomodoroController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_main));
        setSupportActionBar(toolbar);

        mPomodoroController = new PomodoroController();

        mPomodoroController.setPomodoroListener(new PomodoroListener() {
            @Override
            public void onMinuteLeft() {

            }

            @Override
            public void onTimeOver() {
                playSound();
            }
        });

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPomodoroController.start();
            }
        });
    }

    void playSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // does nothing yet
    }
}
