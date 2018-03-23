package com.iss.ft03se.photolearn.Adapters;

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

import com.iss.ft03se.photolearn.Models.LearningSession;
import com.iss.ft03se.photolearn.Models.LearningTitle;
import com.iss.ft03se.photolearn.Models.QuizItem;
import com.iss.ft03se.photolearn.Models.QuizTitle;
import com.iss.ft03se.photolearn.Models.Trainer;
import com.iss.ft03se.photolearn.AddLearningTitleActivity;
import com.iss.ft03se.photolearn.R;
import com.iss.ft03se.photolearn.AddQuizItemActivity;
import com.iss.ft03se.photolearn.AddQuizTitleActivity;
import com.iss.ft03se.photolearn.AddSessionActivity;
import com.iss.ft03se.photolearn.Utilities.Constants;
import com.iss.ft03se.photolearn.Utilities.State;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.List;




public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.SampleRecyclerHolder> {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Class<?> cls;
    private ListOrderedMap<String, Object> map;

    public RecyclerAdapter(Context context, List<String> dataSet, ListOrderedMap<String, Object> map, Class<?> cls) {


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

            int pos = getLayoutPosition();
            State.setUpdateMode(true);
            Bundle b = new Bundle();


            if (cls == LearningSession.class)
                if (State.isTrainerMode()) {
                    Intent i = new Intent(mContext, AddSessionActivity.class);
                    LearningSession ls = (LearningSession)map.getValue(pos);
                    b.putSerializable("value", ls);
                    i.putExtras(b);
                    mContext.startActivity(i);
                    Toast.makeText(mContext, Constants.EDIT_LEARNING_SESSION, Toast.LENGTH_SHORT).show();
                }
            if (cls == LearningTitle.class) {
                Intent i = new Intent(mContext, AddLearningTitleActivity.class);
                LearningTitle lt = (LearningTitle)map.getValue(pos);
                b.putSerializable("value", lt);
                i.putExtras(b);
                mContext.startActivity(i);
                Toast.makeText(mContext, Constants.EDIT_LEARNING_TITLE , Toast.LENGTH_SHORT).show();
            }

            if (cls == QuizTitle.class) {
                if (State.isTrainerMode()) {
                    Intent i = new Intent(mContext, AddQuizTitleActivity.class);
                    QuizTitle qt = (QuizTitle)map.getValue(pos);
                    b.putSerializable("value", qt);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
                Toast.makeText(mContext, Constants.EDIT_QUIZ_TITLE , Toast.LENGTH_SHORT).show();
            }

            if (cls == QuizItem.class) {
                if (State.isTrainerMode()) {
                    Intent i = new Intent(mContext, AddQuizItemActivity.class);
                    QuizItem qi= (QuizItem) map.getValue(pos);
                    b.putSerializable("value", qi);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
                Toast.makeText(mContext, Constants.EDIT_QUIZ_ITEM , Toast.LENGTH_SHORT).show();
            }
        }
        public void delete_button_action(){
            int  pos = getLayoutPosition();

            if (cls == LearningSession.class){
                LearningSession ls = (LearningSession) map.getValue(pos);
                String key = ls.getSessionKey();
                Toast.makeText(mContext, Constants.DELETE_LEARNING_SESSION , Toast.LENGTH_SHORT);
                ((Trainer)State.getCurrentUser()).deleteLearningSession(key);
            }

            if (cls == LearningTitle.class) {
                LearningTitle lt = (LearningTitle) map.getValue(pos);
                String key = lt.getTitleID();
                ((Trainer)State.getCurrentUser()).deleteLearningTitle(key);
                Toast.makeText(mContext, Constants.DELETE_LEARNING_TITLE , Toast.LENGTH_SHORT).show();
            }

            if (cls == QuizTitle.class) {
                QuizTitle qt = (QuizTitle) map.getValue(pos);
                String key = qt.getTitleID();
                ((Trainer)State.getCurrentUser()).deleteQuizTitle(key);
                Toast.makeText(mContext, Constants.DELETE_QUIZ_TITLE , Toast.LENGTH_SHORT).show();
            }

            if(cls == QuizItem.class) {
                QuizItem qi = (QuizItem) map.getValue(pos);
                String key = qi.getItemID();
                String url = qi.getPhotoURL();
                ((Trainer)State.getCurrentUser()).deleteQuizItem(key,url);
                Toast.makeText(mContext, Constants.DELETE_QUIZ_ITEM , Toast.LENGTH_SHORT).show();
            }
        }

    }

}
