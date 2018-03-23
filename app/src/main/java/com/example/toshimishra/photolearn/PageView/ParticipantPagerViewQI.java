package com.example.toshimishra.photolearn.PageView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.ParticipantAttemptQuizItemActivity;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.Images;
import com.example.toshimishra.photolearn.Utilities.LoadImage;
import com.example.toshimishra.photolearn.Utilities.State;

public class ParticipantPagerViewQI implements LoadImage.ImageCallBack {


    private final View mRootView;
    private TextView mQuiz;
    private RadioButton mOption1, mOption2, mOption3, mOption4;
    private int choiceselected;
    private QuizItem quizItem;
    private ImageView mImageView;
    private TextView mExplain;

    public ParticipantAttemptQuizItemActivity mMainActivity;
    public final LayoutInflater mInflater;

    public ParticipantPagerViewQI(ParticipantAttemptQuizItemActivity mainActivity, QuizItem qItem, int choiceselected) {
        super();
        mMainActivity = mainActivity;
        mInflater = LayoutInflater.from(mMainActivity);
        mMainActivity = mainActivity;
        mRootView = initView();

        this.quizItem = qItem;
        this.choiceselected = choiceselected;

    }


    public View initView() {
        final Participant participant = (Participant) State.getCurrentUser();

        View itemView = mInflater.inflate(R.layout.activity_participant_pager_view_qi, null);
        mQuiz = (TextView) itemView.findViewById(R.id.title_Quest);
        mOption1 = (RadioButton) itemView.findViewById(R.id.radioButton1);
        mOption2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
        mOption3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
        mOption4 = (RadioButton) itemView.findViewById(R.id.radioButton4);
        mImageView = (ImageView) itemView.findViewById(R.id.img);
        mExplain = (TextView) itemView.findViewById(R.id.explain_Answer);
        mExplain.setVisibility(View.GONE);

        if (!State.isReadOnlyQuiz()) {
            mOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participant.writeResponse(1, quizItem.getItemID());

                }
            });
            mOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participant.writeResponse(2, quizItem.getItemID());

                }
            });
            mOption3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participant.writeResponse(3, quizItem.getItemID());

                }
            });
            mOption4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participant.writeResponse(4, quizItem.getItemID());

                }
            });
        } else {
            mOption1.setEnabled(false);
            mOption2.setEnabled(false);
            mOption3.setEnabled(false);
            mOption4.setEnabled(false);


        }

        return itemView;
    }


    public void initData() {
        int COLOR;

        mQuiz.setText((quizItem.getQuestion()));
        mOption1.setText(quizItem.getOption1());
        mOption2.setText(quizItem.getOption2());
        mOption3.setText(quizItem.getOption3());
        mOption4.setText(quizItem.getOption4());

        /*For Read only set different color*/
        if (choiceselected == quizItem.getAnswer()) {
            COLOR = Color.GREEN;
        } else
            COLOR = Color.RED;
        switch (choiceselected) {
            case 1:
                mOption1.setChecked(true);
                if (State.isReadOnlyQuiz()) {
                    mOption1.setTextColor(COLOR);
                }
                break;
            case 2:
                mOption2.setChecked(true);
                if (State.isReadOnlyQuiz()) {
                    mOption2.setTextColor(COLOR);
                }
                break;
            case 3:
                mOption3.setChecked(true);
                if (State.isReadOnlyQuiz()) {
                    mOption3.setTextColor(COLOR);
                }
                break;
            case 4:
                mOption4.setChecked(true);
                if (State.isReadOnlyQuiz()) {
                    mOption4.setTextColor(COLOR);
                }
                break;
        }

        //Load image from cache
        if(Images.isImageCached(quizItem.getPhotoURL()))
            mImageView.setImageBitmap(Images.getBitmapfromURL(quizItem.getPhotoURL()));
        else
            new LoadImage(this, 200, 300).execute(quizItem.getPhotoURL());

        if (State.isReadOnlyQuiz()) {
            mExplain.setText(Constants.CORRECT_ANSWER + quizItem.getAnswer() + "\n" + Constants.EXPLANATION + quizItem.getAnsExp());
            mExplain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onImageLoad(Bitmap bitmap) {

        mImageView.setImageBitmap(bitmap);
        Images.addImageToCache(quizItem.getPhotoURL(),bitmap);

    }

    public View getRootView() {
        return mRootView;
    }

}