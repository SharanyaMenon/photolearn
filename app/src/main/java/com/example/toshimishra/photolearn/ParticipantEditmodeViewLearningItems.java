package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.PageView.ParticipantPagerViewLI;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ParticipantEditmodeViewLearningItems extends AppCompatActivity {
    private Button mButton;
    private ViewPager mViewPager;
    Toolbar toolbar;


    List<ParticipantPagerViewLI> mPageViews = new ArrayList<>();

    //The data source
    // List<String> mStrings = new ArrayList<>();
    List<String> mPhotoURL = new ArrayList<>();
    List<String> mPhotoDesc = new ArrayList<>();
    List<String> mGPS = new ArrayList<>();
    private MyAdapter mAdapter;
    private TextView mTvNum,mTitle_LS,mTitle_Q;
    private int mCurrentCount = 1;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initView();
        initEvent();
        initDatas();


    }

    private void initDatas() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child(Constants.LEARNING_SESSION_LEARNING_TITLES_LEARNING_ITEMS_DB).child(State.getCurrentSession().getSessionKey()).child(State.getCurrentLearningTitle().getTitleID());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGPS.clear();
                mPhotoDesc.clear();
                mPhotoURL.clear();
                mPageViews.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    addPage(val.getValue(LearningItem.class));

                }
                mAdapter.notifyDataSetChanged();
                if (mPhotoDesc.size() == 0)
                    mTvNum.setText(0 + " / " + 0);
                else
                    mTvNum.setText(mCurrentCount + " / " + mAdapter.getCount());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_participant_editmode_view_learningitems);
        mButton = (Button) findViewById(R.id.bt_Add);
        mTvNum = (TextView) findViewById(R.id.tvnum);
        mTitle_LS = (TextView)findViewById(R.id.title_LS);
        mTitle_Q = (TextView)findViewById(R.id.title_Q);
        mTitle_LS.setText(State.getCurrentSession().getSessionID());
        mTitle_Q.setText(State.getCurrentLearningTitle().getTitle());
        if (mPhotoDesc.size() == 0)
            mTvNum.setText(0 + " / " + 0);
        else
            mTvNum.setText(mCurrentCount + " / " + mAdapter.getCount());


        // Get the viewpage instance.
        mViewPager = (ViewPager) findViewById(R.id.viewPage);


        // The PagerAdapter is associated with ViewPager, and the data source is indirectly bound to the ViewPager.
        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);

        if (!State.isEditMode() || State.isTrainerMode())
            mButton.setVisibility(View.GONE);

        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPhotoDesc.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ParticipantPagerViewLI basePageView = mPageViews.get(position);
            View rootView = basePageView.getRootView();
            basePageView.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Jump to the second Activity.
                State.setUpdateMode(false);
                Intent intent = new Intent(ParticipantEditmodeViewLearningItems.this, ParticipantEditmodeAddLearningItem.class);
                startActivityForResult(intent, 0);
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentCount = position + 1;
                mTvNum.setText(mCurrentCount + " / " + mAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String datas = data.getStringExtra("backString");
            if (TextUtils.isEmpty(datas)) {
                return;
            }

        }
    }

    /**
     * This method encapsulates the code logic implementation of the added page, and the parameter text is the data to be displayed.
     */
    public void addPage(LearningItem item) {

        ParticipantPagerViewLI basePageView = new ParticipantPagerViewLI(this, item.getItemID(), item.getPhotoDesc(), item.getGps(), item.getPhotoURL());
        mPhotoURL.add(item.getPhotoURL());
        mPhotoDesc.add(item.getPhotoDesc());
        mGPS.add(item.getGps());
        mPageViews.add(basePageView);//Add a data to the data source.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return false;
        } else if (i == R.id.action_switch) {
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle b = getIntent().getExtras();
        String flag = b.getString("flag");

        if(flag ==null){
            finish();
        }

        if (State.isTrainerMode() && !flag.equals("true")) {
            finish();
        }


    }
}


