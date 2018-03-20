package com.example.toshimishra.photolearn;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;


import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.PageView.ParticipantPagerViewQI;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticipantAttemptQuizItemActivity extends AppCompatActivity {

    Button mTerminate;
    private ViewPager mViewPager;
    Toolbar toolbar;

    //ViewPager How many pages are there?
    List<ParticipantPagerViewQI> mPageViews = new ArrayList<>();

    //The data source
    // List<String> mStrings = new ArrayList<>();

    List<QuizItem> quizItemList = new ArrayList<>();
    HashMap<String, Integer> answers = new HashMap();

    private ParticipantAttemptQuizItemActivity.MyAdapter mAdapter;
    private TextView mTvNum;
    private Button mExit;
    private int mCurrentCount = 1;//默认为1
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private int attemptedQuestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialization data
        initView();//Initialization interface
        initEvent();
        initDatas();


    }

    private void initDatas() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        mDatabase2 = database.getReference().child(Constants.USERS_QUIZ_TITLE_QUIZ_ITEM_QUIZ_ANSWER_DB).child(getUid()).child(State.getCurrentQuizTitle().getTitleID());
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                answers.clear();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    answers.put(val.getKey(), val.getValue(QuizAnswer.class).getOptionSelcted());
                }
                updateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase = database.getReference().child(Constants.LEARNING_SESSION_QUIZ_TITLES_QUIZ_ITEMS_DB).child(State.getCurrentSession().getSessionID()).child(State.getCurrentQuizTitle().getTitleID());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mPageViews.clear();
                quizItemList.clear();


                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    addPage(val.getValue(QuizItem.class));

                }
                mAdapter.notifyDataSetChanged();
                updateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initView() {
        setContentView(R.layout.activity_participant_attempt_quiz_item);
        mTvNum = (TextView) findViewById(R.id.tvnum);
        mTerminate = (Button) findViewById(R.id.Terminate);
        mExit = (Button) findViewById(R.id.exit);


        updateUI();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        // toolbar.setLogo(R.drawable.timg);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!State.isReadOnlyQuiz()) {
            mExit.setVisibility(View.GONE);
        } else
            mTerminate.setVisibility(View.GONE);


        // Get the viewpage instance.
        mViewPager = (ViewPager) findViewById(R.id.viewPage);


        // The PagerAdapter is associated with ViewPager, and the data source is indirectly bound to the ViewPager.
        mAdapter = new ParticipantAttemptQuizItemActivity.MyAdapter();
        mViewPager.setAdapter(mAdapter);


    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return quizItemList.size();
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
            ParticipantPagerViewQI basePageView = mPageViews.get(position);
            View rootView = basePageView.getRootView();
            basePageView.initData();
            container.addView(rootView);
            return rootView;//Return to show
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    /**
     * shhow popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow, null);

        // Here are two ways to get width and height. getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // Set popWindow pop-up window to click, which must be added, and true.
        window.setFocusable(true);


        // Instantiate a ColorDrawable color as translucent.
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);


        // Set popWindow display and disappear animation.
        window.setAnimationStyle(R.style.mypopwindow_anim_style);


        // Show At the bottom
        window.showAtLocation(ParticipantAttemptQuizItemActivity.this.findViewById(R.id.Terminate),
                Gravity.BOTTOM, 0, 0);


        //popWindow Vanishing listening method
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });
        // check if the button in popWindow can be clicked.
        Button first = (Button) view.findViewById(R.id.first);
        first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                State.removeAnswers();
                finish();
            }
        });

        Button second = (Button) view.findViewById(R.id.second);
        second.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        Button third = (Button) view.findViewById(R.id.third);
        third.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                window.dismiss();
            }
        });


    }


    private void initEvent() {
        mTerminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTerminate.getText() == Constants.TERMINATE)
                    showPopwindow();
                if (mTerminate.getText() == Constants.SUBMIT) {
                    int score = generateScore(quizItemList, answers);
                    Intent intent = new Intent(getBaseContext(), ParticipantCompleteQuiz.class);
                    intent.putExtra("SCORE", String.valueOf(score));
                    intent.putExtra("MAX_SCORE", String.valueOf(quizItemList.size()));
                    startActivity(intent);
                    finish();
                }


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
    public void addPage(QuizItem item) {
        int choiceselected = 0;
        if (answers.containsKey(item.getItemID()))
            choiceselected = answers.get(item.getItemID());
        ParticipantPagerViewQI basePageView = new ParticipantPagerViewQI(this, item, choiceselected);
        quizItemList.add(item);
        updateUI();

        mPageViews.add(basePageView);//Add a data to the data source.

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private int generateScore(List<QuizItem> quizItemList, HashMap<String, Integer> answers) {
        int score = 0;
        for (int i = 0; i < answers.size(); i++) {
            QuizItem quiz = quizItemList.get(i);
            if (answers.get(quiz.getItemID()) == quiz.getAnswer())
                score++;
        }
        return score;
    }

    private void updateUI() {
        if (answers.size() == quizItemList.size())
            mTerminate.setText(Constants.SUBMIT);
        else
            mTerminate.setText(Constants.TERMINATE);

        if (quizItemList.size() == 0) {
            mTvNum.setText(Constants.NO_QUIZ);
            mTerminate.setVisibility(View.GONE);
        } else {
            mTerminate.setVisibility(View.VISIBLE);
            mTvNum.setText(mCurrentCount + " / " + mAdapter.getCount());
        }
        if (State.isReadOnlyQuiz())
            mTerminate.setVisibility(View.GONE);
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
        if(State.isTrainerMode()){
            finish();
        }
        Log.d("TrainerSessionsActivity","onStart********");

    }

}

