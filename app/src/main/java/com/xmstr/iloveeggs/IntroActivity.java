package com.xmstr.iloveeggs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Created by Xmstr.
 */

public class IntroActivity extends AppIntro{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("INTRO");
        sliderPage1.setDescription("dasdada");
        sliderPage1.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Clean App Intros");
        sliderPage2.setDescription("This library offers developers the ability to add clean app intros at the start of their apps.");
        sliderPage2.setBgColor(getResources().getColor(R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
