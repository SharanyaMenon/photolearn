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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ParticipantChoiceLearningsessionMode extends AppCompatActivity {

    TextView text_ls;

    Button button;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_choice_learningsession_mode);
        text_ls = (TextView) findViewById(R.id.title_LS);
        button = (Button) findViewById(R.id.Go);



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


        final String session_id = getIntent().getStringExtra("SESSION_ID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query sessionQuery= database.getReference().child("LearningSessions").orderByChild("sessionID").equalTo(session_id);
        sessionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getChildrenCount()==1){
                for(DataSnapshot val : dataSnapshot.getChildren())
                    State.setCurrentSession(val.getValue(LearningSession.class));
                text_ls.setText(State.getCurrentSession().getSessionID());
                } else {
                    Toast.makeText(getBaseContext(), Constants.ENTER_VALID_SESSION_ID, Toast.LENGTH_LONG).show();
                finish();
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (State.getCurrentSession() != null) {

                    if (((RadioButton) findViewById(R.id.edit_button)).isChecked()) {
                        State.setEditMode(true);
                        startActivity(new Intent(ParticipantChoiceLearningsessionMode.this, ParticipantEditModeLearningTitlesQuizTiltlesActivity.class));
                    } else if (((RadioButton) findViewById(R.id.view_button)).isChecked()) {
                        State.setEditMode(false);
                        startActivity(new Intent(ParticipantChoiceLearningsessionMode.this, ParticipantEditModeLearningTitlesQuizTiltlesActivity.class));
                    } else
                        Toast.makeText(getBaseContext(), "Select a Mode", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        if(State.isTrainerMode()){
            finish();
        }


    }
}
