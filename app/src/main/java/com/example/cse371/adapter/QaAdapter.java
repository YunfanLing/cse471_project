package com.example.cse371.adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cse371.Qa;
import com.example.cse371.QaReply;
import com.example.cse371.R;
import com.example.cse371.ReplyActivity;
import com.example.cse371.Review;
import com.example.cse371.ReviewDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class QaAdapter extends RecyclerView.Adapter<QaAdapter.ViewHolder> {

    private ArrayList<Qa> qas;
    private LayoutInflater mInflater;
    private Context mContext;

    public QaAdapter(Context context, ArrayList<Qa> qas) {
        this.mInflater = LayoutInflater.from(context);
        this.qas = qas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.qa_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Qa qa = qas.get(position);
        holder.qatv.setText(qa.getQa());
        holder.qa_authortv.setText(qa.getQa_author());
        holder.qa = qa;
    }

    @Override
    public int getItemCount() {
        return qas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView qatv;
        TextView qa_authortv;
        Qa qa;
        ViewHolder(View itemView) {
            super(itemView);
            qatv = itemView.findViewById(R.id.qa);
            qa_authortv = itemView.findViewById(R.id.qa_author);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReplyActivity.class);
                    intent.putExtra("qa",qa);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}