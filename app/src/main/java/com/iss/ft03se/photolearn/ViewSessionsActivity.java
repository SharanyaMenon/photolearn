package com.iss.ft03se.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iss.ft03se.photolearn.Adapters.RecyclerAdapter;
import com.iss.ft03se.photolearn.Models.LearningSession;
import com.iss.ft03se.photolearn.Utilities.Constants;
import com.iss.ft03se.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.List;


public class ViewSessionsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<String> dataSet;
    private List<LearningSession> sessionList;
    private RecyclerAdapter adapter;
    private ListOrderedMap<String, Object> map;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sessions);
        Log.d("ViewSessionsActivity", "onCreate");

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


        dataSet = new ArrayList<>();
        sessionList = new ArrayList<>();
        map = new ListOrderedMap<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recy);
        //Set the layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, dataSet, map, LearningSession.class);
        //   Setadapter
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child(Constants.USER_LEARNING_SESSIONS_DB).child(getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map.clear();
                sessionList.clear();
                dataSet.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    dataSet.add(val.getValue(LearningSession.class).getSessionID());
                    map.put(val.getValue(LearningSession.class).getSessionKey(), val.getValue(LearningSession.class));
                    sessionList.add(val.getValue(LearningSession.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button button = (Button) findViewById(R.id.bt_Add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                State.setUpdateMode(false);
                startActivity(new Intent(ViewSessionsActivity.this, AddSessionActivity.class));
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
    public void onItemClick(View view, int position, String name) {
        State.setCurrentSession(sessionList.get(position));
        Toast.makeText(getApplicationContext(), "click " + position, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ViewSessionsActivity.this, TrainerTitlesActivity.class));
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!State.isTrainerMode()) {
            finish();
        }
    }

}


