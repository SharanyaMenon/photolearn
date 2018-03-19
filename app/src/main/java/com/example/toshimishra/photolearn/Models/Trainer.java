package com.example.toshimishra.photolearn.Models;
import android.icu.util.*;
import android.util.Log;

import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.*;
import java.util.Calendar;


public class Trainer extends User {

    private PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();

    public void createLearningSession(Date date, Integer moduleNumber, String courseCode) {
        LearningSession learningSession = new LearningSession(photoLearnDao.getUid(), date, moduleNumber, courseCode);
        photoLearnDao.createLearningSession(learningSession);

    }

    public void deleteLearningSession(int index) {

    }


    public void createQuizTitle(String Title) {
        LearningSession learningSession = State.getCurrentSession(); //hardcodedvalue
        String key = photoLearnDao.getQuizTitleKey(learningSession.getSessionID());
        QuizTitle quizTitle = new QuizTitle(key, photoLearnDao.getUid(), Title, learningSession.getSessionID());
        photoLearnDao.createQuizTitle(quizTitle);
//
    }

    public void createQuizItem(String url, String question,
                               String option1, String option2, String option3, String option4,
                               int answer, String ansExp) {
        String sessionId = State.getCurrentSession().getSessionID();
        String titleId = State.getCurrentQuizTitle().getTitleID();
        String quizItemKey = photoLearnDao.getQuizItemKey(sessionId, titleId);
        QuizItem quizItem = new QuizItem(titleId, quizItemKey, url, question, option1, option2, option3, option4, answer, ansExp);
        photoLearnDao.createQuizItem(quizItem, sessionId, titleId);
    }
    public void updateQuizTitle(String key,String value){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final QuizTitle qt = new QuizTitle(key,photoLearnDao.getUid(),value,State.getCurrentSession().getSessionID());
        mDatabase.child("Users").child(photoLearnDao.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void populateQuizItem(String key, String value, final CallBackInterface callBack){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDatabaseRef = mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(State.getCurrentSession().getSessionID()).child(State.getCurrentQuizTitle().getTitleID()).child(key);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                QuizItem qi = snapshot.getValue(QuizItem.class);
                callBack.onCallback(qi);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("onCancelled", " cancelled");
            }
        });
    }

    public void updateQuizItem(String key,String url,String question,
                               String option1,String option2,String option3,String option4,
                               int answer,String ansExp){
        final String sessionID = State.getCurrentSession().getSessionID();
        final String titleID = State.getCurrentQuizTitle().getTitleID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final QuizItem qi = new QuizItem(titleID,key,url,question,option1,option2,option3,option4,answer,ansExp);
        mDatabase.child("Users").child(photoLearnDao.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void deleteQuizTitle(String titleID){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String sessionID = State.getCurrentSession().getSessionID();
        mDatabase.child("LearningSessions-QuizTitles").child(sessionID).child(titleID).removeValue();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).removeValue();
        //todo move to DAO
    }

    public void deleteQuizItem(String key){
        String sessionID = State.getCurrentSession().getSessionID();
        String titleID = State.getCurrentQuizTitle().getTitleID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).child(key).removeValue();
        //todo move to DAO
    }
    private void writeQuizTitle(QuizTitle qt){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions-QuizTitles").child(qt.getSessionID()).child(qt.getTitleID()).setValue(qt);
        //todo cleanup
        //todo move to DAO
    }

    private void writeQuizItem(QuizItem qi,String sessionID,String titleID){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).child(qi.getItemID()).setValue(qi);
        //todo cleanup
        //todo move to DAO

    }


}
