package com.xmstr.iloveeggs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.xmstr.iloveeggs.Fragments.InfoFragment;
import com.xmstr.iloveeggs.Fragments.RecipesFragment;
import com.xmstr.iloveeggs.Fragments.TimerFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    Fragment currentFragment = null;
    private static String fragmentTag = "fragment_undef";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recipes:
                    showRightFragment(item.getItemId());
                    return true;
                case R.id.navigation_timer:
                    showRightFragment(item.getItemId());
                    return true;
                case R.id.navigation_info:
                    showRightFragment(item.getItemId());
                    return true;
            }
            return false;
        }
    };

    private void showRightFragment(int itemId) {
        switch (itemId){
            case R.id.navigation_recipes:
                currentFragment = RecipesFragment.newInstance(getResources().getString(R.string.navigation_recipes_text));
                navigation.setItemBackgroundResource(android.R.color.white);
                break;
            case R.id.navigation_timer:
                currentFragment = TimerFragment.newInstance(getResources().getString(R.string.navigation_timer_text));
                navigation.setItemBackgroundResource(R.color.colorAccent);
                break;
            case R.id.navigation_info:
                currentFragment = InfoFragment.newInstance(getResources().getString(R.string.navigation_info_text));
                navigation.setItemBackgroundResource(android.R.color.white);
                break;
        }
        if (currentFragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, currentFragment, fragmentTag);
            ft.commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_timer);
    }

}
