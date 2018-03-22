package com.example.toshimishra.photolearn.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Adapters.SampleRecyclerAdapter;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.ParticipantAttemptQuizItemActivity;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.TrainerAddQuizTitle;
import com.example.toshimishra.photolearn.TrainerViewQuizItems;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class SecondFragment extends BaseFragment implements SampleRecyclerAdapter.OnItemClickListener {

    private View mSecondFragmentView;

    private RecyclerView mRecyclerView;

    private DatabaseReference mDatabase;

    private ListOrderedMap<String, Object> map; //Map <TitleID,Title>

    private ArrayList<QuizTitle> quizTitles;

    private SampleRecyclerAdapter adapter;

    private ArrayList<String> dataSet;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mSecondFragmentView = inflater.inflate(R.layout.fragment_second, container, false);
        mRecyclerView = (RecyclerView) mSecondFragmentView.findViewById(R.id.recy_quiz);
        //Set the layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //    ArrayList<String> list = new ArrayList<>();
        //      list.add("test1");
        //   list.add("test2");


        dataSet = new ArrayList<>();
        map = new ListOrderedMap<>();
        quizTitles = new ArrayList<>();
        adapter = new SampleRecyclerAdapter(getContext(), dataSet, map, QuizTitle.class);
        Log.d("adapter initialised", "");
        Button button = (Button) mSecondFragmentView.findViewById(R.id.bt_Add_fragment);
        if (!State.isTrainerMode())
            button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (State.isTrainerMode()) {
                    State.setUpdateMode(false);
                    startActivity(new Intent(getContext(), TrainerAddQuizTitle.class));
                }
            }
        });
//        SampleRecyclerAdapter adapter = new SampleRecyclerAdapter(getContext(), Strings.string_frag2);

        //è®¾ç½®adapter
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference().child("LearningSessions-QuizTitles").child(State.getCurrentSession().getSessionKey()).orderByChild("userID").equalTo(getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSet.clear();
                quizTitles.clear();
                map.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    dataSet.add(val.getValue(QuizTitle.class).getTitle());
                    quizTitles.add(val.getValue(QuizTitle.class));
                    map.put(val.getValue(QuizTitle.class).getTitleID(), val.getValue(QuizTitle.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mSecondFragmentView;
    }

    @Override
    public void onItemClick(View view, int position, String name) {
        State.setCurrentQuizTitle(quizTitles.get(position));
        Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
        if (State.isTrainerMode())
            startActivity(new Intent(getContext(), TrainerViewQuizItems.class));
        else {
            State.setReadOnlyQuiz(false);
            startActivity(new Intent(getContext(), ParticipantAttemptQuizItemActivity.class));
        }
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
