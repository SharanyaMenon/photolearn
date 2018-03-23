package com.iss.ft03se.photolearn.Utilities;

import android.content.Context;
import android.util.Log;

import com.iss.ft03se.photolearn.DAO.PhotoLearnDao;
import com.iss.ft03se.photolearn.DAO.PhotoLearnDaoImpl;
import com.iss.ft03se.photolearn.Models.LearningSession;
import com.iss.ft03se.photolearn.Models.LearningTitle;
import com.iss.ft03se.photolearn.Models.Participant;
import com.iss.ft03se.photolearn.Models.QuizTitle;
import com.iss.ft03se.photolearn.Models.Trainer;
import com.iss.ft03se.photolearn.Models.User;
import com.iss.ft03se.photolearn.EnterLearningsessionActivity;
import com.iss.ft03se.photolearn.ViewSessionsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Created by toshimishra on 12/03/18.
 */

public class State {

    private static LearningSession currentSession;

    private static LearningTitle currentLearningTitle;

    private static QuizTitle currentQuizTitle;

    private static User currentUser;

    private static boolean readOnlyQuiz = false;

    private static boolean editMode = false;

    private static boolean trainerMode = true;

    private static boolean updateMode = false;

    private static PhotoLearnDao photoLearnDao = new PhotoLearnDaoImpl();

    private static GeoLocation geoLocation =null;

    public static void setGeoLocation(Context context) throws IOException {
        geoLocation = new GeoLocation(context);
    }

    public static boolean isGeoLocationset(){
        return (geoLocation!=null);
    }

    public static boolean isReadOnlyQuiz() {
        return readOnlyQuiz;
    }

    public static void setReadOnlyQuiz(boolean readOnlyQuiz) {
        State.readOnlyQuiz = readOnlyQuiz;
    }

    public static QuizTitle getCurrentQuizTitle() {
        return currentQuizTitle;
    }

    public static void setCurrentQuizTitle(QuizTitle currentQuizTitle) {
        State.currentQuizTitle = currentQuizTitle;
    }

    public static boolean isEditMode() {
        return editMode;
    }

    public static boolean isTrainerMode() {
        return trainerMode;
    }

    public static void setEditMode(boolean b) {
        editMode = b;
    }

    public static void setTrainerMode(boolean b) {
        trainerMode = b;
    }

    public static boolean isUpdateMode() {
        return updateMode;
    }

    public static void setUpdateMode(boolean b) {
        updateMode = b;
    }

    public static LearningSession getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(LearningSession currentSession) {
        State.currentSession = currentSession;
    }

    public static LearningTitle getCurrentLearningTitle() {
        return currentLearningTitle;
    }

    public static void setCurrentLearningTitle(LearningTitle currentLearningTitle) {
        State.currentLearningTitle = currentLearningTitle;
    }

    public static void removeAnswers() {
        photoLearnDao.removeAnswers();
    }

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User u) {
        currentUser = u;
    }

    public static Class changeMode() {
        State.setTrainerMode(!State.isTrainerMode());
        if (State.isTrainerMode()) {
            currentUser = new Trainer();
            Log.d("Trainer :", "" + (currentUser instanceof Trainer));
            return ViewSessionsActivity.class;
        } else {
            currentUser = new Participant();
            return EnterLearningsessionActivity.class;
        }

    }

}
