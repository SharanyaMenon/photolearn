package com.example.toshimishra.photolearn.Utilities;

import android.content.Intent;
import android.util.Log;

import com.example.toshimishra.photolearn.DAO.PhotoLearnDao;
import com.example.toshimishra.photolearn.DAO.PhotoLearnDaoImpl;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Models.User;
import com.example.toshimishra.photolearn.ParticipantChoiceLearningsessionMode;
import com.example.toshimishra.photolearn.ParticipantEnterLearningsessionActivity;
import com.example.toshimishra.photolearn.TrainerSessionsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.ProcessingInstruction;

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

    //todo move to DAO
    public static void removeAnswers() {
        photoLearnDao.removeAnswers();
//        FirebaseDatabase.getInstance().getReference().child("Users-QuizTitle-QuizItem-QuizAnswer").child(getUid()).child(State.getCurrentQuizTitle().getTitleID()).removeValue();
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
            return TrainerSessionsActivity.class;
        } else {
            currentUser = new Participant();
            return ParticipantEnterLearningsessionActivity.class;
        }

    }
}
