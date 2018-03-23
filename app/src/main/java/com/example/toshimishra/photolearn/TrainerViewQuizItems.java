package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Adapters.SampleRecyclerAdapter;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUGANTHI on 15-03-2018.
 */

public class TrainerViewQuizItems extends AppCompatActivity implements SampleRecyclerAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<String> dataSet;
    private List<QuizItem> quizItems;
    private SampleRecyclerAdapter adapter;
    private ListOrderedMap<String, Object> map;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_view_quizitems);


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
        quizItems = new ArrayList<>();
        map = new ListOrderedMap<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recy_quizitem);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SampleRecyclerAdapter(this, dataSet, map, QuizItem.class);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child(Constants.LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentQuizTitle().getTitleID());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSet.clear();
                quizItems.clear();
                map.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    dataSet.add(val.getValue(QuizItem.class).getQuestion());
                    quizItems.add(val.getValue(QuizItem.class));
                    map.put(val.getValue(QuizItem.class).getItemID(), val.getValue(QuizItem.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Button button = (Button) findViewById(R.id.bt_Add);
        TextView text_ls = (TextView) findViewById(R.id.title_LS);
        text_ls.setText(State.getCurrentSession().getSessionID());
        TextView text_q = (TextView) findViewById(R.id.title_Q);
        text_q.setText(State.getCurrentQuizTitle().getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                State.setUpdateMode(false);
                startActivity(new Intent(TrainerViewQuizItems.this, TrainerAddQuizItem.class));
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, String name) {

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
        if(!State.isTrainerMode()){
            finish();
        }
    }

}
