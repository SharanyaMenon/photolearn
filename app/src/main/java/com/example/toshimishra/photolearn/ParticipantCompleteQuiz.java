package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.database.FirebaseDatabase;


public class ParticipantCompleteQuiz extends AppCompatActivity {

    private TextView mScore;
    private Button mButton;
    private Button mButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_complete_quiz);
        String score = (getIntent().getStringExtra("SCORE"));
        String max_score = (getIntent().getStringExtra("MAX_SCORE"));
        mScore =(TextView)findViewById(R.id.score);
        mScore.setText(score+"/"+max_score);
        mButton = (Button)findViewById(R.id.ReadOnly);
        mButton1= (Button)findViewById(R.id.Retake);

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

}
