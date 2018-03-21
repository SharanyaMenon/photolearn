package com.example.toshimishra.photolearn;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.ImageCallback;
import com.example.toshimishra.photolearn.Utilities.ImageUploadUtility;
import com.example.toshimishra.photolearn.Utilities.GeoLocation;
import com.example.toshimishra.photolearn.Utilities.LoadImage;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ParticipantEditmodeAddLearningItem extends AppCompatActivity implements LoadImage.Listener {
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    Button button,selectImg_btn,uploadImg_btn;
    TextView mTitle_LS,mTitle_Q;
    EditText text;
    String url,itemId, desc, gps, photoURL;
    boolean isImageSelected = false;
    LoadImage.Listener l;
    private final int GALLERY = 1, CAMERA = 2;
    ImageUploadUtility imageUploadUtility = new ImageUploadUtility();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_editmode_add_learningitem);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        l = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Init gelocation services here, we dont need location before this activity launch

        if(!State.isGeoLocationset()) {
            try {
                State.setGeoLocation(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        button = (Button) findViewById(R.id.bt_Add);
        text = (EditText) findViewById(R.id.xh_txt);
        selectImg_btn = (Button)findViewById(R.id.selectimgbtn);
        uploadImg_btn = (Button)findViewById(R.id.uploadimgbtn);
        imageView = (ImageView) findViewById(R.id.img);
        mTitle_LS = (TextView)findViewById(R.id.title_LS);
        mTitle_Q = (TextView)findViewById(R.id.title_Q);
        mTitle_LS.setText(State.getCurrentSession().getSessionID());
        mTitle_Q.setText(State.getCurrentLearningTitle().getTitle());;
        if(!State.isUpdateMode()) {

            selectImg_btn.setVisibility(View.VISIBLE);
            uploadImg_btn.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String photoDesc = text.getText().toString();
                    if (photoDesc == null || photoDesc.isEmpty()) {
                        Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.INVALID_INPUT, Toast.LENGTH_SHORT).show();
                    } else if (url == null || url.isEmpty()) {
                        Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.UPLOAD_IMAGE, Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            GeoLocation.getLocation(new CallBackInterface() {
                                @Override
                                public void onCallback(Object value) {
                                    ((Participant) State.getCurrentUser()).createLearningItem(url, photoDesc, (String)value);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }
            });
        }else{
            button.setText("Update");

            selectImg_btn.setVisibility(View.GONE);
            uploadImg_btn.setVisibility(View.GONE);

            Bundle b = getIntent().getExtras();
            itemId = b.getString("itemID");
            desc = b.getString("photoDesc");
            gps = b.getString("gps");
            photoURL = b.getString("photoURL");

            text.setText(desc);
            new LoadImage(l, 200, 150).execute(photoURL);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    desc = text.getText().toString();
                    try {
                        GeoLocation.getLocation(new CallBackInterface() {
                            @Override
                            public void onCallback(Object value) {
                                ((Participant) State.getCurrentUser()).updateLearningItem(itemId,photoURL, desc, (String)value);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
//
            case GALLERY:
                if (imageReturnedIntent != null) {

                    try {
                        selectedImage = imageReturnedIntent.getData();
                        imageView = (ImageView) findViewById(R.id.img);
                        imageView.setImageURI(selectedImage);
                        isImageSelected = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ParticipantEditmodeAddLearningItem.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case CAMERA:
                if (imageReturnedIntent != null) {

                    try {
                        Bitmap thumbnail = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        imageView.setImageBitmap(thumbnail);
                        imageUploadUtility.saveImage(thumbnail, this);
                        selectedImage = imageUploadUtility.getImageUri(getApplicationContext(), thumbnail);
                        isImageSelected = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ParticipantEditmodeAddLearningItem.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }
    }

    public void selectImage(View view) {
        imageUploadUtility.selectImage(this, this, new ImageCallback() {
            @Override
            public void onImageCallback(Intent intent, int i) {
                startActivityForResult(intent, i);
            }
        });

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
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.ERROR_IN_UPLOADING, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    url = downloadUrl.toString();
                    Log.i("downloadURL", "download:" + downloadUrl);
                    Toast.makeText(ParticipantEditmodeAddLearningItem.this, Constants.UPLOAD_SUCCESSFUL, Toast.LENGTH_SHORT).show();
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
            return false;
        } else if (i == R.id.action_switch) {
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onImageLoaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(State.isTrainerMode()){
            finish();
        }
        Log.d("TrainerSessionsActivity","onStart********");

    }

}
