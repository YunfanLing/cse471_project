package com.example.cse371;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity {
    ArrayList<Review> reviewlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle b = getIntent().getExtras();
        String name = "";
        if(b != null) {
            name = b.getString("name");
        }
        String finalName = name;
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("name", finalName);
                intent.putExtras(b);
                startActivityForResult(intent, 9999);
            }
        });

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("review").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerView listview = findViewById(R.id.recyclerView);
                    listview.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
                    listview.addItemDecoration(new DividerItemDecoration(ReviewActivity.this,LinearLayoutManager.VERTICAL));
                    reviewlist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists() && document.get("name").equals(finalName)) {
                            Review review = new Review();
                            review.setName(String.valueOf(document.get("name")));
                            review.setContent(String.valueOf(document.get("content")));
                            review.setImagePath(String.valueOf(document.get("image")));
                            reviewlist.add(review);
                        }
                    }
                    ReviewAdapter adapter = new ReviewAdapter(ReviewActivity.this,reviewlist);
                    listview.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 9999) {
                Review review = (Review) data.getSerializableExtra("review");
                reviewlist.add(review);
                ReviewAdapter adapter = new ReviewAdapter(ReviewActivity.this,reviewlist);
                RecyclerView listview = findViewById(R.id.recyclerView);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}