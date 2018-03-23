package com.iss.ft03se.photolearn.PageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iss.ft03se.photolearn.Models.Participant;
import com.iss.ft03se.photolearn.AddLearningItemActivity;
import com.iss.ft03se.photolearn.Utilities.Images;
import com.iss.ft03se.photolearn.Utilities.LoadImage;
import com.iss.ft03se.photolearn.ViewLearningItemsActivity;
import com.iss.ft03se.photolearn.R;
import com.iss.ft03se.photolearn.Utilities.State;
import com.iss.ft03se.photolearn.Utilities.TexttoSpeech;


public class PagerViewLearningtem implements LoadImage.ImageCallBack {

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
    public ViewLearningItemsActivity mMainActivity;
    public final LayoutInflater mInflater;
    private int width;
    private Button mListen;

    public PagerViewLearningtem(ViewLearningItemsActivity mainActivity, String itemID , String photoDesc, String gps, String photoURL) {
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
        View itemView = mInflater.inflate(R.layout.pager_view_learning_item,null);
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
                Intent i = new Intent(mMainActivity,AddLearningItemActivity.class);
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
        if(Images.isImageCached(photoURL))
            mImageView.setImageBitmap(Images.getBitmapfromURL(photoURL));
        else
            new LoadImage(this, 200, 300).execute(photoURL);

    }
    @Override
    public void onImageLoad(Bitmap bitmap) {

        mImageView.setImageBitmap(bitmap);
        Images.addImageToCache(photoURL,bitmap);
    }
    public View getRootView(){
        return mRootView;
    }
}
