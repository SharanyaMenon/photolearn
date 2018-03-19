package com.example.toshimishra.photolearn.DAO;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;



public interface PhotoLearnDao {

    public void createLearningSession(final LearningSession session);

    public void createQuizTitle(QuizTitle quizTitle);

    public String getQuizTitleKey(String sessionId);

    public void createQuizItem(QuizItem quizItem, String sessionID, String titleID);

    public String getQuizItemKey(String sessionId, String titleId);

    public String getLearningTitleKey();

    public void createLearningTitle(LearningTitle learningtitle, String key);

    public void createLearningItem(LearningItem learningItem, String key);

    public String getLearningItemKey();

    public String getUid();

    public void removeAnswers();


}
