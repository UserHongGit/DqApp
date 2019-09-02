package com.hong.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hong.R;
import com.hong.mvp.model.AddModel;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class MyAdapter extends SwipeMenuAdapter<MyAdapter.MyViewHolder> {
    private static final String TAG = MyAdapter.class.getSimpleName()+"_______--";

    public interface OnItemClickListener {
        void onItemClick(int position , View v);
    }

    private List<AddModel> strList;
    private OnItemClickListener mOnItemClickListener;

    public MyAdapter(List<AddModel> strList){
        Log.i(TAG, "MyAdapter: ");
        this.strList = strList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        Log.i(TAG, "setOnItemClickListener: ");
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateContentView: ");
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
    }

    @Override
    public MyAdapter.MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        Log.i(TAG, "onCompatCreateViewHolder: ");
        MyViewHolder myViewHolder = new MyViewHolder(realContentView);
        myViewHolder.mOnItemClickListener = mOnItemClickListener;
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        AddModel addModel = strList.get(position);
        Log.i(TAG, "onBindViewHolder: ___________"+addModel);
        holder.content.setText(addModel.getContent());
        holder.sfwcEt.setText(addModel.getIsOver());
        holder.sj1.setText(addModel.getSj1());
        holder.sj2.setText(addModel.getSj2());
        holder.sggxAddEt1.setText(addModel.getCs1());
        holder.sggxAddEt2.setText(addModel.getCs2());
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return strList == null ? 0 : strList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener;
        TextView num;
        TextView del;
        TextView sfwcEt;
        TextView content;
        TextView sggxAddEt1;
        TextView sggxAddEt2;
        TextView sj1;
        TextView sj2;


        public MyViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "MyViewHolder: ");
            num = (TextView) itemView.findViewById(R.id.num);
            del = (TextView) itemView.findViewById(R.id.del);
            sfwcEt = (TextView) itemView.findViewById(R.id.sfwcEt);
            content = (TextView) itemView.findViewById(R.id.et_input);
            sggxAddEt2 =(TextView) itemView.findViewById(R.id.sggxEt2);
            sggxAddEt1 = (TextView)itemView.findViewById(R.id.sggxEt1);
            sj1 = (TextView)itemView.findViewById(R.id.sjItemEt1);
            sj2 = (TextView)itemView.findViewById(R.id.sjItemEt2);
            del.setOnClickListener(this);
            sfwcEt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: ");
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}