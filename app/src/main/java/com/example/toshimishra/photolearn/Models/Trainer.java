package com.example.toshimishra.photolearn.Models;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.Date;

/**
 * Created by toshimishra on 06/03/18.
 */

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

}
