package com.example.toshimishra.photolearn.DAO;

import android.util.Log;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class PhotoLearnDaoImpl implements PhotoLearnDao {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public void createLearningItem(LearningItem learningItem, String key) {
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentLearningTitle().getTitleID()).child(key).setValue(learningItem);
        mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentLearningTitle().getTitleID()).child(learningItem.getItemID()).setValue(learningItem.getPhotoURL());
    }

    @Override
    public void createLearningSession(final LearningSession session) {

        mDatabase.child(Constants.LEARNING_SESSIONS_DB).orderByChild("sessionID").equalTo(session.getSessionID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(LearningSession.class) == null) {
                    writeSession(session);
                } else {
                    Log.e("Trainer", "Session Already exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });

    }

    private void writeSession(LearningSession s) {
        mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(s.getSessionKey()).setValue(s);
        mDatabase.child(Constants.USER_LEARNING_SESSIONS_DB).child(getUid()).child(s.getSessionKey()).setValue(s);
        //todo cleanup
        /*Map<String, Object> add = new HashMap<>();
        add.put("/LearningSessions/" + s.getSessionID(), s);
        add.put("/Users-LearningSessions/" + user.getUid() + "/" + s.getSessionID(), s);
        mDatabase.updateChildren(add);*/

    }

    @Override
    public String getQuizTitleKey(String sessionId) {
        String key = mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_DB).child(sessionId).push().getKey();
        return key;
    }

    @Override
    public void createQuizTitle(final QuizTitle quizTitle) {
        mDatabase.child(Constants.USERS_DB).child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                writeQuizTitle(quizTitle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });

    }

    private void writeQuizTitle(QuizTitle qt) {
        mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_DB).child(qt.getSessionID()).child(qt.getTitleID()).setValue(qt);
        //todo cleanup
    }

    @Override
    public String getQuizItemKey(String sessionId, String titleId) {
        String key = mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS_DB).child(sessionId).child(titleId).push().getKey();
        return key;
    }

    @Override
    public void createQuizItem(final QuizItem quizItem, final String sessionID, final String titleID) {
        mDatabase.child(Constants.USERS_DB).child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                writeQuizItem(quizItem, sessionID, titleID);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeQuizItem(QuizItem qi, String sessionID, String titleID) {
        mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS_DB).child(sessionID).child(titleID).child(qi.getItemID()).setValue(qi);
        mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(sessionID).child(titleID).child(qi.getItemID()).setValue(qi.getPhotoURL());
        //todo cleanup

    }

    @Override
    public String getLearningTitleKey() {
        String key = mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(State.getCurrentSession().getSessionKey()).push().getKey();
        return key;
    }

    @Override
    public void createLearningTitle(LearningTitle learningtitle, String key) {
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(learningtitle.getSessionID()).child(key).setValue(learningtitle);
        mDatabase.child(Constants.USERS_LEARNING_SESSION_LEARNING_TITLES_DB).child(getUid()).child(learningtitle.getSessionID()).child(key).setValue(learningtitle);
    }

    @Override
    public String getLearningItemKey() {
        String key = mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(State.getCurrentSession().getSessionID()).child(State.getCurrentLearningTitle().getTitleID()).push().getKey();
        return key;
    }

    @Override
    public void removeAnswers() {
        FirebaseDatabase.getInstance().getReference().child(Constants.USERS_QUIZ_TITLE_QUIZ_ITEM_QUIZ_ANSWER_DB).child(getUid()).child(State.getCurrentQuizTitle().getTitleID()).removeValue();

        mDatabase.child(Constants.LEARNINGSESSIONS_QUIZ_TITLES_QUIZ_ITEMS_QUIZ_ANSWERS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentQuizTitle().getTitleID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    val.child(getUid()).getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void writeResponse(QuizAnswer quizAnswer, String quizItemId) {
        mDatabase.child(Constants.LEARNINGSESSIONS_QUIZ_TITLES_QUIZ_ITEMS_QUIZ_ANSWERS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentQuizTitle().getTitleID()).child(quizItemId).child(getUid()).setValue(quizAnswer);
    }

    @Override
    public void addUser(FirebaseUser user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.USERS_DB).child(user.getUid()).setValue(user.getEmail());
    }

    public void updateQuizTitle(final QuizTitle quizTitle) {
        mDatabase.child(Constants.USERS_DB).child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                writeQuizTitle(quizTitle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });

    }

    public void updateQuizItem(final QuizItem quizItem, final String sessionID, final String titleID) {
        mDatabase.child(Constants.USERS_DB).child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                writeQuizItem(quizItem, sessionID, titleID);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Trainer", "Trainer:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void deleteQuizItem(String sessionID, String titleID, String key,String url) {

        deleteImage(url);
        mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(sessionID).child(titleID).child(key).removeValue();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionID).child(titleID).child(key).removeValue();
        mDatabase.child(Constants.LEARNINGSESSIONS_QUIZ_TITLES_QUIZ_ITEMS_QUIZ_ANSWERS_DB).child(sessionID).child(titleID).child(key).removeValue();
    }

    @Override
    public void deleteQuizTitle(String sessionId, String titleId) {
        mDatabase.child("LearningSessions-QuizTitles").child(sessionId).child(titleId).removeValue();
        mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(sessionId).child(titleId).removeValue();
        mDatabase.child(Constants.LEARNINGSESSIONS_QUIZ_TITLES_QUIZ_ITEMS_QUIZ_ANSWERS_DB).child(sessionId).child(titleId).removeValue();
        DatabaseReference mDatabaseRef = mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(sessionId).child(titleId);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    String photo = val.getValue(String.class);
                    deleteImage(photo);
                }
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void populateQuizItem(String key, final CallBackInterface callBack) {
        DatabaseReference mDatabaseRef = mDatabase.child("LearningSessions-QuizTitles-QuizItems").child(State.getCurrentSession().getSessionKey()).child(State.getCurrentQuizTitle().getTitleID()).child(key);
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

    @Override
    public String getLearningSessionKey() {
       return mDatabase.child(Constants.LEARNING_SESSIONS_DB).push().getKey();
    }

    @Override
    public void deleteLearningSession(String key) {
        mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(key).removeValue();
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(key).removeValue();
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(key).removeValue();
        mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_DB).child(key).removeValue();
        mDatabase.child(Constants.LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS_DB).child((key)).removeValue();
        mDatabase.child(Constants.USER_LEARNING_SESSIONS_DB).child(getUid()).child(key).removeValue();
        mDatabase.child(Constants.USERS_LEARNING_SESSION_LEARNING_TITLES_DB).child(getUid()).child(key).removeValue();
        mDatabase.child(Constants.LEARNINGSESSIONS_QUIZ_TITLES_QUIZ_ITEMS_QUIZ_ANSWERS_DB).child(key).removeValue();

        DatabaseReference mDatabaseRef = mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(key);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot val : dataSnapshot.getChildren()){// Iterate through all titleID
                    for(DataSnapshot val2:val.getChildren()){//Iterate through all Items
                        String photo = val2.getValue(String.class);
                        deleteImage(photo);
                    }
                }
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void populateLearningSession(String key, final CallBackInterface callBack) {
        DatabaseReference mDatabaseRef = mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(key);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                LearningSession session = snapshot.getValue(LearningSession.class);
                callBack.onCallback(session);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("onCancelled", " cancelled");
            }
        });
    }

    @Override
    public void updateLearningSession(String key, LearningSession s) {
        mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(key).setValue(s);
        mDatabase.child(Constants.USER_LEARNING_SESSIONS_DB).child(getUid()).child(s.getSessionKey()).setValue(s);

    }
    @Override
    public void deleteLearningItem(String key,String photoURL){
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentLearningTitle().getTitleID()).child(key).removeValue();
        deleteImage(photoURL);
        mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentLearningTitle().getTitleID()).child(key).removeValue();
    }

    @Override
    public void deleteLearningTitle(String key){
        String sessionKey = State.getCurrentSession().getSessionKey();
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(sessionKey).child(key).removeValue();
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(sessionKey).child(key).removeValue();
        mDatabase.child(Constants.USERS_LEARNING_SESSION_LEARNING_TITLES_DB).child(getUid()).child(sessionKey).child(key).removeValue();
        DatabaseReference mDatabaseRef = mDatabase.child(Constants.LEARNINGSESSIONS_TITLES_ITEMS_PHOTO_DB).child(sessionKey).child(key);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    String photo = val.getValue(String.class);
                    deleteImage(photo);
                }
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteImage(String photoURL){
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(photoURL);
        ref.delete();

    }

}
