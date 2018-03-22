package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;


public class ParticipantEditmodeAddLearningTitle extends AppCompatActivity {

    Button button;
    EditText et;
    Toolbar toolbar;
    TextView textView,mTitle_LS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_editmode_add_learningtitle);

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

        button = (Button) findViewById(R.id.bt_Add);
        et = (EditText) findViewById(R.id.xh_txt);
        textView = (TextView) findViewById(R.id.textView4);
        mTitle_LS = (TextView)findViewById(R.id.title_LS);
        mTitle_LS.setText(State.getCurrentSession().getSessionID());

        if (!State.isUpdateMode()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = et.getText().toString();
                    if (title == null || title.isEmpty()) {
                        Toast.makeText(ParticipantEditmodeAddLearningTitle.this, "Add Title", Toast.LENGTH_SHORT).show();
                    } else {
                        ((Participant) State.getCurrentUser()).createLearningTitle(title);
                        finish();
                    }
                }
            });
        } else {
            button.setText("Update");
            textView.setText("Update Learning Title");
            Bundle b = getIntent().getExtras();
            final LearningTitle lt =(LearningTitle) b.getSerializable("value");
            et.setText(lt.getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = et.getText().toString();
                    if (value == null || value.isEmpty()) {
                        Toast.makeText(ParticipantEditmodeAddLearningTitle.this, "Add Title", Toast.LENGTH_SHORT).show();
                    } else {
                        ((Trainer) State.getCurrentUser()).updateLearningTitle(lt.getTitleID(), value);
                        State.setUpdateMode(false);
                        finish();
                    }
                }
            });
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
    protected void onStart() {
        super.onStart();
        if (State.isTrainerMode()&&!State.isUpdateMode()) {
            finish();
        }
        Log.d("TrainerSessionsActivity", "onStart********");

    }
}
