package com.example.cse371;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ReviewDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        Bundle b = getIntent().getExtras();
        Review review = null;
        if(b != null) {
            review = (Review) b.getSerializable("review");
        }
        if (review!=null){
            TextView content = findViewById(R.id.content);
            content.setText(review.getContent());
            ImageView imageView = findViewById(R.id.image);
            downloadImage(review.getImagePath(),imageView);
        }
    }

    private void downloadImage(String filename, ImageView imageView){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();

        storageReferenceProfilePic.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //System.out.println(uri);
                Glide.with(ReviewDetailsActivity.this).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}