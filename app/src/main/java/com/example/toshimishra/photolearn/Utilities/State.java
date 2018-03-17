package com.example.toshimishra.photolearn.Utilities;

import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by toshimishra on 12/03/18.
 */

public class State {
    private static LearningSession currentSession;
    private static LearningTitle currentLearningTitle;
    private  static QuizTitle currentQuizTitle;

    public static boolean isReadOnlyQuiz() {
        return readOnlyQuiz;
    }

    public static void setReadOnlyQuiz(boolean readOnlyQuiz) {
        State.readOnlyQuiz = readOnlyQuiz;
    }

    private static boolean readOnlyQuiz = false;

    public static QuizTitle getCurrentQuizTitle() {
        return currentQuizTitle;
    }

    public static void setCurrentQuizTitle(QuizTitle currentQuizTitle) {
        State.currentQuizTitle = currentQuizTitle;
    }

    private static boolean editMode = false;
    private static boolean trainerMode = true;

    public static boolean isEditMode(){
        return editMode;
    }
    public static boolean isTrainerMode(){
        return trainerMode;
    }
    public static void setEditMode(boolean b){
        editMode = b;
    }
    public static void setTrainerMode(boolean b){
        trainerMode = b;
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
    public static void removeAnswers(){
        FirebaseDatabase.getInstance().getReference().child("Users-QuizTitle-QuizItem-QuizAnswer").child(getUid()).child(State.getCurrentQuizTitle().getTitleID()).removeValue();
    }
    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
