package com.example.smartstudentassistant.imagescontents;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.graphics.Color.TRANSPARENT;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;

public class ContentActivity extends AppCompatActivity {

    /* Here we are creating variables of our activity views like image, text, and edit text */
    AppCompatImageView imgClear, imgCopy, imgPicks; TextInputEditText editTextImg;
    MaterialTextView txtClear, txtPicks, txtCopy; TextInputLayout textInputLayout;

    /* Here we are defining code for camera permission of device */
    private static final int CAMERA_PERMISSION_CODE = 100;

    /* Here we are creating one launcher intent for camera image */
    private ActivityResultLauncher<Uri> cameraLauncher;

    /* Here we are defining code for gallery permission of device */
    private static final int GALLERY_PERMISSION_CODE = 200;

    /* Here we are defining code for media permission of device */
    private static final int MEDIA_PERMISSION_CODE = 300;

    /* Here we are defining code for storage permission of device */
    private static final int STORAGE_PERMISSION_CODE = 400;

    /* Here we are creating one launcher intent for camera image */
    private ActivityResultLauncher<Intent> galleryLauncher;

    /* Here we are creating our uri for camera images with files */
    private Uri cameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_ocr);

        /* They are our text area where image text will be shown */
        textInputLayout = findViewById(R.id.layout_ocr_text);
        editTextImg = findViewById(R.id.img_extracted_text);

        /* They are text views as labels for our task actions */
        txtClear = findViewById(R.id.clear_text_label);
        txtPicks = findViewById(R.id.pick_text_label);
        txtCopy = findViewById(R.id.copy_text_label);

        /* These are our icon buttons for basic task actions */
        imgClear = findViewById(R.id.img_clear_text);
        imgPicks = findViewById(R.id.img_pick_icon);
        imgCopy = findViewById(R.id.img_copy_text);

        /* Here we will set visibility of a text area views */
        textInputLayout.setVisibility(View.INVISIBLE);

        /* Here we will set visibility of image view label */
        editTextImg.setVisibility(View.INVISIBLE);

        /* Here we will set visibility of all button views */
        txtClear.setVisibility(View.INVISIBLE);
        imgClear.setVisibility(View.INVISIBLE);
        txtCopy.setVisibility(View.INVISIBLE);
        imgCopy.setVisibility(View.INVISIBLE);

        /* Here we are setting click listener for image select icon */
        imgPicks.setOnClickListener(v -> {
            /* Here we are calling our custom select dialog box */
            showOptionsDialog();
        });

        /* Here we are setting click listener for image select text */
        txtPicks.setOnClickListener(v -> {
            /* Here we are calling our custom select dialog box */
            showOptionsDialog();
        });

        /* Here we are setting click listener for a copy text icon */
        imgCopy.setOnClickListener(v -> {
            /* Here we will call our function of copy text */
            copyTextForDocument();
        });

        /* Here we are setting click listener for a copy text icon */
        imgClear.setOnClickListener(v -> {
            /* Here we will call our function of clear text */
            clearTextForChanges();
        });

        /* Here we will register camera launcher for data */
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                success -> {

                    /* When image uri is not null then we will try launch */
                    if (cameraImageUri != null) {

                        try {/* Starting of try block with our image uri */

                            if (success) { /* When image is gotten by uri */

                                /* When bitmap is set then we will set title visible */
                                textInputLayout.setVisibility(View.INVISIBLE);
                                editTextImg.setVisibility(View.VISIBLE);

                                /* Then we will run our recognize task */
                                runTextRecognizer(cameraImageUri);
                            }
                        }
                        catch (Exception e) {
                            /* When we get any errors during this then */

                            Log.e("image_error", "Error in setting image", e);
                            /* This prints details of error in logcat */
                        }
                    }
                });

        /* Here we will register gallery launcher for data */
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {

            /* This checks if result code is ok and our result data is not empty */
            if (result.getResultCode() == RESULT_OK && result.getData() != null){

                /* Here we are getting intent and its uri via two get functions */
                Uri pickedImgUri = result.getData().getData();

                /* Now we will use try catch block for a safe getting of image */

                try { /* Here we will try to get user picked image with bitmap */

                    if (pickedImgUri != null) { /* If imageUri is not null then we will create sourceUri */

                        Bitmap bitmap; /* We wil use bitmap later on data passing */

                        /* Here we are creating image source if Uri is not empty */
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),
                                pickedImgUri);

                        /* Here we are setting a decoded Source onto our Bitmap */
                        bitmap = ImageDecoder.decodeBitmap(source);

                        /* When bitmap is set then we will set title visible */
                        textInputLayout.setVisibility(View.INVISIBLE);
                        editTextImg.setVisibility(View.VISIBLE);

                        /* When bitmap is set then we will run recognitions */
                        runTextRecognition(bitmap);
                    }
                } catch (IOException e){ /* This is input output exception catch */

                    Log.e("image_error", "Error in setting image", e);
                    /* This prints full details of exception in logcat section */

                    /* We will show a toast when image is not set onto image view */
                    Toast.makeText(this, "Error Kindly try later", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* This is done to enable back pressed on Image Translate screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent LoginScreen = new Intent(ContentActivity.this, HomeActivity.class);

                startActivity(LoginScreen);

            }
        }; getOnBackPressedDispatcher().addCallback(this, callback);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.text_ocr_screen), (v, insets) -> {
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

    /* Here we are showing a dialog for user to select their desired image by gallery or camera of device */
    private void showOptionsDialog(){
        /* Here we are enabling contents of our custom dialog for our system user */
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_image_dialog);

        if (dialog.getWindow() != null){ /* This is done mainly for round corners */
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        }

        /* Here we are enabling selections cards of our custom dialog for our user */
        MaterialCardView cardGallery = dialog.findViewById(R.id.card_device_gallery);
        MaterialCardView cameraCard = dialog.findViewById(R.id.card_device_camera);

        /* Here we are setting click listener for gallery images on gallery card */
        cardGallery.setOnClickListener(v -> {
            /* Here we will call gallery access check function */
            checkGalleryPermission();

            /* Once item card is click we will dismiss dialog */
            dialog.dismiss();
        });

        /* Here we are setting click listener for camera images on camera card */

        cameraCard.setOnClickListener(v -> {
            /* Here we will call camera access check function */
            checkCameraPermission();

            /* Once item card is click we will dismiss dialog */
            dialog.dismiss();
        });

        /* Showing our custom dialog in image translate screen */
        dialog.show();
    }

    /* Here we will check if user has given camera permission or not for activity task to be finish */
    private void checkCameraPermission(){

        /* If there are no permissions given then we will ask for camera access for all versions */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else {
            /* When camera access is already granted then we will start camera activity */
            openDeviceCamera();
        }
    }

    /* This is used to start camera screen for image capture and its texts */
    private void openDeviceCamera(){

        /* Here we are using uri of captured image with a file provider */
        File imageFile = new File(getApplicationContext().getFilesDir(),
                "camera_images.jpg");

        /* This will give us an image uri from internal storage where */
        cameraImageUri = FileProvider.getUriForFile(getApplicationContext(),
                "com.example.digitalstudentassistant.fileProvider",
                imageFile); /* private data of our app will be stored */

        /* Now we will use camera launcher with this image uri easily */
        cameraLauncher.launch(cameraImageUri);

    }

    /* Here we will check if user has given gallery permission or not */
    private void checkGalleryPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){ /* This is used when sdk is Android 15 or above */

            if (ContextCompat.checkSelfPermission(this, READ_MEDIA_VISUAL_USER_SELECTED) != PERMISSION_GRANTED){

                /* Requesting gallery images access with request code for android 15 devices and android 16 if it is not granted */
                ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_VISUAL_USER_SELECTED}, MEDIA_PERMISSION_CODE);

            } else {

                openDeviceGallery(); /* When camera access is already granted then we will start gallery activity for process */

            }


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { /* This is used when sdk is Android 14 or below */

            if (ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) != PERMISSION_GRANTED){

                /* Requesting gallery images access with activity context and request code for android 13 and android 14 */
                ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES}, GALLERY_PERMISSION_CODE);

            }
            else {

                openDeviceGallery(); /* When camera access is already granted then we start gallery activity for process */

            }

        }
        else { /* This is used for lower devices which are android 12 or below than it mainly for mins sdk 30 of project */

            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED){

                /* Gallery images access with activity context and request code for android 12 and device below than it */
                ActivityCompat.requestPermissions(this, new String[] {READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            } else { /* If gallery access is granted then start activity to allow user in select of text recognize image */

                openDeviceGallery(); /* calling of our gallery image function to start our text recognize process easily */


            }

        }
    }

    private void openDeviceGallery(){ /* This is used to start gallery for image select for a text recognizer */

        Intent galleryIntent = new Intent(Intent.ACTION_PICK); /* Here we are defining select form of intent */

        /* Here we are using our intent with Media Store image and external content uri to select one image */
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        galleryLauncher.launch(galleryIntent); /* Now we will use camera launcher to finish our image task */
    }

    /* Here we will handle logic of camera access request from user */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE){ /* When request code is for device camera then it is used */

            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){

                /* When user gives camera access then we will show a success toast and start camera */
                Toast.makeText(this, "Camera Access Granted", Toast.LENGTH_SHORT).show();

                openDeviceCamera(); /* This is done when user has granted camera access for images */
            }
            else {

                /* When user gives camera access then we will show failure toast for our request */
                Toast.makeText(this, "Camera Access Denied", Toast.LENGTH_SHORT).show();

            }
        }
        else if (requestCode == GALLERY_PERMISSION_CODE) { /* When request code is for gallery then it is used */

            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {

                /* When user gives gallery access then we will show a success toast and start gallery selects */
                Toast.makeText(this, "Gallery Access Granted", Toast.LENGTH_SHORT).show();

                openDeviceGallery(); /* This is done when user has granted a gallery access for media images */
            }
            else {
                /* When user gives camera access then we will show failure toast that our request is denied */
                Toast.makeText(this, "Gallery Access Denied", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == STORAGE_PERMISSION_CODE) { /* When request code is storage then it is used */

            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){

                /* When user gives gallery access then we will show a success toast and start gallery selects */
                Toast.makeText(this, "Gallery Access Granted", Toast.LENGTH_SHORT).show();

                openDeviceGallery(); /* This is done when user has granted a gallery access for media images */
            }
            else {
                /* When user gives camera access then we will show failure toast that our request is denied */
                Toast.makeText(this, "Storage Access Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* This is our main function to extract text from image and then sets it on edit text view for more actions */
    private  void runTextRecognition(Bitmap bitmap){ /* This codes function will be used for gallery images */

        /* Here we are making our gallery input image from bitmap for a view */
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        /* Here we are using text recognizer labeler to initialize options */
        com.google.mlkit.vision.text.TextRecognizer textRecognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS);

        /* Here we will start to process our input image for text extracts */
        /* First we will link on success listener if result is ok for text */
        textRecognizer.process(inputImage).addOnSuccessListener(result->{

            /* Here we will set text on edittext when result is a success */
            editTextImg.setText(result.getText());

            /* We will also set visibility for all invisible element now */

            /* Here we will set visibility of a text area views */
            editTextImg.setVisibility(View.VISIBLE);

            /* Here we will set visibility of all button views */
            txtClear.setVisibility(View.VISIBLE);
            imgClear.setVisibility(View.VISIBLE);
            txtCopy.setVisibility(View.VISIBLE);
            imgCopy.setVisibility(View.VISIBLE);

            /* Now we will set failure listener on it to catch */
        }).addOnFailureListener(e -> { /* basic errors for it */

            /* Here we will show one toast message for error */
            Toast.makeText(this, "Error! Try again",
                    Toast.LENGTH_SHORT).show();
        });
    }

    /* This is our main function to extract text from image and then sets it on edit text view for more actions */
    private void runTextRecognizer(Uri cameraImageUri){ /* This codes function will be used for camera images */

        if (cameraImageUri != null){ /* Here we are making input image from camera uri for view */

            try { /* Here for safety check we will use try and catch block to see if get error */
                InputImage inputImage = InputImage.fromFilePath(this, cameraImageUri);
                /* Here we are setting our uri for camera image to our input image for process */

                /* Here we are using text recognizer labeler to initialize options */
                com.google.mlkit.vision.text.TextRecognizer textRecognizer = TextRecognition.getClient(
                        TextRecognizerOptions.DEFAULT_OPTIONS);

                /* Here we will start to process our input image for text extracts */
                /* First we will link on success listener if result is ok for text */
                textRecognizer.process(inputImage).addOnSuccessListener(result->{

                    if (result.getTextBlocks().isEmpty()){ /* When no text is in a image then */

                        Toast.makeText(this, "No text to recognize", Toast.LENGTH_SHORT).show();

                        return; /* This will return from current statements and no execution */
                    }

                    /* Here we will set text on edittext when result is a success */
                    editTextImg.setText(result.getText());
                    /* We will also set visibility for all invisible element now */

                    /* Here we will set visibility of a text area views */
                    editTextImg.setVisibility(View.VISIBLE);

                    /* Here we will set visibility of all button views */
                    txtClear.setVisibility(View.VISIBLE);
                    imgClear.setVisibility(View.VISIBLE);
                    txtCopy.setVisibility(View.VISIBLE);
                    imgCopy.setVisibility(View.VISIBLE);

                    /* Now we will set failure listener on it to catch */
                }).addOnFailureListener(e -> { /* basic errors for it */

                    /* Here we will show one toast message for error */
                    Toast.makeText(this, "Error! Try again",
                            Toast.LENGTH_SHORT).show();

                    Log.e("Error_Msg", "Text Recognize failed");

                });

            } catch (IOException e) { /* Here we will catch any error thrown at system runtime */
                throw new RuntimeException(e); /* This throws an error if it occurs on runtime */
            } /* So as try catch block is finished hence we will move on to our other activity */
        }
    }

    /* This is used to copy text onto our clipboard when user click on copy button of interface */
    private void copyTextForDocument(){ /* So that it can be re used for various useful reason */

        /* Here we will obtain text from edit text to see if copy it */
        String extractedText = String.valueOf(editTextImg.getText());

        /* When edit text is not empty then it copies all text of it */
        if (!extractedText.isEmpty()){ /* When there is text on it */

            /* Here we will obtain services of a clip board manager */
            ClipboardManager clipboardManager = (ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);

            /* Now we will add copy Data as plain text to keyboard */
            clipboardManager.setPrimaryClip(ClipData.newPlainText(
                    "text", extractedText));

            /* We will show one toast message when data is copied */
            Toast.makeText(this, "Success! Text Copied",
                    Toast.LENGTH_SHORT).show();
        }
        else { /* This is used when there is no text inside layout */

            /* We will show a toast message when no text is there */
            Toast.makeText(this, "Alert! No Text to Copy",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /* This is used to clear text from edit text when user click on clears button of interface */
    private void clearTextForChanges(){ /* So that it can be erased for various useful reason */

        /* Here we will obtain text from edit text to see if clear it */
        String extractedText = String.valueOf(editTextImg.getText());

        /* When edit text is not empty then it clears all text of it */
        if (!extractedText.isEmpty()){ /* When there is text on it */

            editTextImg.setText(""); /* This clears a text inside it */

            /* We will show one toast message when data is copied */
            Toast.makeText(this, "Success! Text Cleared",
                    Toast.LENGTH_SHORT).show();
        }
        else { /* This is used when there is no text inside layout */

            /* We will show a toast message when no text is there */
            Toast.makeText(this, "Alert! No Text to Clear",
                    Toast.LENGTH_SHORT).show();
        }
    }
}