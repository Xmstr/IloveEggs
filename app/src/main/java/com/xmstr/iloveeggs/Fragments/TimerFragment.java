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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xmstr.iloveeggs.R;
import com.xmstr.iloveeggs.interfaces.TimerCallbacks;

import java.util.Locale;

/**
 * Created by Xmstr.
 */

public class TimerFragment extends Fragment {

    public static final String TAG = "tag:timer";

    SeekBar eggSeekBar;
    TextView typeOfCookTextView;
    TextView descriptionOfEggTypeTextView;
    TextView timerTextView;
    ImageView picOfEgg;
    Button startTimerButton;
    CountDownTimer eggCountDownTimer;
    Boolean counterIsActive = false;
    int currentBoilTime = 5000;
    long currentTimeLeft = 0;
    Resources resources;
    //static int seekBarProgress = 2;

    private static final String ARG_TEXT = "arg_text";

    // LIST OF BOIL TIMES
    public final int BOIL_TIME_1 = 50000;
    public final int BOIL_TIME_2 = 7000;
    public final int BOIL_TIME_3 = 90000;
    public final int BOIL_TIME_4 = 160000;

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
            updateTimer((int)currentTimeLeft);
    }

    public TimerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_fragmet_layout, container, false);

        resources = getResources();

        // ALL INFO PANEL INITIALIZING
        typeOfCookTextView = rootView.findViewById(R.id.textView_typeOfCook);
        descriptionOfEggTypeTextView = rootView.findViewById(R.id.textView_descriptionOfCook);
        picOfEgg = rootView.findViewById(R.id.imageView_eggPic);
        startTimerButton = rootView.findViewById(R.id.buttonStartTimer);
        timerTextView = rootView.findViewById(R.id.textView_timer);

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
        }

        return rootView;
    }

    public void controlTimer() {
        if (!counterIsActive) {
            counterIsActive = true;
            setControlsLocked(true);
            eggCountDownTimer = new CountDownTimer(currentBoilTime + 50, 1000) {
                @Override
                public void onTick(long millsUntilFinished) {
                    Log.i("TIMER", "Timer ticked");
                    updateTimer((int) millsUntilFinished);
                    currentTimeLeft = millsUntilFinished;
                }

                @Override
                public void onFinish() {
                    timerCallbacks.onTimerFinished();
                    Log.i("TIMER", "Timer finished");
                    resetTimer();
                }
            }.start();
        } else {
            resetTimer();
        }
    }

    private void setControlsLocked(boolean isControlsLocked) {
        if (isControlsLocked) {
            eggSeekBar.setEnabled(false);
            startTimerButton.setText(R.string.reset_timer_button_text);
        } else {
            eggSeekBar.setEnabled(true);
            startTimerButton.setText(R.string.start_timer_button_text);
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
                updateTimer(currentBoilTime);
                setControlsLocked(false);
                startTimerButton.setEnabled(true);
            }
        }, 1500);

    }

    public void updateTimer(int secondsLeft) {
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
                updateTimer(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[0]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[0]);
                break;
            case 1:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_2;
                updateTimer(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[1]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[1]);
                break;
            case 2:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_3;
                updateTimer(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[2]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[2]);

                break;
            case 3:
                Log.i("COOK CHOOSING", "Choosed cook type: " + progress);
                currentBoilTime = BOIL_TIME_4;
                updateTimer(currentBoilTime);
                typeOfCookTextView.setText(cookTypeArray[3]);
                descriptionOfEggTypeTextView.setText(descriptionTypeArray[3]);

                break;

        }

    }
}
