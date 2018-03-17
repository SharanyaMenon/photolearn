package com.example.toshimishra.photolearn.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.LearningTitle;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.Utilities.State;

import java.util.List;

public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.SampleRecyclerHolder> {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Class<?> cls;

    public SampleRecyclerAdapter(Context context,List<String> dataSet,Class<?> cls){


        mDatas = dataSet;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.cls = cls;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 用于创建自定义的ViewHolder，在这里初始化Item的布局成ItemView，并传给自定义ViewHolder
     * Used to create a custom ViewHolder, which initializes the layout of the Item into ItemView and passes it to the custom ViewHolder.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SampleRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        if(State.isTrainerMode())
            itemView= mInflater.inflate(R.layout.item_edit,parent,false);
        else
            itemView= mInflater.inflate(R.layout.item_view,parent,false);
        return new SampleRecyclerHolder(itemView);
    }

    /**
     * 用于绑定数据的
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



    /**---------  增加删除item、更新当前item的方法  ---------**/
    /**
     * 在那个位置添加数据
     * @param position
     */
    public void add(int position){
        mDatas.add(position,"HowardItem"+position);
        notifyItemInserted(position);
    }

    /**
     * 在哪个位置移除数据
     * @param position
     */
    public void remove(int position){
        mDatas.remove(position);
        //只刷新一个位置
        notifyItemRemoved(position);
    }




    /**
     * RecyclerView的item的点击监听器
     * The click listener for the item of the recycle clerview.
     */
    public interface OnItemClickListener{
        /**
         * 条目点击事件Item click event
         * @param view
         *  当前的itemViewThe current itemView
         * @param position
         *  当前的条目的positionThe position of the current entry.
         * @param name
         *  当前条目的数据Current entry data.
         */
        void onItemClick(View view, int position, String name);
    }

    /**
     * 内部类，Holder。用于缓存ViewInner class, Holder.
     Used to cache View
     */
    public class SampleRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**---------  发挥Holder的作用  Play the role of Holder.---------**/
        Button mBtn1;
        Button mBtn2;
        TextView mTv;
        View itemView;

        public SampleRecyclerHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            //No edit/delete buttons in case of view only mode
            if (State.isTrainerMode()){
                mBtn1 = (Button) itemView.findViewById(R.id.btn1);
                mBtn2 = (Button) itemView.findViewById(R.id.btn2);
                mBtn1.setOnClickListener(this);
                mBtn2.setOnClickListener(this);
            }

            mTv = (TextView) itemView.findViewById(R.id.tv);



            if(mOnItemClickListener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v,getLayoutPosition(),mDatas.get(getLayoutPosition()));
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
            if(cls == LearningSession.class)
                Toast.makeText(mContext,"Edit for LearningSession",Toast.LENGTH_SHORT).show();
            if(cls == LearningTitle.class)
                Toast.makeText(mContext,"Edit for LearningTitle",Toast.LENGTH_SHORT).show();
            if(cls == QuizTitle.class)
                Toast.makeText(mContext,"Edit for QuizTitle",Toast.LENGTH_SHORT).show();

        }
        public void delete_button_action(){
            if(cls == LearningSession.class)
                Toast.makeText(mContext,"Delete for LearningSession",Toast.LENGTH_SHORT).show();
            if(cls == LearningTitle.class)
                Toast.makeText(mContext,"Delete for LearningTitle",Toast.LENGTH_SHORT).show();
            if(cls == QuizTitle.class)
                Toast.makeText(mContext,"Delete for QuizTitle",Toast.LENGTH_SHORT).show();
        }
    }

}
