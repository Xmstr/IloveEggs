package com.xmstr.iloveeggs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.xmstr.iloveeggs.Fragments.InfoFragment;
import com.xmstr.iloveeggs.Fragments.RecipesFragment;
import com.xmstr.iloveeggs.Fragments.TimerFragment;
import com.xmstr.iloveeggs.interfaces.TimerCallbacks;

public class MainActivity extends AppCompatActivity implements TimerCallbacks {

    public static final String KEY_CURRENT_ID = "key:currentId";
    private static final int NOTIFY_ID = 1;

    BottomNavigationView navigation;
    private TimerFragment mTimerFragment;
    private RecipesFragment mRecipesFragment;
    private InfoFragment mInfoFragment;
    MediaPlayer mPlayer;
    private boolean isAppRunning;

    //private static String fragmentTag = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //INTRO CHECK
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                boolean isFirstRun = preferences.getBoolean("firstStart", true);

                if (isFirstRun) {
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                }
            }

        });
        t.start();

        FragmentManager fm = getSupportFragmentManager();
        mTimerFragment = (TimerFragment) fm.findFragmentByTag(TimerFragment.TAG);
        mRecipesFragment = (RecipesFragment) fm.findFragmentByTag(RecipesFragment.TAG);
        mInfoFragment = (InfoFragment) fm.findFragmentByTag(InfoFragment.TAG);

        int currentId = savedInstanceState != null ? savedInstanceState.getInt(KEY_CURRENT_ID) : R.id.navigation_timer;

        navigation = findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(currentId);

        // PREPARING MEDIAPLAYER

        mPlayer = MediaPlayer.create(this, R.raw.ring);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_ID, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    private void showRightFragment(int itemId) {
        Fragment fragment = null;
        String tag = null;
        switch (itemId) {
            case R.id.navigation_recipes:
                if (mRecipesFragment == null) {
                    mRecipesFragment = RecipesFragment.newInstance(getResources().getString(R.string.navigation_recipes_text));
                }
                tag = RecipesFragment.TAG;
                fragment = mRecipesFragment;
                break;
            case R.id.navigation_timer:
                if (mTimerFragment == null) {
                    mTimerFragment = TimerFragment.newInstance(getResources().getString(R.string.navigation_timer_text));
                }
                tag = TimerFragment.TAG;
                fragment = mTimerFragment;
                break;
            case R.id.navigation_info:
                if (mInfoFragment == null) {
                    mInfoFragment = InfoFragment.newInstance(getResources().getString(R.string.navigation_info_text));
                }
                tag = InfoFragment.TAG;
                fragment = mInfoFragment;
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment, tag)
                    .commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recipes:
                case R.id.navigation_timer:
                case R.id.navigation_info:
                    showRightFragment(item.getItemId());
                    return true;
            }
            return false;
        }
    };


    @Override
    public void startVibration() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            Log.i("Vibrator", "Starting vibration");
            vibrator.vibrate(2000); // for 2 sec
        }

    }

    @Override
    public void showNotification() {
        if (!isAppRunning) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            @SuppressWarnings
                    ("deprecation") NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_egg_timer)
                    .setContentTitle(getString(R.string.notification_finish_title_text))
                    .setContentText(getString(R.string.notification_finish_content_text))
                    .setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        }
    }

    @Override
    public void playSound() {
        mPlayer.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        isAppRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAppRunning = false;
    }
}
