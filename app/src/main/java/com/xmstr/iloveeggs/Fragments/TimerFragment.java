package com.xmstr.iloveeggs.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xmstr.iloveeggs.R;
import com.xmstr.iloveeggs.interfaces.TimerCallbacks;

import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Xmstr.
 */

public class TimerFragment extends Fragment {

    public static final String TAG = "tag:timer";

    SeekBar eggSeekBar;
    TextView typeOfCookTextView;
    TextView descriptionOfEggTypeTextView;
    TextView timerTextView;
    TextView timerPrevTextView;
    ImageView picOfEgg;
    FancyButton startTimerButton;
    CountDownTimer eggCountDownTimer;
    Boolean counterIsActive = false;
    int currentBoilTime = 5000;
    long currentTimeLeft = 0;
    Resources resources;

    private static final String ARG_TEXT = "arg_text";

    // LIST OF BOIL TIMES
    public final int BOIL_TIME_1 = 180000;
    public final int BOIL_TIME_2 = 300000;
    public final int BOIL_TIME_3 = 540000;
    public final int BOIL_TIME_4 = 660000;
    public final int BOIL_TIME_TEST = 7000;

    public static TimerFragment newInstance(String name) {
        TimerFragment frag = new TimerFragment();
        frag.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, name);
        //args.putInt("seekBarProgress", seekBarProgress);
        //frag.setArguments(args);
        return frag;
    }


    private TimerCallbacks timerCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        timerCallbacks = (TimerCallbacks) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (counterIsActive)
            updateTimerText((int)currentTimeLeft);
    }

    public TimerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_fragmet_layout, container, false);

        resources = getResources();

        // ALL UI INITIALIZING
        typeOfCookTextView = rootView.findViewById(R.id.textView_typeOfCook);
        descriptionOfEggTypeTextView = rootView.findViewById(R.id.textView_descriptionOfCook);
        picOfEgg = rootView.findViewById(R.id.imageView_eggPic);
        startTimerButton = rootView.findViewById(R.id.buttonStartTimer);
        //startTimerButton = new FancyButton(getContext());
        timerTextView = rootView.findViewById(R.id.textView_timer);
        timerPrevTextView = rootView.findViewById(R.id.textView_timerPrevText);

        // SEEKBAR
        eggSeekBar = rootView.findViewById(R.id.seekBar);
        //eggSeekBar.setProgress(seekBarProgress);
        eggSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                showCookInfo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlTimer();
            }
        });

        showCookInfo(eggSeekBar.getProgress());

        if (counterIsActive) {
            setControlsLocked(true);
            timerPrevTextView.setText(R.string.timer_prev_text_running);
        }
        else {
            timerPrevTextView.setText(R.string.timer_prev_text_default);
        }

        return rootView;
    }

    // TIMER

    public void controlTimer() {
        if (!counterIsActive) {
            counterIsActive = true;
            setControlsLocked(true);
            timerPrevTextView.setText(R.string.timer_prev_text_running);
            eggCountDownTimer = new CountDownTimer(currentBoilTime + 50, 1000) {
                @Override
                public void onTick(long millsUntilFinished) {
                    Log.i("TIMER", "Timer ticked");
                    updateTimerText((int) millsUntilFinished);
                    currentTimeLeft = millsUntilFinished;
                }

                @Override
                public void onFinish() {
                    Log.i("TIMER", "Timer finished");
                    timerCallbacks.startVibration();
                    timerCallbacks.playSound();
                    timerCallbacks.showNotification();
                    Log.i("TIMER", "Sound played");
                    timerCallbacks.showNotification();
                    timerPrevTextView.setText(R.string.timer_prev_text_finish);
                    resetTimer();
                }
            }.start();
        } else {
            resetTimer();
            timerPrevTextView.setText(R.string.timer_prev_text_reset);
        }
    }

    // END OF TIMER

    private void setControlsLocked(boolean isControlsLocked) {
        if (isControlsLocked) {
            eggSeekBar.setEnabled(false);
            startTimerButton.setText(resources.getString(R.string.reset_timer_button_text));
        } else {
            eggSeekBar.setEnabled(true);
            startTimerButton.setText(resources.getString(R.string.start_timer_button_text));
        }

    }

    private void resetTimer() {
        eggCountDownTimer.cancel();
        setControlsLocked(true);
        counterIsActive = false;
        startTimerButton.setEnabled(false);
        timerTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark));
        timerTextView.setText(R.string.zero_timer_text);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timerTextView.setTextColor(resources.getColor(android.R.color.black));
                updateTimerText(currentBoilTime);
                setControlsLocked(false);
                startTimerButton.setEnabled(true);
                timerPrevTextView.setText(R.string.timer_prev_text_default);
            }
        }, 1500);

    }

    public void updateTimerText(int secondsLeft) {
        // Приводим к секундам
        secondsLeft = secondsLeft / 1000;

        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);

        if (seconds <= 9) {

            secondString = "0" + secondString;

        }

        timerTextView.setText(String.format(Locale.getDefault(), "%d:%s", minutes, secondString));

    }

    private void showCookInfo(int progress) {
        Log.i("COOK CHOOSING", "Getting resources...");
        Resources res = getResources();
        String[] cookTypeArray = res.getStringArray(R.array.type_of_cook);
        String[] descriptionTypeArray = res.getStringArray(R.array.description_of_cook);
        switch (progress) {
            case 0:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_1;
                updateTimerText(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[0]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[0]);
                picOfEgg.setImageResource(R.drawable.egg1_done);
                break;
            case 1:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                // TESTING BOIL TIME
                currentBoilTime = BOIL_TIME_2;
                updateTimerText(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[1]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[1]);
                picOfEgg.setImageResource(R.drawable.egg2_done);
                break;
            case 2:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_3;
                updateTimerText(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[2]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[2]);
                picOfEgg.setImageResource(R.drawable.egg3_done);
                break;
            case 3:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_4;
                updateTimerText(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[3]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[3]);
                picOfEgg.setImageResource(R.drawable.egg4_done);
                break;

        }

    }
}
