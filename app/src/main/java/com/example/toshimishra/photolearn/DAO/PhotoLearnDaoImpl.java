package com.example.toshimishra.photolearn.DAO;

import android.util.Log;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class PhotoLearnDaoImpl implements PhotoLearnDao {

//    public static final String LEARNING_SESSIONS = "LearningSessions";
//    public static final String USER_LEARNING_SESSIONS = "Users-LearningSessions";
//    public static final String USERS = "Users";
//    public static final String LEARNING_SESSION_QUIZ_TITLES = "LearningSessions-QuizTitles";
//    public static final String LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS = "LearningSessions-QuizTitles-QuizItems";
//    public static final String LEARNING_SESSION_LEARNING_TITLES = "LearningSessions-LearningTitles";
//    public static final String USERS_LEARNING_SESSION_LEARNING_TITLES = "Users-LearningSessions-LearningTitles";
//    public static final String LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS = "LearningSessions-LearningTitles-LearningItems";
//    public static final String USERS_QUIZ_TITLE_QUIZ_ITEM_QUIZ_ANSWER = "Users-QuizTitle-QuizItem-QuizAnswer";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public void createLearningItem(LearningItem learningItem, String key) {
        mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(State.getCurrentSession().getSessionID()).child(State.getCurrentLearningTitle().getTitleID()).child(key).setValue(learningItem);
    }

    @Override
    public void createLearningSession(final LearningSession session) {

        mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(session.getSessionID()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        mDatabase.child(Constants.LEARNING_SESSIONS_DB).child(s.getSessionID()).setValue(s);
        mDatabase.child(Constants.USER_LEARNING_SESSIONS_DB).child(getUid()).child(s.getSessionID()).setValue(s);
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
        //todo cleanup

    }

    @Override
    public String getLearningTitleKey() {
        String key = mDatabase.child(Constants.LEARNING_SESSION_LEARNING_TITLES_DB).child(State.getCurrentSession().getSessionID()).push().getKey();
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
    }

    @Override
    public void writeResponse(QuizAnswer quizAnswer, String quizItemId) {
        mDatabase.child(Constants.USERS_QUIZ_TITLE_QUIZ_ITEM_QUIZ_ANSWER_DB).child(getUid()).child(State.getCurrentQuizTitle().getTitleID()).child(quizItemId).setValue(quizAnswer);
    }

    @Override
    public void addUser(FirebaseUser user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.USERS_DB).child(user.getUid()).setValue(user.getEmail());
    }


}
