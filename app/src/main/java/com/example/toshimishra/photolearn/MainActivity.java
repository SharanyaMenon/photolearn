package com.example.toshimishra.photolearn;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.Arrays;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class MainActivity extends AppCompatActivity {

    SignInButton button;
    FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String EMAIL = "email";
    //    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    RadioButton rb_ans1, rb_ans2;
    int ans;

    String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager callbackManager;
    private DatabaseReference mDatabase;

    private PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, 1);

        button = (SignInButton) findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        final RadioButton trainer = (RadioButton) findViewById(R.id.trainer_button);

//        if (!State.isGeoLocationset()) {
//            try {
//                State.setGeoLocation(getApplicationContext());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        callbackManager = CallbackManager.Factory.create();

        rb_ans1 = (RadioButton) findViewById(R.id.trainer_button);
        rb_ans2 = (RadioButton) findViewById(R.id.participant_button);


        rb_ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 1;
            }
        });
        rb_ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans = 2;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ans == 0) {
                    Toast.makeText(MainActivity.this, "Select the login identity first.", Toast.LENGTH_SHORT).show();
                } else {
                    signIn();
                }

            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }


        mAuthListener =
                new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (trainer.isChecked()) {
                                if (mGoogleApiClient.isConnected())
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                State.setTrainerMode(true);
                                State.setCurrentUser(new Trainer());
                                startActivity(new Intent(MainActivity.this, TrainerSessionsActivity.class));
                                finish();

                            } else if (rb_ans2.isChecked()) {
                                if (mGoogleApiClient.isConnected())
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                State.setTrainerMode(false);
                                State.setCurrentUser(new Participant());
                                startActivity(new Intent(MainActivity.this, ParticipantEnterLearningsessionActivity.class));
                                finish();


                            } else {
                                Toast.makeText(MainActivity.this, "Select the login identity first.", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }
                };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedIn = loginResult.getAccessToken() != null;
                handleFacebookAccessToken(loginResult.getAccessToken());
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        // [END initialize_auth]
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            super.onActivityResult(requestCode, resultCode, data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(MainActivity.this, "Auth went wrong", Toast.LENGTH_SHORT);
                //sign in failed
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Activity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Activity", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (rb_ans1.isChecked()) {

                                State.setTrainerMode(true);
                                State.setCurrentUser(new Trainer());
                                startActivity(new Intent(MainActivity.this, TrainerSessionsActivity.class));
                                finish();

                            } else if (rb_ans2.isChecked()) {

                                State.setTrainerMode(false);
                                State.setCurrentUser(new Participant());
                                startActivity(new Intent(MainActivity.this, ParticipantEnterLearningsessionActivity.class));
                                finish();


                            } else {
                                Toast.makeText(MainActivity.this, "Select the login identity first.", Toast.LENGTH_SHORT).show();
                            }
                            photoLearnDao.addUser(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Activity", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT);

                            //updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final RadioButton trainer = (RadioButton) findViewById(R.id.trainer_button);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            photoLearnDao.addUser(user);
                            if (trainer.isChecked()) {
                                State.setTrainerMode(true);
                                State.setCurrentUser(new Trainer());

                                startActivity(new Intent(MainActivity.this, TrainerSessionsActivity.class));
                                finish();
                            } else {
                                State.setTrainerMode(false);
                                State.setCurrentUser(new Participant());

                                startActivity(new Intent(MainActivity.this, ParticipantEnterLearningsessionActivity.class));
                                finish();

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Log.d("back pressed", "#####");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


//    private void addUser(FirebaseUser user) {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child(Constants.USERS_DB).child(user.getUid()).setValue(user.getEmail());
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}