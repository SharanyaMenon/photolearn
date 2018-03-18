package com.example.toshimishra.photolearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


/**
 * Created by toshimishra on 14/03/18.
 */

public class ParticipantEditmodeAddLearningItem extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    Button button;
    EditText text;
    String url;
    boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_editmode_add_learningitem);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        button = (Button) findViewById(R.id.bt_Add);
        text = (EditText) findViewById(R.id.xh_txt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoDesc = text.getText().toString();
                if (photoDesc == null || photoDesc.isEmpty()) {
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.INVALID_INPUT, Toast.LENGTH_SHORT).show();
                } else if (url == null || url.isEmpty()) {
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.UPLOAD_IMAGE, Toast.LENGTH_SHORT).show();
                } else {
                    ((Participant) State.getCurrentUser()).createLearningItem(url, photoDesc, "testGPS");
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.IMAGE_SELECTED, Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    imageView = (ImageView) findViewById(R.id.img);
                    imageView.setImageURI(selectedImage);
                    isImageSelected = true;
                }
        }
    }


    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    public void uploadImage(View view) {
        if (isImageSelected) {
//create reference to images folder and assing a name to the file that will be uploaded
            imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
            //creating and showing progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage(Constants.UPLOADING);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            //starting upload
            uploadTask = imageRef.putFile(selectedImage);
            // Observe state change events such as progress, pause, and resume
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            });
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    url = downloadUrl.toString();
                    Log.i("downloadURL", "download:" + downloadUrl);
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    Log.i("ImageView", "image:" + imageView);
                    Picasso.with(ParticipantEditmodeAddLearningItem.this).load(downloadUrl).into(imageView);
                }
            });
        } else {
            Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.SELECT_IMAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (i == R.id.action_switch) {
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
