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
import com.example.cse371.R;
import com.example.cse371.Review;
import com.example.cse371.ReviewDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private LayoutInflater mInflater;
    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.mInflater = LayoutInflater.from(context);
        this.mReviews = reviews;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.content.setText(review.getContent());
        holder.review = review;
        if (review.getImagePath().length()>0) {
            downloadImage(review.getImagePath(), holder.image);
        }
    }

    private void downloadImage(String filename,ImageView imageView){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();

        storageReferenceProfilePic.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //System.out.println(uri);
                Glide.with(mContext).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        ImageView image;
        Review review;

        ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReviewDetailsActivity.class);
                    intent.putExtra("review",review);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}