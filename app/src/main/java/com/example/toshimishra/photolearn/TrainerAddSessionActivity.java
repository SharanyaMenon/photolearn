package com.example.toshimishra.photolearn;

/**
 * Created by Administrator on 2018/3/9.
 */
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;

public class TrainerAddSessionActivity extends Activity{
    int mYear, mMonth, mDay;
    Button btn,add_btn;
    EditText et1,et2;

   // TextView dateDisplay;
    final int DATE_DIALOG = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_add_session);

       /*choice date*/
        btn = (Button) findViewById(R.id.dateChoose);
        et1 = (EditText)findViewById(R.id.txt1) ;
        et2 = (EditText)findViewById(R.id.txt2);
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

        add_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = et1.getText().toString();
                int moduleNumber = Integer.parseInt(et2.getText().toString());
                Date date = new GregorianCalendar(mYear, mMonth, mDay).getTime();
                ((Trainer)(State.getCurrentUser())).createLearningSession(date,moduleNumber,courseCode);
                finish();

            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
   public void display() {
       btn.setText(new StringBuffer().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear).append(" "));
   }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
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
            return true;
        }
        else if(i == R.id.action_switch){
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return  true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}
