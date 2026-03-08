package com.example.smartstudentassistant.filesscannings;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudentassistant.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesScanner extends AppCompatActivity {

    TextInputLayout layoutFileName; TextInputEditText editTextFile;

    MaterialButton btnStartScan; /* Button for starting a scanner */

    /* This is variable for gms document scanner from google kit */
    GmsDocumentScanner documentScanner; /* This is docs scanner */

    /* This is intent sender for base launch of scanner screen */
    ActivityResultLauncher<IntentSenderRequest> scannerLauncher;

    int length; /* This is variable for buffer data of file */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_files_scanning);

        /* Linking user interface elements inside java code */
        layoutFileName = findViewById(R.id.layout_file_name);
        editTextFile = findViewById(R.id.edit_text_file_name);
        btnStartScan = findViewById(R.id.btn_start_scanning);

        /* Here we are building document scanner options in our scanning screen activity */
        GmsDocumentScannerOptions scannerOptions = new GmsDocumentScannerOptions.Builder()

                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)

                .setGalleryImportAllowed(true) /* Allowing for gallery images imports */

                .setPageLimit(40) /* Setting page limit for file that will be scanned */

                /* Setting result format to portable document file to save it as file */
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)

                .build(); /* Building document scanner options for its further usages */

        /* This is done to obtain document scanner options from ml kits docs scanner */
        documentScanner = GmsDocumentScanning.getClient(scannerOptions);


        /* This is used to start scanner for document with an intent sender request */
        scannerLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {

                    /* When our result code is ok and its data is not null or not empty then */
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){

                        GmsDocumentScanningResult scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(
                                result.getData()); /* we obtain a scanning result from intent */

                        /* if a scanning result is not null and its result file is a not null */
                        if (scanningResult != null && scanningResult.getPdf() != null){

                            /* We wil obtain file name from edit text of our user interface */
                            String fileTitle = String.valueOf(editTextFile.getText());

                            /* This is done to obtain uri of a file with its document file */
                            Uri filePath = scanningResult.getPdf().getUri();

                            if (!fileTitle.isEmpty()){ /* When file name is not null then */

                                saveDocsFile(filePath, fileTitle); /* calls save function */

                            }

                        }

                        /* This is used when scanning is canceled by user during scanning */
                        else if (result.getResultCode() == RESULT_CANCELED) {

                            /* Here we will show message to user on cancel of file scanning */
                            Toast.makeText(this, "Document Scan is dismissed", Toast.LENGTH_SHORT).show();

                        }

                    }

                });


        /* Setting click listener onto our button for actions */
        btnStartScan.setOnClickListener(v -> {

            /* Getting value for file name from our user */
            String fileName = String.valueOf(editTextFile.getText());

            if (fileName.isEmpty()){
                /* When user input is empty then we set errors on it */
                layoutFileName.setError("File Name cannot be empty");

            } else if (fileName.length() > 16) {

                /* When user input is greater than our defined limit */
                layoutFileName.setError("File Name cannot be above 16");


            } else { /* When input from user is not empty or invalid */

                /* Then we will call our scanning function for note */
                startDocsScanning();

            }
        });

        hideSystemBars(); /* This is our function calling that hides system screens bars */


        /* This is default margins apply code for screen so that our element views do not conflict with both of system bars */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.files_scanner_scr), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

    }

    /* This function is used to start scanning of images after user has given name of it */
    private void startDocsScanning() {

        documentScanner.getStartScanIntent(this).addOnSuccessListener(intentSender -> {

            /* Here we are calling intent sender class for setting intent sender request of scanning */
            IntentSenderRequest senderRequest = new IntentSenderRequest.Builder(intentSender).build();

            scannerLauncher.launch(senderRequest); /* Now we are launching scanner for scan intent */

        }).addOnFailureListener( /* This is done to handle scanner start failed for avoiding crash */

                /* Here we will show all details inside logcat of system to focus on error details */
                e -> Log.e("LaunchFailed", "Scanner Launch did not work"));

    }

    /* This function is used to save scanned file in device of system user */
    private void saveDocsFile(Uri filePath, String fileTitle){

        try { /* This is beginning of our try block to save file inside documents folder of device */

            /* Here we are opening file input stream for a scanned document file from our ML Kit */
            InputStream inputStream = getContentResolver().openInputStream(filePath);

            if (inputStream == null){ /* Checking if input stream is empty or has no image in it */

                Toast.makeText(this, "File stream is empty", Toast.LENGTH_SHORT).show();

                return; /* Showing error toast message to user and closing execution at this time */
            }

            ContentValues contentValues = new ContentValues(); /* We initialize content value */

            /* Here we are setting a file with format into downloads folder with media store */
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileTitle + ".pdf");

            /* Here we are setting file with mime form in downloads folder with media store */
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");

            /* Here we are setting file with a path into downloads folder with media store */
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            /* Here we are adding file with its uri into downloads folder with media store */
            Uri fileUri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            if (fileUri != null){ /* when our file uri is not null and is a valid address */

                /* Here we are opening output stream from destination file if exists in documents */
                try (OutputStream outputStream = getContentResolver().openOutputStream(fileUri)) {

                    byte[] buffer = new byte[16192]; /* Making buffer for holding data temporarily */

                    /* This keeps copying data till buffer is not empty */
                    while ((length = inputStream.read(buffer) )!= -1){

                        if (outputStream != null) {

                            /* This writes all data into our output stream */
                            outputStream.write(buffer, 0, length);

                        }

                    }

                    inputStream.close(); /* Closing inputs stream */

                    if (outputStream != null) {

                        outputStream.close(); /* Close output stream */

                    }

                    /* Showing success message to user upon save */
                    Toast.makeText(this, "File is saved to Downloads",Toast.LENGTH_LONG).show();

                }

            }

        } catch (IOException e) { /* This is start of catch block to handle any error during file save */

            /* Showing error message in logcat to avoid crash of system */
            Log.e("Scanner_Failed", "Error in saving file", e);

        }

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