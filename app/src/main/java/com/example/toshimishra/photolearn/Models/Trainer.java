package com.example.toshimishra.photolearn.Models;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.Date;


public class Trainer extends User {

    private PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();

    public void createLearningSession(Date date, Integer moduleNumber, String courseCode) {
        LearningSession learningSession = new LearningSession(photoLearnDao.getUid(), date, moduleNumber, courseCode);
        photoLearnDao.createLearningSession(learningSession);

    }

    public void deleteLearningSession(int index) {


    }


    public void createQuizTitle(String Title) {
        LearningSession learningSession = State.getCurrentSession();
        String key = photoLearnDao.getQuizTitleKey(learningSession.getSessionID());
        QuizTitle quizTitle = new QuizTitle(key, photoLearnDao.getUid(), Title, learningSession.getSessionID());
        photoLearnDao.createQuizTitle(quizTitle);
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

    public void updateQuizTitle(String key, String value) {
        final QuizTitle quizTitle = new QuizTitle(key, photoLearnDao.getUid(), value, State.getCurrentSession().getSessionID());
        photoLearnDao.updateQuizTitle(quizTitle);
    }

    public void populateQuizItem(String key, String value, final CallBackInterface callBack) {
        photoLearnDao.populateQuizItem(key, callBack);
    }

    public void updateQuizItem(String key, String url, String question,
                               String option1, String option2, String option3, String option4,
                               int answer, String ansExp) {
        final String sessionID = State.getCurrentSession().getSessionID();
        final String titleID = State.getCurrentQuizTitle().getTitleID();
        final QuizItem quizItem = new QuizItem(titleID, key, url, question, option1, option2, option3, option4, answer, ansExp);
        photoLearnDao.updateQuizItem(quizItem, sessionID, titleID);

    }

    public void deleteQuizTitle(String titleID) {
        String sessionID = State.getCurrentSession().getSessionID();
        photoLearnDao.deleteQuizTitle(sessionID, titleID);

    }

    public void deleteQuizItem(String key) {
        String sessionID = State.getCurrentSession().getSessionID();
        String titleID = State.getCurrentQuizTitle().getTitleID();
        photoLearnDao.deleteQuizItem(sessionID, titleID, key);
    }
    public void deleteLearningTitle(String key){
        photoLearnDao.deleteLearningTitle(key);
    }

    public void updateLearningTitle(String key, String title){
        LearningTitle learningTitle = new LearningTitle(key, photoLearnDao.getUid(), title, State.getCurrentSession().getSessionID());
        photoLearnDao.createLearningTitle(learningTitle, key);
    }


}
