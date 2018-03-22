package com.example.toshimishra.photolearn.Models;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.Date;

public class Participant extends User {

    private PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();

    public void createLearningTitle(String title) {

        String learningTitleKey = photoLearnDao.getLearningTitleKey();
        LearningTitle learningTitle = new LearningTitle(learningTitleKey, photoLearnDao.getUid(), title, State.getCurrentSession().getSessionKey());
        photoLearnDao.createLearningTitle(learningTitle, learningTitleKey);

    }

    public void createLearningItem(String photoURL, String photoDesc, String GPS) {

        String learningItemKey = photoLearnDao.getLearningItemKey();
        LearningItem learningItem = new LearningItem(learningItemKey, State.getCurrentLearningTitle().getTitleID(), photoURL, photoDesc, GPS, photoLearnDao.getUid());
        photoLearnDao.createLearningItem(learningItem, learningItemKey);
    }

    public void writeResponse(int ans, String quiItemID) {
        QuizAnswer quizAnswer = new QuizAnswer(ans);
        quizAnswer.setTimeStamp(new Date(System.currentTimeMillis()));
        photoLearnDao.writeResponse(quizAnswer, quiItemID);
    }

    public void updateLearningItem(String key, String photoURL, String photoDesc, String GPS) {
        LearningItem learningItem = new LearningItem(key, State.getCurrentLearningTitle().getTitleID(), photoURL, photoDesc, GPS, photoLearnDao.getUid());
        photoLearnDao.createLearningItem(learningItem, key);
    }

    public void deleteLearningItem(String key,String photoURL) {
        photoLearnDao.deleteLearningItem(key,photoURL);
    }


}
