package com.example.toshimishra.photolearn.DAO;

import android.telecom.Call;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public interface PhotoLearnDao {

    public void createLearningSession(final LearningSession session);

    public void createQuizTitle(QuizTitle quizTitle);

    public String getQuizTitleKey(String sessionId);

    public void createQuizItem(QuizItem quizItem, String sessionID, String titleID);

    public String getQuizItemKey(String sessionId, String titleId);

    public String getLearningTitleKey();

    public void createLearningTitle(LearningTitle learningtitle, String key);

    public void deleteLearningTitle(String key);

    public void createLearningItem(LearningItem learningItem, String key);

    public void deleteLearningItem(String key,String photoURL);

    public String getLearningItemKey();

    public String getUid();

    public void removeAnswers();

    public void writeResponse(QuizAnswer quizAnswer, String quizItemId);

    public void addUser(FirebaseUser user);

    public void updateQuizTitle(final QuizTitle quizTitle);

    public void updateQuizItem(QuizItem quizItem, String sessionID, String titleID);

    public void deleteQuizItem(String sessionID, String titleID, String key,String url);

    public void deleteQuizTitle(String sessionId, String titleId);

    public void populateQuizItem(String key, final CallBackInterface callBack);

    public String getLearningSessionKey();

    public void deleteLearningSession(String key);

    public void populateLearningSession(String key, final CallBackInterface callBack);

    public void updateLearningSession(String key, LearningSession s);

}
