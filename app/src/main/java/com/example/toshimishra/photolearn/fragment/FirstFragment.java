package com.example.toshimishra.photolearn.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Adapters.SampleRecyclerAdapter;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.ParticipantEditmodeAddLearningTitle;
import com.example.toshimishra.photolearn.ParticipantEditmodeViewLearningItems;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;

public class FirstFragment extends BaseFragment implements SampleRecyclerAdapter.OnItemClickListener {

    private View mFirstFragmentView;
    private RecyclerView mRecyclerView;
    private ArrayList<LearningTitle> learningTitles;
    private ArrayList<LearningTitle> learningTitlesfull;
    private ListOrderedMap<String, Object> map;
    private SampleRecyclerAdapter adapter;
    private ArrayList<String> dataSet;
    private ArrayList<String> dataSetfull;
    private Query query;
    private SearchView mSearchView;


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFirstFragmentView = inflater.inflate(R.layout.fragment_first, container, false);
        mRecyclerView = (RecyclerView) mFirstFragmentView.findViewById(R.id.recy_learning);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchView = (SearchView) mFirstFragmentView.findViewById(R.id.search);
        dataSet = new ArrayList<>();
        dataSetfull = new ArrayList<>();
        learningTitles = new ArrayList<>();
        learningTitlesfull = new ArrayList<>();
        map = new ListOrderedMap<>();
        adapter = new SampleRecyclerAdapter(getContext(), dataSet, map, LearningTitle.class);
        Button button = (Button) mFirstFragmentView.findViewById(R.id.bt_Add_fragment);

        mSearchView.setBackgroundColor(Color.LTGRAY);

        //For Trainer and Participant Show appropriate buttons
        if (State.isTrainerMode())
            button.setVisibility(View.GONE);
        if (!State.isTrainerMode() && !State.isEditMode())
            button.setVisibility(View.GONE);
        if (State.isTrainerMode() || State.isEditMode())
            mSearchView.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setUpdateMode(false);
                startActivity(new Intent(getContext(), ParticipantEditmodeAddLearningTitle.class));
            }
        });


        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        if(!State.isTrainerMode() && State.isEditMode())
            query = database.getReference().child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(State.getCurrentSession().getSessionKey()).orderByChild("userID").equalTo(getUid());
        else
            query = database.getReference().child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(State.getCurrentSession().getSessionKey());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSet.clear();
                dataSetfull.clear();
                learningTitles.clear();
                learningTitlesfull.clear();
                map.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    dataSet.add(val.getValue(LearningTitle.class).getTitle());
                    dataSetfull.add(val.getValue(LearningTitle.class).getTitle());
                    map.put(val.getValue(LearningTitle.class).getTitleID(), val.getValue(LearningTitle.class));
                    Log.d("FirstFragment ", dataSet.get(0));
                    learningTitles.add(val.getValue(LearningTitle.class));
                    learningTitlesfull.add(val.getValue(LearningTitle.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                dataSet.clear();
                learningTitles.clear();
                if (newText == null) {
                    for (int i = 0; i < dataSetfull.size(); i++) {
                        dataSet.add(dataSetfull.get(i));
                        learningTitles.add(learningTitlesfull.get(i));

                    }
                }
                for (int i = 0; i < dataSetfull.size(); i++) {
                    String s = dataSetfull.get(i);
                    if (dataSetfull.get(i).toLowerCase().contains(newText.toLowerCase())) {
                        dataSet.add(s);
                        learningTitles.add(learningTitlesfull.get(i));
                    }
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        });
        return mFirstFragmentView;
    }

    @Override
    public void onItemClick(View view, int position, String name) {

        State.setCurrentLearningTitle(learningTitles.get(position));
        Toast.makeText(getContext(), State.getCurrentLearningTitle().getTitle(), Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putString("flag", "true");
        Intent i = new Intent(getContext(), ParticipantEditmodeViewLearningItems.class);
        i.putExtras(b);
        startActivity(i);
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
