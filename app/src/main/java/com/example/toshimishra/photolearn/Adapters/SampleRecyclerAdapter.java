package com.example.toshimishra.photolearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.ParticipantEditmodeAddLearningTitle;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.TrainerAddQuizItem;
import com.example.toshimishra.photolearn.TrainerAddQuizTitle;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.TrainerAddSessionActivity;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.HashMap;
import java.util.List;



public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.SampleRecyclerHolder> {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Class<?> cls;
    private HashMap<String, String> map;

    public SampleRecyclerAdapter(Context context, List<String> dataSet, HashMap<String, String> map, Class<?> cls) {


        mDatas = dataSet;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.cls = cls;
        this.map = map;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     *
     * Used to create a custom ViewHolder, which initializes the layout of the Item into ItemView and passes it to the custom ViewHolder.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SampleRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        if (State.isTrainerMode())
            itemView = mInflater.inflate(R.layout.item_edit, parent, false);
        else
            itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new SampleRecyclerHolder(itemView);
    }

    /**
     *
     * Used to bind data.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final SampleRecyclerHolder holder, final int position) {
        holder.mTv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



    /**--------- Add the method to delete item and update the current item.  ---------**/
    /**
     *
     * @param position
     */
    public void add(int position) {
        mDatas.add(position, "HowardItem" + position);
        notifyItemInserted(position);
    }

    /**
     * Where to remove the data.
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        //Refresh only one location.
        notifyItemRemoved(position);
    }




    /**
     *
     * The click listener for the item of the recycle clerview.
     */
    public interface OnItemClickListener {
        /**
         * Item click event
         *
         * @param view     The current itemView
         * @param position The position of the current entry.
         * @param name     Current entry data.
         */
        void onItemClick(View view, int position, String name);
    }

    /**
     * Inner class, Holder.
     * Used to cache View
     */
    public class SampleRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * ---------    Play the role of Holder.---------
         **/
        Button mBtn1;
        Button mBtn2;
        TextView mTv;
        View itemView;

        public SampleRecyclerHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            //No edit/delete buttons in case of view only mode
            if (State.isTrainerMode()) {
                mBtn1 = (Button) itemView.findViewById(R.id.btn1);
                mBtn2 = (Button) itemView.findViewById(R.id.btn2);
                mBtn1.setOnClickListener(this);
                mBtn2.setOnClickListener(this);
            }

            mTv = (TextView) itemView.findViewById(R.id.tv);


            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, getLayoutPosition(), mDatas.get(getLayoutPosition()));
                    }
                });
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    edit_button_action();
                    break;
                case R.id.btn2:
                    delete_button_action();
                    break;

                default:
                    break;
            }
        }

        public void edit_button_action(){
            String value = mDatas.get(getLayoutPosition());
            String key = getKeyFromValue(value);
            State.setUpdateMode(true);
            Bundle b = new Bundle();
            b.putString("key", key);
            b.putString("value", value);
            if (cls == LearningSession.class)
                if (State.isTrainerMode()) {
                    Intent i = new Intent(mContext, TrainerAddSessionActivity.class);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
                if (cls == LearningTitle.class) {
                    Intent i = new Intent(mContext, ParticipantEditmodeAddLearningTitle.class);
                    i.putExtras(b);
                    mContext.startActivity(i);
                    Toast.makeText(mContext, Constants.EDIT_LEARNING_TITLE + value, Toast.LENGTH_SHORT).show();
                }

                if (cls == QuizTitle.class) {
                    if (State.isTrainerMode()) {
                        Intent i = new Intent(mContext, TrainerAddQuizTitle.class);
                        i.putExtras(b);
                        mContext.startActivity(i);
                    }
                Toast.makeText(mContext, Constants.EDIT_QUIZ_TITLE + value, Toast.LENGTH_SHORT).show();
                }
                if (cls == QuizItem.class) {
                    if (State.isTrainerMode()) {
                        Intent i = new Intent(mContext, TrainerAddQuizItem.class);
                        i.putExtras(b);
                        mContext.startActivity(i);
                    }
                Toast.makeText(mContext, Constants.EDIT_QUIZ_ITEM + value, Toast.LENGTH_SHORT).show();
                }
        }
        public void delete_button_action(){
            String value = mDatas.get(getLayoutPosition());
            String key = getKeyFromValue(value);
            if (cls == LearningSession.class){
                Toast.makeText(mContext, Constants.DELETE_LEARNING_SESSION + value, Toast.LENGTH_SHORT);
                ((Trainer)State.getCurrentUser()).deleteLearningSession(key);
            }


            if (cls == LearningTitle.class)
                new Trainer().deleteLearningTitle(key);
            Toast.makeText(mContext, Constants.DELETE_LEARNING_TITLE + value, Toast.LENGTH_SHORT).show();
            if (cls == QuizTitle.class) {
                new Trainer().deleteQuizTitle(key);
                Toast.makeText(mContext, Constants.DELETE_QUIZ_TITLE + value, Toast.LENGTH_SHORT).show();
            }
            if(cls == QuizItem.class) {
                new Trainer().deleteQuizItem(key);
                Toast.makeText(mContext, Constants.DELETE_QUIZ_ITEM + value, Toast.LENGTH_SHORT).show();
            }
        }

        public String getKeyFromValue(String value) {
            for (String s : map.keySet()) {
                if (map.get(s).equals(value)) {
                    return s;
                }
            }
            return null;
        }
    }

}
