package com.example.toshimishra.photolearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.Utilities.LoadImage;
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
 * Created by SUGANTHI on 16-03-2018.
 */

public class TrainerAddQuizItem extends AppCompatActivity implements LoadImage.Listener{
    TextView text_ls,text_q;
    Button button;
    EditText et_question,et_opt1,et_opt2,et_opt3,et_opt4,ansExp;
    RadioButton rb_ans1,rb_ans2,rb_ans3,rb_ans4;
    Toolbar toolbar;


    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    String url,key,value ;
    int ans;
    boolean isImageSelected = false;
    LoadImage.Listener l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_add_quizitem);
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


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        text_ls = (TextView)findViewById(R.id.title_LS);
        text_q = (TextView)findViewById(R.id.title_Q);
        text_ls.setText(State.getCurrentSession().getCourseCode());
        text_q.setText(State.getCurrentQuizTitle().getTitle());
        //textView_mode = (TextView)findViewById(R.id.textView4);

        et_question = (EditText)findViewById(R.id.xh_txt); //question
        et_opt1 = (EditText)findViewById(R.id.Opt1); //option1
        et_opt2 = (EditText)findViewById(R.id.Opt2); //option2
        et_opt3 = (EditText)findViewById(R.id.Opt3); //option3
        et_opt4 = (EditText)findViewById(R.id.Opt4); //option4

        rb_ans1 = (RadioButton)findViewById(R.id.radioButton3);
        rb_ans2 = (RadioButton)findViewById(R.id.radioButton4);
        rb_ans3 = (RadioButton)findViewById(R.id.radioButton5);
        rb_ans4 = (RadioButton)findViewById(R.id.radioButton6);

        ansExp = (EditText)findViewById(R.id.Exp);

        button = (Button)findViewById(R.id.bt_Add);
        rb_ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 1 ;
            }
        });
        rb_ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 2 ;
            }
        });
        rb_ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 3 ;
            }
        });
        rb_ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 4 ;
            }
        });

        imageView = (ImageView) findViewById(R.id.img);
        if(!State.isUpdateMode()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ques = et_question.getText().toString();
                    String opt1 = et_opt1.getText().toString();
                    String opt2 = et_opt2.getText().toString();
                    String opt3 = et_opt3.getText().toString();
                    String opt4 = et_opt4.getText().toString();
                    String answerExp = ansExp.getText().toString();
                    if (ques == null || ques.isEmpty() || opt1 == null || opt1.isEmpty() || opt2 == null || opt2.isEmpty() || opt3 == null || opt3.isEmpty() || opt4 == null || opt4.isEmpty() || answerExp == null || answerExp.isEmpty() || ans == 0) {
                        Toast.makeText(TrainerAddQuizItem.this, "Provide all the parameters", Toast.LENGTH_SHORT).show();
                    } else if (url == null || url.isEmpty()) {
                        Toast.makeText(TrainerAddQuizItem.this, Constants.UPLOAD_IMAGE, Toast.LENGTH_SHORT).show();
                    } else {
                        ((Trainer) State.getCurrentUser()).createQuizItem(url, ques, opt1, opt2, opt3, opt4, ans, answerExp);
                        finish();
                    }
                }
            });
        }else{
            button.setText("Update");
            //textView_mode.setText("Update Quiz Item");
            Bundle b = getIntent().getExtras();
            key = b.getString("key");
            value = b.getString("value");
            et_question.setText(value);


            new Trainer().populateQuizItem(key,value,new CallBackInterface() {
                @Override
                public void onCallback(Object value) {
                    QuizItem qi = (QuizItem)value;
                    et_opt1.setText(qi.getOption1());
                    et_opt2.setText(qi.getOption2());
                    et_opt3.setText(qi.getOption3());
                    et_opt4.setText(qi.getOption4());
                    ansExp.setText(qi.getAnsExp());
                    new LoadImage(l,200,300).execute(qi.getPhotoURL());
                    if(qi.getAnswer() == 1) {
                        rb_ans1.setChecked(true);
                        ans = 1;
                    }
                    if(qi.getAnswer() == 2) {
                        rb_ans2.setChecked(true);
                        ans = 2;
                    }
                    if(qi.getAnswer() == 3) {
                        rb_ans3.setChecked(true);
                        ans = 3;
                    }
                    if(qi.getAnswer() == 4) {
                        rb_ans4.setChecked(true);
                        ans = 4;
                    }
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ques = et_question.getText().toString();
                    String opt1 = et_opt1.getText().toString();
                    String opt2 = et_opt2.getText().toString();
                    String opt3 = et_opt3.getText().toString();
                    String opt4 = et_opt4.getText().toString();
                    String answerExp = ansExp.getText().toString();

                    new Trainer().updateQuizItem(key,url, ques, opt1, opt2, opt3, opt4, ans, answerExp);
                    finish();

                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(TrainerAddQuizItem.this, "Image selected, click on upload button", Toast.LENGTH_SHORT).show();
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
            progressDialog.setMessage("Uploading...");
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
                    Toast.makeText(TrainerAddQuizItem.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    url = downloadUrl.toString();
                    Log.i("downloadURL", "download:" + downloadUrl);
                    Toast.makeText(TrainerAddQuizItem.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    Log.i("ImageView", "image:" + imageView);
                    Picasso.with(TrainerAddQuizItem.this).load(downloadUrl).into(imageView);
                }
            });

        } else {
            Toast.makeText(TrainerAddQuizItem.this, Constants.SELECT_IMAGE, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
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
