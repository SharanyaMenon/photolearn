package com.iss.ft03se.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iss.ft03se.photolearn.Utilities.Constants;
import com.iss.ft03se.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;

public class EnterLearningsessionActivity extends AppCompatActivity {
    Button button;
    EditText text;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_learningsession);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        // toolbar.setLogo(R.drawable.timg);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        button = (Button) findViewById(R.id.GO);
        text = (EditText) findViewById(R.id.editText2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionID = text.getText().toString();
                if (sessionID == null || sessionID.isEmpty()) {
                    Toast.makeText(EnterLearningsessionActivity.this, Constants.ENTER_LEARNING_SESSION, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getBaseContext(), ChooseLearningsessionModeActivity.class);
                    intent.putExtra("SESSION_ID", sessionID);
                    startActivity(intent);
                }

            }
        });

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
            startActivity(new Intent(this, LoginActivity.class));
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
    protected void onStart() {
        super.onStart();
        if(State.isTrainerMode()){
            finish();
        }


    }
}
