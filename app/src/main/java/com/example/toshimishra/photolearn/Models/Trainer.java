package com.example.toshimishra.photolearn.Models;
import android.util.Log;
import android.widget.Toast;


import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
/**
 * Created by toshimishra on 06/03/18.
 */

public class Trainer extends User{
    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public void createLearningSession(Date date,Integer moduleNumber, String courseCode) {
        final LearningSession s = new LearningSession(getUid(), date, moduleNumber, courseCode);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("LearningSessions").child(s.getSessionID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(LearningSession.class)==null)
                    writeSession(s);
                else
                    Log.e("Trainer","Session Already exists");

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });
    }
    private void writeSession(LearningSession s){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions").child(s.getSessionID()).setValue(s);
        mDatabase.child("Users-LearningSessions").child(getUid()).child(s.getSessionID()).setValue(s);
        //todo cleanup
        /*Map<String, Object> add = new HashMap<>();
        add.put("/LearningSessions/" + s.getSessionID(), s);
        add.put("/Users-LearningSessions/" + user.getUid() + "/" + s.getSessionID(), s);
        mDatabase.updateChildren(add);*/

    }
    public void deleteLearningSession(int index){

    }



    public void createQuizTitle(String Title){
        LearningSession ls = State.getCurrentSession(); //hardcodedvalue
        Date timeStamp = Calendar.getInstance().getTime();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("LearningSessions-QuizTitles").child(ls.getSessionID()).push().getKey();
        final QuizTitle qt = new QuizTitle(key,getUid(),Title,ls.getSessionID());
        mDatabase.child("Users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                writeQuizTitle(qt);

    }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeQuizTitle(QuizTitle qt){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions-QuizTitles").child(qt.getSessionID()).child(qt.getTitleID()).setValue(qt);
        //todo cleanup
    }

    public void createQuizItem(String url, String question,
                               String option1,String option2,String option3,String option4,
                               int answer,String ansExp){
        final String sessionID = State.getCurrentSession().getSessionID();
        final String titleID = State.getCurrentQuizTitle().getTitleID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).push().getKey();
        final QuizItem qi = new QuizItem(titleID,key,url,question,option1,option2,option3,option4,answer,ansExp);
        mDatabase.child("Users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                writeQuizItem(qi,sessionID,titleID);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeQuizItem(QuizItem qi,String sessionID,String titleID){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).child(qi.getItemID()).setValue(qi);
        //todo cleanup

    }



}
