package com.example.smartstudentassistant.onboardingsui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.R;

public class IntroActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro3);

        Button BtnGotoNext = findViewById(R.id.btn_next_three);

        BtnGotoNext.setOnClickListener(v -> {

            /* Here we are using shared prefrences to avoid on boarding appearing again onto our user interface */
            SharedPreferences preferences = getSharedPreferences("onboarding_finish", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit(); /* calling of our editor class for data adds */

            editor.putBoolean("isOnboardFinished", true); editor.apply(); /* here we put and apply our values */


            Intent goNext = new Intent(IntroActivity3.this, HomeActivity.class);
            startActivity(goNext);



        });

        /* This is done to enable back pressed on intro3 screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent i3Prev = new Intent(IntroActivity3.this, IntroActivity2.class);

                startActivity(i3Prev);

            }
        };
        getOnBackPressedDispatcher().addCallback(IntroActivity3.this, callback);

        hideSystemBars(); /* This is our function calling that hides system screens bars */

    }

    @Override
    protected void onResume() { /* This is used when activity is resumed by system users */

        super.onResume(); /* This is object of class that is called with resume function */

        hideSystemBars(); /* This is our function calling that hides system screens bars */

    }

    /* This is done to hide status bar and navigations bar in our Splash Screen activity */
    private void hideSystemBars(){

        /* Getting instance of insets controller for hiding of system bars for screen */
        final WindowInsetsController insetsController = getWindow().getInsetsController();

        if (insetsController != null) { /* First we hide system bars on screen creation */

            /* This is used to hide system bars from screen until swipe is not occurred */
            insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());

            /* Then we re show them when user swipes navigation area or status bar area */
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) { /* This is a focus change function */

        super.onWindowFocusChanged(hasFocus); /* This is required key only for few devices */

        if (hasFocus){ /* So that system bars can be hided on them for immersive display */

            hideSystemBars(); /* This is a function calling to hide system screens bars */

        }
    }
}