package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;


public class ParticipantCompleteQuiz extends AppCompatActivity {
    TextView mTitle_LS,mTitle_Q;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_complete_quiz);
        mTitle_LS = (TextView)findViewById(R.id.title_LS);
        mTitle_Q = (TextView)findViewById(R.id.title_Q);
        mTitle_LS.setText(State.getCurrentSession().getSessionID());
        mTitle_Q.setText(State.getCurrentQuizTitle().getTitle());
        String score = (getIntent().getStringExtra("SCORE"));
        String max_score = (getIntent().getStringExtra("MAX_SCORE"));
        TextView mScore =(TextView)findViewById(R.id.score);
        mScore.setText(score+"/"+max_score);
        Button mButton = (Button)findViewById(R.id.ReadOnly);
        Button mButton1= (Button)findViewById(R.id.Retake);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setReadOnlyQuiz(true);
                startActivity(new Intent(ParticipantCompleteQuiz.this,ParticipantAttemptQuizItemActivity.class));
                finish();
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.removeAnswers();
                startActivity(new Intent(ParticipantCompleteQuiz.this,ParticipantAttemptQuizItemActivity.class));
                finish();
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
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return false;
        }
        else if(i == R.id.action_switch){
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return false;
        }
        else {
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
