package com.example.toshimishra.photolearn.Models;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Utilities.CallBackInterface;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.Date;


public class Trainer extends User {

    private PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();

    public void createLearningSession(Date date, Integer moduleNumber, String courseCode) {
        String key = photoLearnDao.getLearningSessionKey();
        LearningSession learningSession = new LearningSession(key,photoLearnDao.getUid(), date, moduleNumber, courseCode);
        photoLearnDao.createLearningSession(learningSession);

    }

    public void deleteLearningSession(String key) {
        photoLearnDao.deleteLearningSession(key);

    }
    public void updateLearningSession(String key,Date date,String courseCode,int Module){
        LearningSession s = new LearningSession(key,photoLearnDao.getUid(),date,Module,courseCode);
        photoLearnDao.updateLearningSession(key,s);
    }

    public void populateLearningSession(String key,final CallBackInterface callBack){
        photoLearnDao.populateLearningSession(key, callBack);
    }


    public void createQuizTitle(String Title) {
        LearningSession learningSession = State.getCurrentSession();
        String key = photoLearnDao.getQuizTitleKey(learningSession.getSessionKey());
        QuizTitle quizTitle = new QuizTitle(key, photoLearnDao.getUid(), Title, learningSession.getSessionKey());
        photoLearnDao.createQuizTitle(quizTitle);
    }

    public void createQuizItem(String url, String question,
                               String option1, String option2, String option3, String option4,
                               int answer, String ansExp) {
        String sessionId = State.getCurrentSession().getSessionKey();
        String titleId = State.getCurrentQuizTitle().getTitleID();
        String quizItemKey = photoLearnDao.getQuizItemKey(sessionId, titleId);
        QuizItem quizItem = new QuizItem(titleId, quizItemKey, url, question, option1, option2, option3, option4, answer, ansExp);
        photoLearnDao.createQuizItem(quizItem, sessionId, titleId);
    }

    public void updateQuizTitle(String key, String value) {
        final QuizTitle quizTitle = new QuizTitle(key, photoLearnDao.getUid(), value, State.getCurrentSession().getSessionKey());
        photoLearnDao.updateQuizTitle(quizTitle);
    }

    public void updateQuizItem(String key, String url, String question,
                               String option1, String option2, String option3, String option4,
                               int answer, String ansExp) {
        final String sessionID = State.getCurrentSession().getSessionKey();
        final String titleID = State.getCurrentQuizTitle().getTitleID();
        final QuizItem quizItem = new QuizItem(titleID, key, url, question, option1, option2, option3, option4, answer, ansExp);
        photoLearnDao.updateQuizItem(quizItem, sessionID, titleID);

    }

    public void deleteQuizTitle(String titleID) {
        String sessionID = State.getCurrentSession().getSessionKey();
        photoLearnDao.deleteQuizTitle(sessionID, titleID);

    }

    public void deleteQuizItem(String key,String url) {
        String sessionID = State.getCurrentSession().getSessionKey();
        String titleID = State.getCurrentQuizTitle().getTitleID();
        photoLearnDao.deleteQuizItem(sessionID, titleID, key,url);
    }
    public void deleteLearningTitle(String key){
        photoLearnDao.deleteLearningTitle(key);
    }

    public void updateLearningTitle(String key, String title){
        LearningTitle learningTitle = new LearningTitle(key, photoLearnDao.getUid(), title, State.getCurrentSession().getSessionKey());
        photoLearnDao.createLearningTitle(learningTitle, key);
    }


}
