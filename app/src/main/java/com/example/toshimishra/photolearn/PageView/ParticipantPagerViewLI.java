package com.example.toshimishra.photolearn.PageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Models.Participant;
import com.example.toshimishra.photolearn.ParticipantEditmodeAddLearningItem;
import com.example.toshimishra.photolearn.Utilities.LoadImage;
import com.example.toshimishra.photolearn.ParticipantEditmodeViewLearningItems;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.Utilities.State;
import com.example.toshimishra.photolearn.Utilities.TexttoSpeech;


public class ParticipantPagerViewLI implements LoadImage.Listener {

    private final View mRootView;
    private TextView mPhotoDesc;
    private TextView mGPS;
    private String photoDesc;
    private String gps;
    private String photoURL;
    private ImageView mImageView;
    private String itemID;
    Button mDelete;
    Button mUpdate;
    public ParticipantEditmodeViewLearningItems mMainActivity;
    public final LayoutInflater mInflater;
    private int width;
    private Button mListen;

    public ParticipantPagerViewLI(ParticipantEditmodeViewLearningItems mainActivity,String itemID ,String photoDesc, String gps, String photoURL) {
        super();
        mMainActivity = mainActivity;
        mInflater =  LayoutInflater.from(mMainActivity);
        mRootView = initView();


        this.itemID = itemID;
        this.photoDesc = photoDesc;
        this.gps = gps;
        this.photoURL = photoURL;

    }


    public View initView() {
        View itemView = mInflater.inflate(R.layout.activity_participant_pager_view_li,null);
        mPhotoDesc = (TextView) itemView.findViewById(R.id.descirbe);
        mPhotoDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        width = itemView.getWidth();
        mGPS = (TextView) itemView.findViewById(R.id.GPS);
        mDelete = (Button)itemView.findViewById(R.id.delete);
        mUpdate=(Button)itemView.findViewById(R.id.update);
        mImageView = (ImageView)itemView.findViewById(R.id.img);
        mListen = (Button)itemView.findViewById(R.id.listen);
        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListen.setEnabled(false);
                new TexttoSpeech(mMainActivity.getApplicationContext(),mPhotoDesc.getText().toString(),mListen);

            }
        });
        if(State.isEditMode() || State.isTrainerMode())
            mListen.setVisibility(View.GONE);
        if(!State.isEditMode() || State.isTrainerMode())
        {
            mDelete.setVisibility(View.GONE);
            mUpdate.setVisibility(View.GONE);
        }
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("itemID",itemID);
                b.putString("photoDesc",photoDesc);
                b.putString("gps",gps);
                b.putString("photoURL",photoURL);

                State.setUpdateMode(true);
                Intent i = new Intent(mMainActivity,ParticipantEditmodeAddLearningItem.class);
                i.putExtras(b);
                mMainActivity.startActivity(i);
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Participant) State.getCurrentUser()).deleteLearningItem(itemID,photoURL);
            }
        });

        return itemView;
    }


    public void initData() {

        mPhotoDesc.setText(photoDesc);
        mGPS.setText(gps);
        new LoadImage(this, 200, 150).execute(photoURL);

    }
    @Override
    public void onImageLoaded(Bitmap bitmap) {

        mImageView.setImageBitmap(bitmap);
    }
    public View getRootView(){
        return mRootView;
    }
}
