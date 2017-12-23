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
        sliderPage1.setTitle(getString(R.string.app_intro_title2));
        sliderPage1.setDescription(getString(R.string.app_intro_description));
        sliderPage1.setBgColor(getResources().getColor(R.color.primary));
        sliderPage1.setImageDrawable(R.drawable.eggs_splash);
        addSlide(AppIntroFragment.newInstance(sliderPage1));

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
