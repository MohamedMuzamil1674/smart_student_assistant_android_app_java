package com.example.smartstudentassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudentassistant.onboardingsui.IntroActivity1;

public class StartingActivity extends AppCompatActivity {

    /* Splash Screen time duration variable */
    private static final int SPLASH_SCREEN = 5000;

    /* Declaring Animation form variables */
    Animation topAnim, bottomAnim;

    /* Declaring our TextView variables */
    TextView txtName, txtHint1;

    /* Declaring ImageView variables */
    ImageView stdLogo1, stdLogo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /* Hooking our views from Splash Layout file */
        txtName = findViewById(R.id.app_name);
        txtHint1 = findViewById(R.id.app_name_hint);
        stdLogo1 = findViewById(R.id.student_logo_one);
        stdLogo2 = findViewById(R.id.student_logo_two);

        /* Loading animations from files of anim directory */
        topAnim = AnimationUtils.loadAnimation(StartingActivity.this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(StartingActivity.this, R.anim.bottom_anim);

        /* Loading desired animation into our image logos */
        stdLogo1.setAnimation(topAnim);
        stdLogo2.setAnimation(bottomAnim);

        /* Making Screen disappear after 5 seconds and opening our OnBoarding Screen one */
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            /* Here we are using shared prefrences to avoid on boarding appearing again onto our user interface */
            SharedPreferences preferences = getSharedPreferences("onboarding_finish", Context.MODE_PRIVATE);

            boolean isFinished = preferences.getBoolean("isOnboardFinished", false);

            if (!isFinished){ /* when onboarding screen is not shown to user on first launch */

                Intent intentIntro = new Intent(StartingActivity.this, IntroActivity1.class);

                startActivity(intentIntro); finish(); /* starting our activity and calling finish */

            }
            else { /* when onboarding screen is shown to a user after first system launch */

                Intent intentHome = new Intent(StartingActivity.this, HomeActivity.class);

                startActivity(intentHome); finish(); /* starting our activity and calling finish */

            }

        },SPLASH_SCREEN);

        hideSystemBars(); /* This is our function calling that hides both of a system screens bars */

        /* This is used to enable back button press if user press it then we minimize application */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { /* This is main function to handle back pressed */

                finishAfterTransition(); /* This closes application with transitions animation */

            }
            /* Here we are registering back press call back on its clicks so we can enable it */
        }; getOnBackPressedDispatcher().addCallback(this, callback);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.starting_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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