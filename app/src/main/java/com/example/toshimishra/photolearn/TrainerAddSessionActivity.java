package com.example.toshimishra.photolearn;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TrainerAddSessionActivity extends AppCompatActivity {
    int mYear, mMonth, mDay;
    Button btn, add_btn;
    EditText et1, et2;
    String key;
    private boolean isDateSelected = false;

    final int DATE_DIALOG = 1;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_add_session);


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



        btn = (Button) findViewById(R.id.dateChoose);
        et1 = (EditText) findViewById(R.id.txt1);
        et2 = (EditText) findViewById(R.id.txt2);
        add_btn = (Button) findViewById(R.id.bc_btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG);
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        if (!State.isUpdateMode()) {
            add_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String module = et2.getText().toString();
                    String courseCode = et1.getText().toString();
                    Date date = new GregorianCalendar(mYear, mMonth, mDay).getTime();

                    if (module == null || module.isEmpty() || courseCode == null || courseCode.isEmpty() || !isDateSelected) {
                        Toast.makeText(TrainerAddSessionActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    } else {
                        int moduleNumber = Integer.parseInt(module);
                        ((Trainer) (State.getCurrentUser())).createLearningSession(date, moduleNumber, courseCode);
                        finish();
                    }
                }
            });
        } else {

            add_btn.setText(Constants.UPDATE);
            ((TextView) findViewById(R.id.textView2)).setText("Update Learning Session");
            Bundle b = getIntent().getExtras();
            final LearningSession s = (LearningSession) b.getSerializable("value");
            key = s.getSessionKey();
            final Date[] d = new Date[1];


            et2.setText(s.getModuleNumber().toString());
            et1.setText(s.getCourseCode());
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            btn.setText(dateFormat.format(s.getCourseDate()).toString());
            d[0] = s.getCourseDate();

            add_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String module = et2.getText().toString();
                    String courseCode = et1.getText().toString();
                    Date date = new GregorianCalendar(mYear, mMonth, mDay).getTime();

                    if (!isDateSelected) {
                        isDateSelected = true;
                        date = d[0];
                    }

                    if (module == null || module.isEmpty() || courseCode == null || courseCode.isEmpty() || !isDateSelected) {
                        Toast.makeText(TrainerAddSessionActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    } else {
                        int moduleNumber = Integer.parseInt(module);
                        ((Trainer) (State.getCurrentUser())).updateLearningSession(key, date, courseCode, moduleNumber);
                        finish();
                    }

                }
            });
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:

                return new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, mdateListener, mYear, mMonth, mDay);

        }
        return null;
    }

    /**
     * Set the date to be appended with a StringBuffer.
     */
    public void display() {
        btn.setText(new StringBuffer().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear).append(" "));

    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            isDateSelected = true;
            display();


        }
    };

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
        if (!State.isTrainerMode()) {
            finish();
        }
    }

}
