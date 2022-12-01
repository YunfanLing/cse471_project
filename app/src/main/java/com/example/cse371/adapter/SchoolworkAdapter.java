package com.example.cse371.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.CourseActivity;
import com.example.cse371.CourseMenuActivity;
import com.example.cse371.R;
import java.util.ArrayList;

public class SchoolworkAdapter extends RecyclerView.Adapter<SchoolworkAdapter.ViewHolder> {

    private ArrayList<String> mTitiles;
    private LayoutInflater mInflater;
    private Context mContext;

    public SchoolworkAdapter(Context context, ArrayList<String> titles) {
        this.mInflater = LayoutInflater.from(context);
        this.mTitiles = titles;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.schoolwork_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mTitiles.get(position);
        holder.title.setText(title);
        holder.titlestr = title;
    }

    @Override
    public int getItemCount() {
        return mTitiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        String titlestr;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (titlestr.equals("CSE") || titlestr.equals("ECE")){
                        Intent intent = new Intent(mContext, CourseMenuActivity.class);
                        intent.putExtra("name",titlestr);
                        mContext.startActivity(intent);
                    }else{
                        Toast.makeText(mContext, "No course!!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}