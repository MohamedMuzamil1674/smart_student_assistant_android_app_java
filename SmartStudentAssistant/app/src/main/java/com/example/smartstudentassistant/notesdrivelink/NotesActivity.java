package com.example.smartstudentassistant.notesdrivelink;

import static android.graphics.Color.TRANSPARENT;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.R;
import com.example.smartstudentassistant.contentsdatabase.MyDatabaseHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    /* Creating variable of our notes array list for calling */
    ArrayList<NotesModel> notesModelArrayList;

    /* Creating variable of notes adapter class for calling */
    NotesRVAdapter notesRVAdapter;

    /* Creating variable of database helper class for call */
    MyDatabaseHelper databaseHelper;

    /* Creating variable of our recycler view for calling */
    RecyclerView notesRView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        /* Linking recycler view object of our activity notes list */
        notesRView = findViewById(R.id.recycle_view_notes);

        /* This is done to enable and initialize object of our DatabaseHelper class */
        databaseHelper = new MyDatabaseHelper(NotesActivity.this);

        /* Initializing object of our array list in order to execute data queries */
        notesModelArrayList = new ArrayList<>();

        /* Getting data of our notes array list from database class by callings */
        notesModelArrayList = databaseHelper.getStudyDrives();

        /* Here we are passing our books array list values to to our adapter class */
        notesRVAdapter = new NotesRVAdapter(notesModelArrayList, NotesActivity.this);

        /* Here we are setting a Layout Manager for alignment of our recycler view */
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotesActivity.this,
                RecyclerView.VERTICAL, false);

        /* Setting Layout Manager on our recycler view for setting vertical display */
        notesRView.setLayoutManager(layoutManager);

        /* Setting Notes RV Adapter onto our recycler view for display of data items */
        notesRView.setAdapter(notesRVAdapter);

        /* Here we will use listener on notes adapter to enable click on notes link */
        notesRVAdapter.setOnNotesLinkClickListener(notesLink -> {

            /* Calling class to create one custom notes dialog option for user */
            Dialog notesDialog = new Dialog(NotesActivity.this);
            notesDialog.setContentView(R.layout.std_notes_dialog);

            if (notesDialog.getWindow() != null){ /* This is done for all of round corners */
                notesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            }

            /* Linking elements of our custom dialog in order to enable actions */
            MaterialCardView cardGoogleDrive = notesDialog.findViewById(R.id.card_drive);
            MaterialCardView cardBrowser = notesDialog.findViewById(R.id.card_browser);

            cardBrowser.setOnClickListener(v -> {
                /* Here we are using implicit intent to open link in a google browser app */
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notesLink));

                /* Here we are setting package name of google drive app */
                browserIntent.setPackage("com.android.chrome");

                try {

                    /* This will try to start intent if package is found */
                    startActivity(browserIntent);
                    notesDialog.dismiss();

                }
                catch (ActivityNotFoundException exception){

                    /* This will find exception if chrome is not install */
                    Toast.makeText(NotesActivity.this, "Chrome app not installed",
                            Toast.LENGTH_SHORT).show();

                }

                notesDialog.dismiss();

            });

            cardGoogleDrive.setOnClickListener(v -> {
                /* Here we are using implicit intent to open link in a google drive app */
                Intent driveIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notesLink));

                /* Here we are setting package name of google drive app */
                driveIntent.setPackage("com.google.android.apps.docs");

                /* Here we are using try catch block to find exception */
                try {
                    /* This will try to start intent if package is found */
                    startActivity(driveIntent);

                    notesDialog.dismiss();
                }

                /* This will find exception if drive app is not installed */
                catch (ActivityNotFoundException exception){
                    Toast.makeText(NotesActivity.this, "Drive app not installed",
                            Toast.LENGTH_SHORT).show();
                }
                notesDialog.dismiss();
            });

            /* Here we are showing our custom dialog */
            notesDialog.show();
        });

        /* This is done to enable back pressed on our User Grades screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent userHome = new Intent(NotesActivity.this, HomeActivity.class);

                startActivity(userHome);

            }
        };
        getOnBackPressedDispatcher().addCallback(NotesActivity.this, callback);

        hideSystemBars(); /* This is our function calling that hides system screens bars */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notes_drive_screen), (v, insets) -> {
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