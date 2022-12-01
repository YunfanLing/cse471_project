package com.example.cse371.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.Qa;
import com.example.cse371.QaReply;
import com.example.cse371.R;
import com.example.cse371.ReplyActivity;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {

    private ArrayList<QaReply> qaReplies;
    private LayoutInflater mInflater;
    private Context mContext;

    public ReplyAdapter(Context context, ArrayList<QaReply> qaReplies) {
        this.mInflater = LayoutInflater.from(context);
        this.qaReplies = qaReplies;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.qa_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QaReply qa = qaReplies.get(position);
        holder.qatv.setText(qa.getContent());
        holder.qa_authortv.setText(qa.getName());
    }

    @Override
    public int getItemCount() {
        return qaReplies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView qatv;
        TextView qa_authortv;
        ViewHolder(View itemView) {
            super(itemView);
            qatv = itemView.findViewById(R.id.qa);
            qa_authortv = itemView.findViewById(R.id.qa_author);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ReplyActivity.class);
//                    intent.putExtra("qa",qa);
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}