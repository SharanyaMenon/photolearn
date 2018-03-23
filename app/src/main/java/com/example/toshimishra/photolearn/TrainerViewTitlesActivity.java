package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Adapters.SampleRecyclerAdapter;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.example.toshimishra.photolearn.fragment.FirstFragment;
import com.example.toshimishra.photolearn.fragment.SecondFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class TrainerViewTitlesActivity extends AppCompatActivity implements SampleRecyclerAdapter.OnItemClickListener {


    private ViewPager viewPager;

    private TabLayout tabLayout;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> tabs = new ArrayList<>();

    private ViewHolder holder;
    Toolbar toolbar;


    private TextView text;

    @Override
    public void onItemClick(View view, int position, String name) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_view_titles);

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

        initData();
        initView();
        text = (TextView) findViewById(R.id.title_LT);
        text.setText(State.getCurrentSession().getCourseCode());///not working

    }

    private void initData() {
        tabs.add("Learning Title");
        tabs.add("Quiz Title");
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        setTabView();
    }


    private void setTabView() {
        holder = null;
        for (int i = 0; i < tabs.size(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);

            tab.setCustomView(R.layout.tab_item);
            holder = new ViewHolder(tab.getCustomView());

            holder.tvTabName.setText(tabs.get(i));

            if (i == 0) {
                holder.tvTabName.setSelected(true);
                holder.tvTabName.setTextSize(18);
            }
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                holder = new ViewHolder(tab.getCustomView());
                holder.tvTabName.setSelected(true);

                holder.tvTabName.setTextSize(18);

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                holder = new ViewHolder(tab.getCustomView());
                holder.tvTabName.setSelected(false);

                holder.tvTabName.setTextSize(16);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class ViewHolder {
        TextView tvTabName;

        public ViewHolder(View tabView) {
            tvTabName = (TextView) tabView.findViewById(R.id.tv_tab_name);
        }
    }

    class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }

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
        if(!State.isTrainerMode()){
            finish();
        }
    }
}
